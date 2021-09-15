package com.billcom.app.controller;

import java.time.Month;
import java.util.List;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billcom.app.dto.count.CountDto;
import com.billcom.app.dto.count.CountTaskTeamMemberDto;
import com.billcom.app.dto.count.TaskMemberDelay;
import com.billcom.app.dto.count.TeamKpi;
import com.billcom.app.dto.count.DetailKPI;
import com.billcom.app.service.TeamDashboardService;

@RestController
@RequestMapping(value="test/")

public class DashboardController {

	private TeamDashboardService dashboardService;
	
	public DashboardController(TeamDashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("countTaskTeamMemberDto/{idTeam}")
	public List <CountTaskTeamMemberDto> countTaskTeamMemberDto(@PathVariable long idTeam){
		return dashboardService.countTaskTeamMemberDto(idTeam);
}
	@GetMapping("/countTaskDelayed/{idTeam}")
	public Set<TaskMemberDelay> countTaskDelayed(@PathVariable Long idTeam){
		return dashboardService.countTaskDelayed(idTeam);
}
	@GetMapping("countKpiTeam/{idTeam}")
	public List<CountDto> countKpi(@PathVariable Long idTeam){
		return dashboardService.countKpiTeam(idTeam);
}
	@GetMapping("/countDelayReate/{id}")
	public CountDto countDelayReate(@PathVariable long id){
		return dashboardService.countDelayReate(id);
}
	@GetMapping("/countKPIweek/{id}")
	public List<DetailKPI> countKPIweek(@PathVariable long id){
		return dashboardService.countKPIweek(id);
}
	@GetMapping("/countKPIMonth/{id}")
	public List<DetailKPI> monthStatistic(@PathVariable long id,@RequestParam Month month,@RequestParam  int year){
		return dashboardService.MonthStatistic(id,month,year);
}
	@GetMapping("/KpiTeamLeader")
	public List<TeamKpi> teamKpiLeader(){
		return dashboardService.listTeamKpi();
}

}