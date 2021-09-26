package com.billcom.app.service;


import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import java.util.HashSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.billcom.app.dto.TeamDto;
import com.billcom.app.dto.count.CountDto;
import com.billcom.app.entity.Chat;
import com.billcom.app.entity.Notification;
import com.billcom.app.entity.Role;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamLeader;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.UserApp;
import com.billcom.app.exception.ForbiddenException;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.TaskRepository;
import com.billcom.app.repository.TeamMemberRepository;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

import javassist.expr.NewArray;

@Service

public class TeamService {
	private TeamRepository teamRepository;
	private UserRepository userRepository;
	private TeamMemberRepository teamMemberRepository;
	private SecurityUtils securityUtils;
	private TaskRepository taskRepository;


	public TeamService(TeamRepository teamRepository, UserRepository userRepository,
			TeamMemberRepository teamMemberRepository, SecurityUtils securityUtils, TaskRepository taskRepository) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.securityUtils = securityUtils;
		this.taskRepository = taskRepository;
	}

	/** Get Logged User **/
	public UserApp getCurrentUser() {
		return securityUtils.getLoggedUser();
	}

	/**
	 * save team
	 * 
	 * @param teamDto
	 * @return Team
	 */
	public Team saveTeam(TeamDto team) {
		UserApp userLogged = getCurrentUser();
		if (securityUtils.checkUserRole(userLogged)) {
			Set<TeamMember> memberList = new HashSet<>();
			TeamLeader leader = new TeamLeader();
			for (Long idList : team.getIdmembers()) {
				UserApp user = userRepository.findById(idList).get();
                
				for (Role r : user.getRoles()) {
					if (r.getName().equalsIgnoreCase("member")) {
						TeamMember teamMember = new TeamMember(user);
						teamMemberRepository.save(teamMember);
						memberList.add(teamMember);
						Set<Notification> notificationList = user.getNotifcation();
						
						notificationList.add(new Notification(securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName() +" invited you to be a team member for  ",team.getTeamName(),securityUtils.getLoggedUser().getId() , LocalDateTime.now()));

						
						user.setNotifcation(notificationList);
		                userRepository.save(user);
					
					
					}
				}
			}
			UserApp user = userRepository.findById(team.getIdLeader()).get();
			for (Role r : user.getRoles()) {
				if (r.getName().equalsIgnoreCase("leader")) {
					
					leader.setUser(user);
					 Set<Notification> notificationList = user.getNotifcation();
						
						notificationList.add(new Notification(securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName() +" invited you to be a team lead for  ",team.getTeamName(),securityUtils.getLoggedUser().getId() , LocalDateTime.now()));

						
						user.setNotifcation(notificationList);
		             userRepository.save(user);
				
				
				}
			}
			
			Team newTeam = new Team(team.getTeamName(), memberList, leader,team.getStartedDate(),team.getDueDate());

			return teamRepository.save(newTeam);
		} else {
			throw new ForbiddenException("Access Denid Exception");

		}
	}

	/** Add member To Team */

	public Team addMemberToTeam(long id, Set<Long> list) {
		UserApp userLogged = getCurrentUser();

		Team team = teamRepository.getOne(id);
		if (securityUtils.checkIfUserLoggedIsTeamLeader(userLogged, team)) {
			Set<TeamMember> memberList = team.getTeamMember();
			for (Long idList : list) {
				UserApp user = userRepository.findById(idList).get();
				{	
					Set<Notification> notificationList = user.getNotifcation();
				
				notificationList.add(new Notification(securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName() +" invited you to be a team member for ",team.getTeamName(),securityUtils.getLoggedUser().getId() , LocalDateTime.now()));

				
				user.setNotifcation(notificationList);
                userRepository.save(user);
					for (Role r : user.getRoles()) {
						if ((r.getName().equals("member")) & (fetchIntoList(memberList, user.getEmail()) == false)) {
							TeamMember member = new TeamMember(user);
							teamMemberRepository.save(member);
							memberList.add(member);
						}
					}
				}

			}

			
			team.setTeamMember(memberList);
			return teamRepository.save(team);
		} else {
			throw new ForbiddenException(" Leader Does not have access to add Team Member");
		}
	}

	/** Add Leader To Team **/

	public Team addLeaderToTeam(long id, long idUser) {
		UserApp userLogged = getCurrentUser();

		Team team = teamRepository.findById(id).get();
		if (securityUtils.checkIfUserLoggedIsTeamLeader(userLogged, team)) {

			UserApp user = userRepository.findById(idUser).get();
			for (Role r : user.getRoles()) {
				if (r.getName().equals("leader")) {
					TeamLeader leader = new TeamLeader(user);
					team.setLeader(leader);

				}
			}
			return teamRepository.save(team);

		} else {
			throw new ForbiddenException(" Leader Does not have access to add Team Member");
		}
	}

	/**
	 * delete a member from team (member/leader)
	 * 
	 * @param id   team
	 * @param list of id user to delete
	 * @return team after change
	 */
	@Transactional
	public Team deleteEmployeeFromTeam(long id, Set<Long> list) {
		UserApp userLogged = getCurrentUser();
		Team team = teamRepository.getOne(id);
		if (securityUtils.checkIfUserLoggedIsTeamLeader(userLogged, team)) {

			Set<TeamMember> memberList = team.getTeamMember();
			for (Long idList : list) {
				TeamMember user = teamMemberRepository.getMemberById(idList)
						.orElseThrow(() -> new NotFoundException("member not found"));
				taskRepository.deleteAllByTeamMember(user);
				memberList.remove(user);
				teamMemberRepository.delete(user);

			}
			team.setTeamMember(memberList);
			
			return teamRepository.save(team);
		} else {
			throw new ForbiddenException(" you should be the team leader to delete the member");
		}
	}

	/**
	 * showteam detail
	 * 
	 * @param id team to show
	 * @return Team
	 */
	public Team getTeam(Long id) {
		Team team = teamRepository.getOne(id);
		UserApp userLogged = getCurrentUser();
		if (securityUtils.checkifUserLoggedIsManagerOrProjectLeaderorTeamLeader(userLogged, team))

		{
			return teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team not found"));
		} else {
			throw new ForbiddenException(" you are forbeddin");

		}
	}

	/**
	 * 
	 * @return all team List
	 */
	public List<Team> getAllTeamList() {
		UserApp userLogged = getCurrentUser();

		if (userLogged.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("manager"))) {
			return teamRepository.findAll();

		} else {
			throw new ForbiddenException(" you should be Manager to show all team list");

		}

	}

	/**
	 * return team Member list for specefic team
	 * 
	 * @param id
	 * @return
	 */
	public Set<TeamMember> getTeamMember(long id) {
		Team team = teamRepository.findById(id).orElseThrow(()-> new NotFoundException("All detail team does not exist !"));

		return team.getTeamMember();
	}

	public boolean fetchIntoList(Set<TeamMember> memberList, String email) {
		for (TeamMember m : memberList) {
			if (m.getUser().getEmail().equals(email))
				return true;
		}
		return false;
	}

	/** Delete Team **/
	public void deleteTeam(long id) {
		Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team Not Found"));
		Set<TeamMember> memberList = team.getTeamMember();
		for (TeamMember member : memberList) {	
			taskRepository.deleteAllByTeamMember(member);

		}
		teamRepository.delete(team);
	}

	public TeamMember getMemberById(long id) {
		return teamMemberRepository.findById(id).get();

	}

	/**
	 * 
	 * @return List of team for a specefic team Lead
	 */
	public List<Team> teamListOfLeader() {
		return teamRepository.findAll().stream().filter(team -> team.getLeader().getUser().getId() == getCurrentUser().getId()).filter(team-> team.getDueDate().isAfter(LocalDateTime.now()) & team.getStartDate().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());

	}
	
	public List<Team> passedTeamListOfLeader() {
		return teamRepository.findAll().stream().filter(team -> team.getLeader().getUser().getId() == getCurrentUser().getId()).filter(team-> team.getDueDate().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());

	}
	public List<Team> futureTeamListOfLeader() {
		return teamRepository.findAll().stream().filter(team -> team.getLeader().getUser().getId() == getCurrentUser().getId()).filter(team-> team.getDueDate().isBefore(LocalDateTime.now()) & team.getStartDate().isBefore(LocalDateTime.now()))
				.collect(Collectors.toList());

	}
	
	
	

	/**
	 * 
	 * @return List of team that member is a part of them
	 */
	public List<TeamDto> teamOfTeamMember() {
		UserApp user = getCurrentUser();
		List<Team> listTeam = teamRepository.findAll();
		List<TeamDto> list = new ArrayList<>();
		for (Team t : listTeam) {
			for (TeamMember teamMember : t.getTeamMember()) {
				if (teamMember.getUser().getId() == user.getId()) {

					list.add(new TeamDto(t.getTeamName(),t.getLeader().getUser().getId(),t.getStartDate(),t.getDueDate(), t.getId(),teamMember.getId(),t.getLeader().getUser().getFirstName() + "" + t.getLeader().getUser().getLastName(),teamMember));
					
				}
			}
		}
		return list;

	}
	
	public TeamDto getTeamSpecefic( long id ) {
		
		Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team Not Found"));

		return new TeamDto(team.getTeamName(),team.getLeader().getUser().getId(),team.getStartDate(),team.getDueDate(), team.getLeader().getUser().getFirstName() + " " + team.getLeader().getUser().getLastName());
		
	}
	
    

	/**
	 * count number of tasks done from team for everey team
	 * 
	 * @return number of task done for every team
	 */
	public List<CountDto> taskDoneTeamMeber() {
		UserApp user = getCurrentUser();
		List<CountDto> countTaskDoneDto = new ArrayList<>();
		for (TeamDto t : teamOfTeamMember()) {
				if (t.getTeamMember().getUser().getId() == user.getId() & ! taskRepository.findAllByTeamMember(t.getTeamMember()).isEmpty() &t.getDueDate().isAfter(LocalDateTime.now())) {
				
					float nbDone =( taskRepository.findAllByTeamMember(t.getTeamMember()).stream()
							.filter(task -> task.getStatus().getStatusDescritpion().equalsIgnoreCase("done")).count() * 100)/(taskRepository.findAllByTeamMember(t.getTeamMember()).size());
					String teamName = t.getTeamName();
					countTaskDoneDto.add(new CountDto( teamName,nbDone));
				}}
			
		
		return countTaskDoneDto;

	}

    public Set<Chat> getChatList(long id){
    	return teamRepository.findById(id).get().getChatList();
    }
         
}




