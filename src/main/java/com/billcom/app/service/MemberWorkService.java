package com.billcom.app.service;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.billcom.app.dto.TeamDto;
import com.billcom.app.dto.WorkDto;
import com.billcom.app.dto.WorkDtoList;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.UserApp;
import com.billcom.app.entity.WorkMember;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.repository.TeamWorkRepository;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

@Service
public class MemberWorkService {
	private TeamRepository teamRepository;
	private UserRepository userRepository;
	private SecurityUtils securityUtils;
	private TeamWorkRepository teamWorkRepository;

	public MemberWorkService(TeamRepository teamRepository, UserRepository userRepository, SecurityUtils securityUtils,
			TeamWorkRepository teamWorkRepository) {
		this.teamRepository = teamRepository;
		this.userRepository = userRepository;
		this.securityUtils = securityUtils;
		this.teamWorkRepository = teamWorkRepository;
	}

	public WorkMember addHourToTeam(long idTeam, double hour) {


		UserApp user = securityUtils.getLoggedUser();
		Team team = teamRepository.findById(idTeam).get();
		if (!teamWorkRepository.findAll().stream().collect(Collectors.toList()).isEmpty()) {
			if (teamWorkRepository.findAll().stream().filter(teamWork -> teamWork.getTeam().getId() == team.getId())
					.findAny().isPresent()) {
				if (teamWorkRepository.findAll().stream()
						.filter(teamWork -> teamWork.getTeam().getId() == team.getId()
								& teamWork.getDateHour().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
						.findAny().isPresent()) {

					WorkMember work = teamWorkRepository.findAll().stream()
							.filter(teamWork -> teamWork.getTeam().getId() == team.getId()
									& teamWork.getDateHour().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())
							.findAny().get();

					work.setNbHour(hour);
					return teamWorkRepository.save(work);
				} else {
					WorkMember newWork = new WorkMember(hour, LocalDateTime.now(), team, user);
					return teamWorkRepository.save(newWork);
				}
			} else {

				WorkMember newWork = new WorkMember(hour, LocalDateTime.now(), team, user);
				return teamWorkRepository.save(newWork);
			}
		} else {
			WorkMember newWork = new WorkMember(hour, LocalDateTime.now(), team, user);
			return teamWorkRepository.save(newWork);
		}
	}

	public Set<WorkDto> getListWorkWithoutCurrentDay (){
		
			
	
	Set<WorkDto> workDtoList2 = teamWorkRepository.findAll().stream()
			.filter(work -> work.getUser().getId() == securityUtils.getLoggedUser().getId())
			.collect(Collectors.mapping(w -> new WorkDto(w.getNbHour(), w.getDateHour(), w.getTeam().getTeamName(),w.getTeam().getStartDate(),w.getTeam().getDueDate()),
					Collectors.toSet()));
	Set<WorkDto> workDtoList = 	workDtoList2.stream()
			.filter(workDto -> workDto.getStartedteam().isBefore(LocalDateTime.now()))
			.filter(workDto -> workDto.getEndteam().isAfter(LocalDateTime.now()))
			.filter(workDto -> workDto.getDate().getDayOfMonth() != LocalDateTime.now().getDayOfMonth())

          .collect(Collectors.toSet());
		
	return workDtoList;
	}

	
	public List<WorkDtoList> workMemberList() {

		
			

		Map<LocalDateTime, List<WorkDto>> workList = getListWorkWithoutCurrentDay().stream()
				.collect(Collectors.groupingBy(work -> work.getDate()));
		List<WorkDtoList> allWorkDtoList = new ArrayList<>();
		Iterator<Map.Entry<LocalDateTime, List<WorkDto>>> iterator = workList.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<LocalDateTime, List<WorkDto>> entryy = iterator.next();
			WorkDtoList workDto = new WorkDtoList(entryy.getKey(), entryy.getValue());
			allWorkDtoList.add(workDto);
		}

		return allWorkDtoList;
	}

	public Set<WorkDto> getCurrentDayStatistic(){
		
		
		for (TeamDto t : teamOfTeamMember()) {
			if (teamWorkRepository.findAll().stream().filter(work -> work.getTeam().getId() == t.getId() & work.getDateHour().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()).findAny().isEmpty() ) {
				Team team = new Team(t.getId(),t.getTeamName(),t.getStartedDate(),t.getDueDate());
				WorkMember newWork = new WorkMember(0, LocalDateTime.now(), team, securityUtils.getLoggedUser());
				 teamWorkRepository.save(newWork);
			}
		}
	

	Set<WorkDto> workDtoList2 = teamWorkRepository.findAll().stream()
			.filter(work -> work.getUser().getId() == securityUtils.getLoggedUser().getId())
			.collect(Collectors.mapping(w -> new WorkDto(w.getNbHour(), w.getDateHour(), w.getTeam().getTeamName(),w.getTeam().getStartDate(),w.getTeam().getDueDate(),w.getTeam().getId()),
					Collectors.toSet()));
	Set<WorkDto> workDtoList = 	workDtoList2.stream()
			.filter(workDto -> workDto.getStartedteam().isBefore(LocalDateTime.now()))
			.filter(workDto -> workDto.getEndteam().isAfter(LocalDateTime.now()))
			.filter(workDto -> workDto.getDate().getDayOfMonth() == LocalDateTime.now().getDayOfMonth())

          .collect(Collectors.toSet());
		
	return workDtoList;
		
		
		
		
		
		
	}
	
	public List<WorkDtoList> workMemberListCurrentDay() {

		
		

		Map<LocalDateTime, List<WorkDto>> workList = getCurrentDayStatistic().stream()
				.collect(Collectors.groupingBy(work -> work.getDate()));
		List<WorkDtoList> allWorkDtoList = new ArrayList<>();
		Iterator<Map.Entry<LocalDateTime, List<WorkDto>>> iterator = workList.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<LocalDateTime, List<WorkDto>> entryy = iterator.next();
			WorkDtoList workDto = new WorkDtoList(entryy.getKey(), entryy.getValue());
			allWorkDtoList.add(workDto);
		}

		return allWorkDtoList;
	}
	
	
	
	
	
	
	public double getNbHour() {
		 Set <WorkMember > workDayList =  teamWorkRepository.findAll().stream().filter(work -> work.getDateHour().getMonth() == LocalDateTime.now().getMonth() & work.getDateHour().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()).collect(Collectors.toSet());
	  
		 
		 double totalHour = 0;
	   
	   for( WorkMember work  : workDayList ) {
		   totalHour = totalHour + work.getNbHour();
	   }
		 return totalHour;
	
	
	}
	
	

	
	
	
	
	/**
	 * 
	 * @return List of team that member is a part of them
	 */
 	public List<TeamDto> teamOfTeamMember() {
		UserApp user = securityUtils.getLoggedUser();
		List<Team> listTeam = teamRepository.findAll();
		List<TeamDto> list = new ArrayList<>();
		for (Team t : listTeam) {
			for (TeamMember teamMember : t.getTeamMember()) {
				if (teamMember.getUser().getId() == user.getId() & t.getStartDate().isBefore(LocalDateTime.now())
						& t.getDueDate().isAfter(LocalDateTime.now())) {

					list.add(new TeamDto(t.getTeamName(), t.getLeader().getUser().getId(), teamMember, t.getId(),t.getStartDate(),t.getDueDate()));
				}
			}
		}
		return list;

	}

}
