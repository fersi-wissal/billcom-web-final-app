package com.billcom.app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billcom.app.dto.TeamDto;
import com.billcom.app.dto.WorkDtoList;
import com.billcom.app.entity.WorkMember;
import com.billcom.app.service.MemberWorkService;

@RestController
@RequestMapping(value = "teamWork")
public class MemberWorkController {
	private MemberWorkService memberWorkService;

	public MemberWorkController(MemberWorkService teamWorkService) {
		this.memberWorkService = teamWorkService;
	}

	@PostMapping("/addHourToTeam/{id}")
	public WorkMember addHourToTeam(@PathVariable long id, @RequestBody String hour) {
		return memberWorkService.addHourToTeam(id, Double.parseDouble(hour) );

	}

	@GetMapping("/workMemberList")
	public List<WorkDtoList> workMemberList() {

		return memberWorkService.workMemberList();

	}

	
	@GetMapping("/getworkMemberListCurrentDay")
	public List<WorkDtoList> workMemberListCurrentDay() {

		return memberWorkService.workMemberListCurrentDay();

	}
	
	
	
	
	
	@GetMapping("/totalNbDayHour")
	public double getNbHour() {

		return memberWorkService.getNbHour();

	}

	
	@GetMapping("/teamList")
	public List<TeamDto> teamOfTeamMember() {
		return memberWorkService.teamOfTeamMember();
	}

}
