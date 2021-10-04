package com.billcom.app.service;

import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import com.billcom.app.dto.CommentDto;
import com.billcom.app.dto.TaskDto;
import com.billcom.app.dto.TaskStatusDto;
import com.billcom.app.dto.TeamTask;
import com.billcom.app.dto.count.CountDto;
import com.billcom.app.entity.Comment;
import com.billcom.app.entity.Notification;
import com.billcom.app.entity.Status;
import com.billcom.app.entity.Task;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.UserApp;
import com.billcom.app.enumeration.TaskPriority;
import com.billcom.app.exception.ForbiddenException;
import com.billcom.app.exception.IllegalArgumentException;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.CommentRepository;
import com.billcom.app.repository.StateRepository;
import com.billcom.app.repository.TaskRepository;
import com.billcom.app.repository.TeamMemberRepository;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Service

public class TaskService {
	private TaskRepository taskRepository;
	private TeamMemberRepository teamMemberRepository;
	private StateRepository statusRepository;
	private SecurityUtils securityUtils;
	private TeamRepository teamRepository;
	private TeamService teamService;
	private UserRepository userRepository;
	private CommentRepository commentRepository;
	public static final String TASKDIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/taskFile";

	public TaskService(TaskRepository taskRepository, TeamMemberRepository teamMemberRepository,
			StateRepository statusRepository, SecurityUtils securityUtils, TeamRepository teamRepository,
			TeamService teamService, CommentRepository commentRepository, UserRepository userRepository) {
		this.taskRepository = taskRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.teamRepository = teamRepository;
		this.commentRepository = commentRepository;
		this.statusRepository = statusRepository;
		this.securityUtils = securityUtils;
		this.teamService = teamService;
		this.userRepository = userRepository;
	}

	/****
	 * save Task from user
	 * 
	 * @param taskDto
	 * @return
	 */
	public UserApp getCurrentUser() {
		return securityUtils.getLoggedUser();
	}

	public Task saveTask(TaskDto taskDto) {
		UserApp userLogged = getCurrentUser();
		Task task = taskDto.fromDtoToTask();
		TeamMember teamMember = teamMemberRepository.findById(taskDto.getIdMember())
				.orElseThrow(() -> new NotFoundException("TeamMember Does Not Exist"));

		Long teamId = teamRepository.getJoin(teamMember.getId());
		if (taskDto.getStartedDate().isBefore(teamRepository.findById(teamId).get().getStartDate())
				|| taskDto.getStartedDate().isAfter(teamRepository.findById(teamId).get().getDueDate())
				|| taskDto.getDeleveryDate().isAfter(teamRepository.findById(teamId).get().getDueDate())) {
			throw new IllegalArgumentException("Date is out of periode for team");
		}

		if (userLogged.getId() == teamMember.getUser().getId()
				|| userLogged.getId() == teamRepository.findById(teamId).get().getLeader().getUser().getId()
				|| securityUtils.checkUserRole(userLogged)
				|| securityUtils.checkIfUserLoggedIsProjectLeader(userLogged))

		{
			if (statusRepository.findByStatusDescritpion(taskDto.getState()).isPresent()) {
				Status status = statusRepository.findByStatusDescritpion(taskDto.getState()).get();
				task.setStatus(status);
			} else {
				Status statu = new Status(taskDto.getState());
				statusRepository.save(statu);
				task.setStatus(statu);
			}
			TaskPriority priority = TaskPriority.valueOf(taskDto.getPriority());
			task.setCreationDate(taskDto.getStartedDate());
			task.setTeamMember(teamMember);
			task.setTaskPriority(priority);
			task.setCreatedBy(userLogged.getFirstName() + " " + userLogged.getLastName());
			if (teamMember.getUser().getId() != securityUtils.getLoggedUser().getId()) {
				Set<Notification> notificationList = teamMember.getUser().getNotifcation();
				notificationList.add(new Notification(
						securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName()
								+ " assigned you a new task ",
						taskDto.getTaskName(), securityUtils.getLoggedUser().getId(), LocalDateTime.now()));
				teamMember.getUser().setNotifcation(notificationList);
				userRepository.save(teamMember.getUser());

			}

			return taskRepository.save(task);
		}
		return null;
	}

