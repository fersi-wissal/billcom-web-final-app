package com.billcom.app.service;

import static java.nio.file.Files.copy;


import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.hibernate.criterion.ProjectionList;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.billcom.app.dto.TeamDto;
import com.billcom.app.dto.count.CountDto;
import com.billcom.app.dto.project.ProjectDto;
import com.billcom.app.dto.project.ProjectListDto;
import com.billcom.app.entity.Notification;
import com.billcom.app.entity.Project;
import com.billcom.app.entity.Role;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamLeader;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.UserApp;
import com.billcom.app.entity.WorkMember;
import com.billcom.app.enumeration.TeamProjectStatus;
import com.billcom.app.exception.AlreadyExistsException;
import com.billcom.app.exception.ForbiddenException;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.handler.NotifictionWebSocketHandler;
import com.billcom.app.repository.ProjectRepository;
import com.billcom.app.repository.TaskRepository;
import com.billcom.app.repository.TeamMemberRepository;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.repository.TeamWorkRepository;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

@Service
public class ProjectService {

	private ProjectRepository projectRepository;
	private UserRepository userRepository;
	private SecurityUtils securityUtils;
	private TeamRepository teamRepository;
	private TeamMemberRepository teamMemberRepository;
	private TaskRepository taskRepository;
	public static final String UPLOADdIRECTORY = System.getProperty("user.dir")
			+ "/src/main/resources/static/projectFile";
    private TeamWorkRepository teamWorkRepository;
	public ProjectService(ProjectRepository projectRepository, UserRepository userRepository,
			SecurityUtils securityUtils, TeamRepository teamRepository, TeamMemberRepository teamMemberRepository,
			TaskRepository taskRepository,TeamWorkRepository teamWorkRepository) {
		this.projectRepository = projectRepository;
		this.userRepository = userRepository;
		this.securityUtils = securityUtils;
		this.teamRepository = teamRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.taskRepository = taskRepository;
		this.teamWorkRepository =teamWorkRepository;
	}

	/** Get Logged User **/
	public UserApp getCurrentUser() {
		return securityUtils.getLoggedUser();
	}

	public void saveProject(ProjectDto projectDto, @RequestParam("files") List<MultipartFile> multipartFiles)
			throws IOException {

		if (securityUtils.checkifUserLoggedIsManager(getCurrentUser())) {

			Project project = projectDto.fromDtoToProject();

			UserApp user = userRepository.findById(projectDto.getIdProjectLeader())
					.orElseThrow(() -> new NotFoundException("User Not Found"));

			Set<Notification> notificationList = user.getNotifcation();
			notificationList.add(new Notification(
					securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName()
							+ " invited you to be a poject leader for",
					projectDto.getName(), securityUtils.getLoggedUser().getId(), LocalDateTime.now()));
			user.setNotifcation(notificationList);
			project.setProjectLeader(user);

			List<String> filenames = new ArrayList<>();
			for (MultipartFile file : multipartFiles) {

				String filename = StringUtils.cleanPath(file.getOriginalFilename());

				Path fileStorage = get(UPLOADdIRECTORY, filename).toAbsolutePath().normalize();

				copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
				filenames.add(filename);

			}

			project.setFiles(filenames);
			userRepository.save(user);
			projectRepository.save(project);
		}
	}

	public Team saveTeam(TeamDto teamDto) {

		Set<TeamMember> memberList = new HashSet<>();
		TeamLeader leader = new TeamLeader();
		for (Long idList : teamDto.getIdmembers()) {
			UserApp user = userRepository.findById(idList).orElseThrow(() -> new NotFoundException("User Not found"));
			Set<Notification> notificationList = user.getNotifcation();
			notificationList.add(new Notification(
					securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName()
							+ " invited you to be a  team  member for",
					teamDto.getTeamName(), securityUtils.getLoggedUser().getId(), LocalDateTime.now()));

			user.setNotifcation(notificationList);
			userRepository.save(user);
			for (Role r : user.getRoles()) {
				if (r.getName().equalsIgnoreCase("member")) {
					TeamMember teamMember = new TeamMember(user);
					teamMemberRepository.save(teamMember);
					memberList.add(teamMember);
				}
			}
		}
		UserApp user = userRepository.findById(teamDto.getIdLeader())
				.orElseThrow(() -> new NotFoundException("User Not found"));
		Set<Notification> notificationList = user.getNotifcation();
		notificationList.add(new Notification(
				securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName()
						+ " invited you to be a  team leader for",
				teamDto.getTeamName(), securityUtils.getLoggedUser().getId(), LocalDateTime.now()));
		user.setNotifcation(notificationList);
		for (Role r : user.getRoles()) {
			if (r.getName().equalsIgnoreCase("leader")) {
				leader.setUser(user);
			}
		}

		Team newTeam = new Team(teamDto.getTeamName(), memberList, leader, teamDto.getStartedDate(),
				teamDto.getDueDate());
		return teamRepository.save(newTeam);

	}

