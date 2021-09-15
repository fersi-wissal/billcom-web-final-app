package com.billcom.app.controller;

import java.util.List;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.billcom.app.dto.TeamDto;
import com.billcom.app.dto.count.CountDto;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.Chat;
import com.billcom.app.entity.Team;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.service.TeamService;

@RestController
@RequestMapping(value="team")
public class TeamController {
	@Autowired
	private TeamService teamService;

	@Autowired
	private TeamRepository teamRepository;

	public TeamController(TeamService teamService) {
		this.teamService = teamService;
	}

	@PostMapping("/addTeam")
	public Team addTeam(@RequestBody TeamDto team) {
		return teamService.saveTeam(team);
	}

	@PutMapping("/addMemberToTeam/{idTeam}")
	public Team addMemberToTeam(@PathVariable long idTeam, @RequestBody Set<Long> list) {
		return teamService.addMemberToTeam(idTeam, list);
	}

	@PutMapping("/addLeaderToTeam/{idTeam}")
	public Team addLeaderToTeam(@PathVariable long idTeam, @RequestParam Long idLeader) {
		return teamService.addLeaderToTeam(idTeam, idLeader);
	}

	@GetMapping("/detailTeam/{idTeam}")
	public Team detailTeam(@PathVariable long idTeam) {
		return teamService.getTeam(idTeam);

	}

	@DeleteMapping("/deleteMemberFromTeam/{idTeam}")
	public Team deleteMemberFromTeam(@PathVariable long idTeam, @RequestBody Set<Long> idMemberList) {
		return teamService.deleteEmployeeFromTeam(idTeam, idMemberList);

	}



	@GetMapping("/getTeamMember/{idTeam}")
	public Set<TeamMember> getTeamMember(@PathVariable long idTeam) {
		return teamService.getTeamMember(idTeam);

	}

	@GetMapping("/getChatList/{id}")
	public Set<Chat> getChatList(@PathVariable long id) {
		return teamService.getChatList(id);

	}
	@GetMapping("/getAllTeamList")
	public List<Team> getAllTeamList() {
		return teamService.getAllTeamList();
	}

	@DeleteMapping("/deleteTeam/{idTeam}")
	public void deleteTeam(@PathVariable long idTeam) {
		teamService.deleteTeam(idTeam);
		
	}
	
	@GetMapping("/getTeamMemberDetail/{id_team}")
	public Long getTeamMemberDetail(@PathVariable("id_team") long id_team) {
		 return teamRepository.getJoin(id_team);
	}
	@GetMapping("/teamListOfLeader")
	public  List<Team> teamListOfLeader() {
		 return teamService.teamListOfLeader();
	}
	@GetMapping("/teamOfTeamMember")
	public  List<TeamDto> teamOfTeamMember() {
		 return teamService.teamOfTeamMember();
	}
	@GetMapping("/getTeamSpecefic/{id}")
	public  TeamDto getTeamSpecefic(@PathVariable long id) {
		 return teamService.getTeamSpecefic(id);
	}
	
	
	@GetMapping("/taskDoneTeamMeber")
	public  List<CountDto> taskDoneTeamMeber() {
		 return teamService.taskDoneTeamMeber();
	}
	

}