	/**
	 * update Task Status
	 * 
	 * @param id
	 * @param status
	 * @return Task
	 */
	public Task updateStatusTask(long id, String status) {

		Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task Does Not Exists"));
		Status statu = statusRepository.findByStatusDescritpion(status)
				.orElseThrow(() -> new NotFoundException("Status Does Not Exist! You should create it"));
		if (status.equalsIgnoreCase("Done")) {
			task.setEffectiveDueDate(LocalDateTime.now());
		}

		task.setStatus(statu);
		return taskRepository.save(task);

	}

	/**
	 * 
	 * @param id
	 * @param coment
	 * @return Task with comment added
	 */

	public void deleteTask(long id) {
		if (taskRepository.findById(id).isPresent()
				& taskRepository.findById(id).get().getTeamMember().getUser().getId() != getCurrentUser().getId()) {
			taskRepository.delete(taskRepository.findById(id).get());
		} else {

			throw new ForbiddenException(" You Can not delete task! ");
		}

	}

	public List<TaskStatusDto> getTasksMember(long id) {
		TeamMember teamMember = teamMemberRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Team Member Does Not Exist"));

		List<Task> tasks = taskRepository.findAllByTeamMember(teamMember).stream()
				.collect(Collectors.mapping(
						task -> new Task(task.getId(), task.getTaskName(), task.getDescriptionTask(),
								task.getTaskPriority(), task.getStatus(), task.getDeleveryDate(), task.getCreatedBy()),
						Collectors.toList()));

		Map<String, List<Task>> testTask = tasks.stream()
				.collect(Collectors.groupingBy(task -> task.getStatus().getStatusDescritpion()));

		List<TaskStatusDto> allTaskDtoList = new ArrayList<>();

		Iterator<Map.Entry<String, List<Task>>> iterator = testTask.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<Task>> entryy = iterator.next();
			TaskStatusDto taskStatusDto = new TaskStatusDto(entryy.getValue().get(0).getStatus().getId(),
					entryy.getKey(), entryy.getValue());
			allTaskDtoList.add(taskStatusDto);

		}
		Comparator<TaskStatusDto> compareById = new Comparator<TaskStatusDto>() {
			@Override
			public int compare(TaskStatusDto t1, TaskStatusDto t2) {
				return t1.getId().compareTo(t2.getId());
			}
		};