	public Project addteamToProject(long id, TeamDto teamDto) {
		Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("project not found"));
		if (securityUtils.checkIfUserLoggedIsManger(getCurrentUser())
				|| (securityUtils.checkIfUserLoggedIsProjectLeader(getCurrentUser())
						& getCurrentUser().getId() == project.getProjectLeader().getId())) {
			if (project.getTeamList().stream().anyMatch(t -> t.getTeamName().equalsIgnoreCase(teamDto.getTeamName()))) {

				throw new AlreadyExistsException(" Project Has Team with this name ");

			}
			Team team = saveTeam(teamDto);
			Set<Team> teamList = project.getTeamList();
			teamList.add(team);
			project.setTeamList(teamList);
			return projectRepository.save(project);

		}

		else {
			throw new ForbiddenException(" Sorry you can't create team for project ");
		}

	}

	public void updateStatusProject(long id, String projectStatus) {
		Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("not found project"));
		if (securityUtils.checkIfUserLoggedIsManger(getCurrentUser())
				|| (securityUtils.checkIfUserLoggedIsProjectLeader(getCurrentUser())
						& getCurrentUser().getId() == project.getProjectLeader().getId())) {

			TeamProjectStatus teamProjectStatus = TeamProjectStatus.valueOf(projectStatus);
			project.setStatusProject(teamProjectStatus);

			projectRepository.save(project);
		}
	}

	/** Delete Team **/
	public void deleteTeam(long id) {
		Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team Not Found"));
		Set<TeamMember> memberList = team.getTeamMember();
		for (TeamMember member : memberList) {
			taskRepository.deleteAllByTeamMember(member);

		}
		Set<WorkMember> works = teamWorkRepository.findAll().stream().filter(work -> work.getTeam().getId() == team.getId()).collect(Collectors.toSet());
		for(WorkMember work :works ) {
			teamWorkRepository.deleteById(work.getId());
		}
		teamRepository.delete(team);
	}

	@Transactional
	public void deleteTeamFromProject(long id, long idTeam) {

		Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("not found project"));
		project.getTeamList().removeIf(team -> team.getId() == idTeam);
		deleteTeam(idTeam);
		projectRepository.save(project);
	}



	public float getProgressType(long id) {

		Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("project not found"));
		float nbPlannedTask = 0;
		float nbDoneTask = 0;
		if(project.getTeamList() != null) {
		for (Team team : project.getTeamList()) {
			for (TeamMember t : team.getTeamMember()) {
				nbPlannedTask = nbPlannedTask + taskRepository.findAllByTeamMember(t).stream().count();
				nbDoneTask = nbDoneTask + taskRepository.findAllByTeamMember(t).stream()
						.filter(task -> (task.getStatus().getStatusDescritpion().equalsIgnoreCase("done"))).count();

			}

		}}

		if (nbPlannedTask != 0) {

			float result = (nbDoneTask / nbPlannedTask) * 100;
			if (result > 0 & result < 100 & (project.getStatusProject().compareTo(TeamProjectStatus.PAUSED) < 0)
					& (project.getStatusProject().compareTo(TeamProjectStatus.PROGRESSING) < 0)) {
				project.setStatusProject(TeamProjectStatus.PROGRESSING);
				projectRepository.save(project);
			}

			if (result == 100 & (project.getStatusProject().compareTo(TeamProjectStatus.PAUSED) != 0)
					& (project.getStatusProject().compareTo(TeamProjectStatus.COMPLETED) != 0)) {
				project.setStatusProject(TeamProjectStatus.COMPLETED);
				project.setEffectiveDeleveryDate(LocalDateTime.now());
				projectRepository.save(project);
			}
			return result;
		}
		return 0;

	}
	public Set<ProjectListDto> getListProject() {
		return projectRepository.findAll().stream()
				.map(project -> new ProjectListDto(project, getProgressType(project.getId())))
				.collect(Collectors.toSet());
	}
	
	public ProjectListDto getProjectDeatil(long id) {

		float progress = getProgressType(id);
		return projectRepository.findById(id).map(p -> {
			return new ProjectListDto(p, progress);
		}).orElseThrow(() -> new NotFoundException("Project Not Found"));

	}

	public ResponseEntity<Resource> downloadFiles(String filename) throws IOException {
		Path filePath = get(UPLOADdIRECTORY).toAbsolutePath().normalize().resolve(filename);
		if (!Files.exists(filePath)) {
			throw new FileNotFoundException(filename + "this file ins not found");
		}

		Resource resource = new UrlResource(filePath.toUri());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("File-Name", filename);
		httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
				.headers(httpHeaders).body(resource);
	}

	public void updateProject(Project project) {
		projectRepository.findById(project.getId()).map(p -> {
			p.setName(project.getName());
			p.setStartedDate(project.getStartedDate());
			p.setEndDate(p.getEndDate());
			p.setCharge(p.getCharge());
			return projectRepository.save(project);
		});
	}

	/******************************************
	 * For delevered Project
	 **********************************************************/
	// calcul de l'écart des délai
	public List<CountDto> getProjectTimeGap() {

		List<CountDto> listTimeGap = new ArrayList<>();

		List<Project> projectList = projectRepository.findAll().stream()
				.filter(p -> p.getEffectiveDeleveryDate() != null).collect(Collectors.toList());

		for (Project p : projectList) {
			if (p.getStatusProject().compareTo(TeamProjectStatus.COMPLETED) == 0) {

				CountDto countTimeGap = new CountDto(p.getName(),
						ChronoUnit.DAYS.between(p.getEndDate(), p.getEffectiveDeleveryDate()));

				listTimeGap.add(countTimeGap);

			}

		}
		return listTimeGap;
	}
	// calcul de l'écart du durée

	public List<CountDto> getProjectDurationGap() {

		List<CountDto> listTimeGap = new ArrayList<>();

		List<Project> projectList = projectRepository.findAll().stream()
				.filter(p -> p.getEffectiveDeleveryDate() != null).collect(Collectors.toList());

		for (Project p : projectList) {
			if (p.getStatusProject().compareTo(TeamProjectStatus.COMPLETED) == 0) {

				long realDuration = ChronoUnit.DAYS.between(p.getEndDate(), p.getStartedDate());
				long initialDureation = ChronoUnit.DAYS.between(p.getEffectiveDeleveryDate(), p.getStartedDate());

				CountDto countTimeGap = new CountDto(p.getName(),
						((realDuration - initialDureation) / initialDureation));

				listTimeGap.add(countTimeGap);

			}

		}
		return listTimeGap;
	}

	/************************************
	 * For Delayed Delevered Project
	 **************************************************/

	public List<CountDto> getProjectDealyedDelevered() {

		List<CountDto> listTimeGap = new ArrayList<>();

		List<Project> projectList = projectRepository.findAll().stream()
				.filter(p -> (p.getEffectiveDeleveryDate() == null
						|| p.getStatusProject().compareTo(TeamProjectStatus.COMPLETED) != 0)
						& p.getEndDate().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());

		for (Project p : projectList) {

			CountDto countTimeGap = new CountDto(p.getName(),
					ChronoUnit.DAYS.between(p.getEndDate(), LocalDateTime.now()));

			listTimeGap.add(countTimeGap);
		}

		return listTimeGap;
	}

	/****************************************************************
	 * Processing Project Statistic
	 ************************************/
	public Set<ProjectListDto> getListProjectNotCompleted() {
		return projectRepository.findAll().stream()
				.filter(project -> project.getStatusProject().compareTo(TeamProjectStatus.COMPLETED) != 0)
				.map(project -> new ProjectListDto(project, getProgressType(project.getId())))
				.collect(Collectors.toSet());
	}

	public List<CountDto> getProjectStatistic(long id) {

		List<CountDto> listProjectStatic = new ArrayList<>();

		Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project Not Found"));

		if (project.getStartedDate().isBefore(LocalDateTime.now())) {

			listProjectStatic.add(new CountDto("Spent Time/days",
					ChronoUnit.DAYS.between(project.getStartedDate(), LocalDateTime.now())));
		} else {
			listProjectStatic.add(new CountDto("Spent Time/days", 0));

		}
		listProjectStatic.add(new CountDto("Duration Date/days ",
				ChronoUnit.DAYS.between(project.getStartedDate(), project.getEndDate())));
		return listProjectStatic;
	}

	public List<CountDto> chargeAssismentProjet(long id) {
		List<CountDto> listProjectStatic = new ArrayList<>();
		Project project = projectRepository.findById(id).orElseThrow(() -> new NotFoundException("Project Not Found"));
		double duree = Math.pow((2.5 * project.getCharge()), 0.38);
		double topt = 1.4 * duree;
		listProjectStatic.add(new CountDto(project.getName(), 0));
		listProjectStatic.add(new CountDto("Charge  project", project.getCharge()));
		listProjectStatic.add(new CountDto("Tmin mois/homme", (int) Math.round(duree)));
		listProjectStatic.add(new CountDto("Topt mois/homme", (int) Math.round(topt)));
		listProjectStatic.add(new CountDto("Nb Resource", (int) Math.round(Math.pow(project.getCharge(), 0.5))));
		return listProjectStatic;
	}

	/******************************************************
	 * Project Leader
	 ********************************************************************/
	public Set<ProjectListDto> getListProjectProjectLeader() {
		return projectRepository.findAll().stream()
				.filter(project -> project.getProjectLeader().getId() == getCurrentUser().getId())
				.map(project -> new ProjectListDto(project, getProgressType(project.getId())))
				.collect(Collectors.toSet());

	}

	// calcul de l'écart des délai du project leader
	public List<CountDto> getProjectsProjectLeaderTimeGap() {

		List<CountDto> listTimeGap = new ArrayList<>();

		Set<ProjectListDto> projectList = getListProjectProjectLeader();

		for (ProjectListDto p : projectList) {
			if (p.getProject().getStatusProject().compareTo(TeamProjectStatus.COMPLETED) == 0) {

				CountDto countTimeGap = new CountDto(p.getProject().getName(), ChronoUnit.DAYS
						.between(p.getProject().getEndDate(), p.getProject().getEffectiveDeleveryDate()));

				listTimeGap.add(countTimeGap);

			}

		}
		return listTimeGap;
	}

	/**
	 * 
	 * @return project delayed where logged user is project leader
	 */

	public List<CountDto> getProjectLeaderDealyedDelevered() {

		List<CountDto> listTimeGap = new ArrayList<>();


		List<ProjectListDto> projectList = getListProjectProjectLeader().stream()
				.filter(p -> (p.getProject().getEffectiveDeleveryDate() == null
						|| p.getProject().getStatusProject().compareTo(TeamProjectStatus.COMPLETED) != 0)
						& p.getProject().getEndDate().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());

		for (ProjectListDto p : projectList) {

			CountDto countTimeGap = new CountDto(p.getProject().getName(),
					ChronoUnit.DAYS.between(p.getProject().getEndDate(), LocalDateTime.now()));

			listTimeGap.add(countTimeGap);
		}

		return listTimeGap;
	}
	public Set<ProjectListDto> getListProjectLeaderNotCompleted() {
		return  getListProjectProjectLeader().stream()
				.filter(p -> p.getProject().getStatusProject().compareTo(TeamProjectStatus.COMPLETED) != 0)
				.map(p -> new ProjectListDto(p.getProject(), getProgressType(p.getProject().getId())))
				.collect(Collectors.toSet());
	}
	public ProjectDto getProjectTeamDetail(long id) {
		
	Set<Project> projectList = 	projectRepository.findAll().stream().collect(Collectors.toSet());
	
	for( Project p : projectList) {
		for(Team team : p.getTeamList()) {
		if(team.getId() == id){
			return  new ProjectDto(p.getName(), p.getDescription(), p.getEndDate(), p.getStartedDate(),
					p.getProjectLeader().getFirstName() +" " +  p.getProjectLeader().getLastName(),p.getProjectLeader().getId());
		}}}
		
		return null;
	}
}