		Collections.sort(allTaskDtoList, compareById);
		return allTaskDtoList;
	}

	
	public List<TaskStatusDto> getTasksTeam(long id) {
		Team team = teamRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Team Does Not Exist"));
		List<Task> tasks = new ArrayList<>();
		
		for(TeamMember t : team.getTeamMember() ) {

		tasks.addAll(taskRepository.findAllByTeamMember(t).stream()
				.collect(Collectors.mapping(
						task -> new Task(task.getId(), task.getTaskName(), task.getDescriptionTask(),
								task.getTaskPriority(), task.getStatus(), task.getDeleveryDate(), task.getCreatedBy(),t),
						Collectors.toList())));
		}
		
		
		
		
		Map<String, List<Task>> testTask = tasks.stream()
				.collect(Collectors.groupingBy(task -> task.getStatus().getStatusDescritpion()));

		List<TaskStatusDto> allTaskDtoList = new ArrayList<>();

		Iterator<Map.Entry<String, List<Task>>> iterator = testTask.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, List<Task>> entryy = iterator.next();
			TaskStatusDto taskStatusDto = new TaskStatusDto(entryy.getValue().get(0).getStatus().getId(),
					entryy.getKey(), entryy.getValue());
			allTaskDtoList.add(taskStatusDto);

		}
		Comparator<TaskStatusDto> compareById = new Comparator<TaskStatusDto>() {
			@Override
			public int compare(TaskStatusDto t1, TaskStatusDto t2) {
				return t1.getId().compareTo(t2.getId());
			}
		};

		Collections.sort(allTaskDtoList, compareById);
		return allTaskDtoList;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Task getTaskDetails(long id) {
		return taskRepository.findById(id).orElseThrow(() -> new NotFoundException(" Task does not found"));
	}

	/**
	 * get all task for a team Member
	 * 
	 * @param id
	 * @return
	 */
	public List<List<Task>> getTasksAllTeamMember(long id) {
		Set<TeamMember> teamList = teamService.getTeamMember(id);
		List<List<Task>> tasksallteamMember = new ArrayList<>();
		for (TeamMember t : teamList) {

			List<Task> tasks = taskRepository.findAllByTeamMember(t).stream().collect(Collectors.mapping(
					task -> new Task(task.getId(), task.getTaskName(), task.getDescriptionTask(),
							task.getTaskPriority(), task.getStatus(), task.getDeleveryDate(), task.getCreatedBy()),
					Collectors.toList()));

			tasksallteamMember.add(tasks);
		}

		return tasksallteamMember;
	}

	/***********************************************
	 * Management Comment
	 ************************************************************/

	/**
	 * add Comment to task's Set
	 **/

	public Task addCommentToTask(long id, CommentDto commentDto) {
		Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("task Not Found"));
		UserApp userLogged = getCurrentUser();
		Comment newComment = new Comment(commentDto.getContent(), userLogged);
		task.getComments().add(newComment);
		if ( task.getTeamMember().getUser().getId() != userLogged.getId() ) {
		Set<Notification> notificationList = task.getTeamMember().getUser().getNotifcation();

		notificationList.add(new Notification(
				securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName()
						+ " commented your task  ",
				task.getTaskName(), securityUtils.getLoggedUser().getId(), LocalDateTime.now()));

		task.getTeamMember().getUser().setNotifcation(notificationList);}
		return taskRepository.save(task);

	}

	/** get Tas's Set Comment **/

	public Set<Comment> getTasksComment(long id) {

		Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("task Not Found"));
		return task.getComments();
	}

	/** update Task's comment **/

	public Comment setCommentOfTask(long id, CommentDto commentDto) {

		Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("team Not Found "));

		comment.setContent(commentDto.getContent());
		return commentRepository.save(comment);
	}

	/** delete Comment **/

	public void deleteComment(long id) {

		Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException("team Not Found "));
		UserApp userLogged = getCurrentUser();

		if (comment.getUser().getId() == userLogged.getId()) {
			commentRepository.deleteById(id);
		} else {
			throw new ForbiddenException(" Only author can delete his comment");
		}

	}

	public Comment getComment(long id) {
		return commentRepository.findById(id).get();
	}

	public List<Task> getTasksBeetweenDates(long id, String firstdDate, String lastDate) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime firstDate = LocalDateTime.parse(firstdDate, formatter);
		LocalDateTime finalDate = LocalDateTime.parse(lastDate, formatter);

		TeamMember teamMember = teamMemberRepository.getMemberById(id).get();

		return taskRepository.findAllByTeamMember(teamMember).stream()
				.filter(task -> task.getDeleveryDate().isAfter(firstDate) & task.getDeleveryDate().isBefore(finalDate))
				.collect(Collectors.toList());
	}

	public void setDueDate(long id, String lastDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

		LocalDateTime finalDate = LocalDateTime.parse(lastDate, formatter);

		taskRepository.findById(id).map(task -> {
			task.setDeleveryDate(finalDate);

			return taskRepository.save(task);
		}).orElseThrow(() -> new NotFoundException("task Not Found"));

	}

	public List<TeamTask> getTeamMembersTaskTwoDate(long id, String firstdDate, String lastDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		LocalDateTime firstDate = LocalDateTime.parse(firstdDate, formatter);
		LocalDateTime finalDate = LocalDateTime.parse(lastDate, formatter);

		List<TeamTask> taskListTeam = new ArrayList<>();
		long idUser = teamMemberRepository.getMemberById(id).get().getUser().getId();
		long teamId = teamRepository.getJoin(teamMemberRepository.getMemberById(id).get().getId());

		taskListTeam = taskRepository.findAll().stream()
				.filter(task -> task.getTeamMember().getUser().getId() == idUser)
				.filter(task -> !(task.getStatus().getStatusDescritpion().equalsIgnoreCase("Done")))
				.filter(task -> task.getCreationDate().isAfter(firstDate) & task.getDeleveryDate().isBefore(finalDate))
				.map(task -> new TeamTask(task, teamRepository.findById(teamId).get())).collect(Collectors.toList());

		taskListTeam.addAll(taskRepository.findAll().stream()
				.filter(task -> task.getTeamMember().getUser().getId() == idUser)
				.filter(task -> !(task.getStatus().getStatusDescritpion().equalsIgnoreCase("Done")))
				.filter(task -> task.getCreationDate().isBefore(firstDate) & task.getDeleveryDate().isAfter(firstDate) & task.getDeleveryDate().isBefore(finalDate))
				.map(task -> new TeamTask(task, teamRepository.findById(teamId).get())).collect(Collectors.toList()));
		
		   taskListTeam.addAll(taskRepository.findAll().stream()
				.filter(task -> task.getTeamMember().getUser().getId() == idUser)
				.filter(task -> !(task.getStatus().getStatusDescritpion().equalsIgnoreCase("Done")))
				.filter(task -> task.getCreationDate().isBefore(finalDate) & task.getCreationDate().isAfter(firstDate) & task.getDeleveryDate().isAfter(finalDate))
				.map(task -> new TeamTask(task, teamRepository.findById(teamId).get())).collect(Collectors.toList()));
	
		taskListTeam.addAll(taskRepository.findAll().stream()
				.filter(task -> task.getTeamMember().getUser().getId() == idUser)
				.filter(task -> !(task.getStatus().getStatusDescritpion().equalsIgnoreCase("Done")))
				.filter(task -> task.getCreationDate().isBefore(firstDate) & task.getDeleveryDate().isAfter(finalDate))
				.map(task -> new TeamTask(task, teamRepository.findById(teamId).get())).collect(Collectors.toList()));
	
		/*taskListTeam.addAll(taskRepository.findAll().stream()
				.filter(task -> task.getTeamMember().getUser().getId() == idUser)
				.filter(task -> !(task.getStatus().getStatusDescritpion().equalsIgnoreCase("Done")))
				.filter(task -> task.getCreationDate().isBefore(firstDate) & task.getDeleveryDate().isBefore(finalDate))
				.map(task -> new TeamTask(task, teamRepository.findById(teamId).get())).collect(Collectors.toList()));*/
		
		return taskListTeam;
	}

	/***************************************************
	 * Management Dashboard Task
	 ***********************************************/
	public List<CountDto> getTaskStatusBuGroup(long id) {
		TeamMember teamMember = teamMemberRepository.getMemberById(id).get();
		List<Task> taskMember = taskRepository.findAllByTeamMember(teamMember);

		Map<Status, Long> countMap = taskMember.stream()
				.collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

		List<CountDto> countTaskDtoList = new ArrayList<>();

		Iterator<Entry<Status, Long>> iterator = countMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Status, Long> entryy = iterator.next();
			Status status = entryy.getKey();
			CountDto countTaskDto = new CountDto(status.getStatusDescritpion(),
					(entryy.getValue() * 100) / taskMember.size());
			countTaskDtoList.add(countTaskDto);

		}

		return countTaskDtoList;

	}

	/************************************************************
	 * ******* Management Files
	 *********************************************************/

	/**
	 * define a method to ipload files
	 * 
	 * @param id
	 * @param multipartFiles
	 * @return
	 * @throws IOException
	 */
	public List<String> uploadFiles(long id, List<MultipartFile> multipartFiles) throws IOException {
		Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("task not found"));
		List<String> filenames = task.getFiles();
		for (MultipartFile file : multipartFiles) {

			String filename = StringUtils.cleanPath(file.getOriginalFilename());

			Path fileStorage = get(TASKDIRECTORY, filename).toAbsolutePath().normalize();

			copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
			filenames.add(filename);

		}

		task.setFiles(filenames);
		taskRepository.save(task);
		return filenames;
	}

	public ResponseEntity<Resource> downloadFiles(String filename) throws IOException {
		Path filePath = get(TASKDIRECTORY).toAbsolutePath().normalize().resolve(filename);
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
}
