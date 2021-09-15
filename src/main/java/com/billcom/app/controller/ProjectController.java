package com.billcom.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.billcom.app.dto.TeamDto;
import com.billcom.app.dto.count.CountDto;
import com.billcom.app.dto.project.ProjectDto;
import com.billcom.app.dto.project.ProjectListDto;
import com.billcom.app.entity.Project;
import com.billcom.app.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/project")

public class ProjectController {
	private ProjectService projectService;

	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}

	@PostMapping("/saveProject")
	public void saveProject(@RequestParam String projectDto, @RequestParam("files") List<MultipartFile> multipartFiles)
			throws IOException {
		ProjectDto project = new ObjectMapper().readValue(projectDto, ProjectDto.class);
		projectService.saveProject(project, multipartFiles);
	}

	@GetMapping("/listProject")
	public Set<ProjectListDto> getListProject() {
		return projectService.getListProject();
	}
	@GetMapping("/listProjectNotCompleted")
	public Set<ProjectListDto> getListProjectNotCompleted() {
		return projectService.getListProjectNotCompleted();
	}
	@GetMapping("/listProjectLeaderNotCompleted")
	public Set<ProjectListDto> listProjectLeaderNotCompleted() {
		return projectService.getListProjectLeaderNotCompleted();
	}
	@PutMapping("/addTeamToProject/{idProject}")
	public Project saveTeam(@PathVariable long idProject, @RequestBody TeamDto team) {
		return projectService.addteamToProject(idProject, team);
	}

	@PutMapping("/updateProjectStatus/{idProject}")
	public void updateProjectStatus(@PathVariable long idProject, @RequestBody String projeectStatus) {
		projectService.updateStatusProject(idProject, projeectStatus);

	}

	@PutMapping("/deleteTeamFromProject/{idProject}")
	public void deleteTeamFromProject(@PathVariable long idProject, @RequestBody long idTeam) {
		projectService.deleteTeamFromProject(idProject, idTeam);

	}

	@GetMapping("getTaskprogress/{idProject}")

	public float getNbTaskProgsess(@PathVariable long idProject) {
		return projectService.getProgressType(idProject);

	}

	@GetMapping("getProjectDetail/{idProject}")
	public ProjectListDto getProjectDetail(@PathVariable long idProject) {
		return projectService.getProjectDeatil(idProject);

	}

	@GetMapping("/download/{filename}")
	public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {

		return projectService.downloadFiles(filename);
	}

	@GetMapping("/kpiGateProject")
	public List<CountDto> getProjectTimeGap() {

		return projectService.getProjectTimeGap();
	}

	@GetMapping("/kpiDurationProject")
	public List<CountDto> getProjectDurationGap() {

		return projectService.getProjectDurationGap();
	}

	@GetMapping("/kpiProjectDealyedDelevered")
	public List<CountDto> getProjectDealyedDelevered() {

		return projectService.getProjectDealyedDelevered();
	}
	
	@GetMapping("/kpiProjectLeaderDealyedDelevered")
	public List<CountDto> getProjectLeaderDealyedDelevered() {

		return projectService.getProjectLeaderDealyedDelevered();
	}
	
	@GetMapping("/kpiProjectStatistic/{id}")
	public List<CountDto> getProjectStatistic(@PathVariable long id) {

		return projectService.getProjectStatistic(id);
	}
	@PutMapping("/updateProject")
	public void updateProjectStatus(@RequestBody Project project) {
		projectService.updateProject(project);

	}

	@GetMapping("/listProjectProjectLeader")
	public Set<ProjectListDto> getListProjectProjectLeader() {
		return projectService.getListProjectProjectLeader();
	}
	
	@GetMapping("/chargeAssismentProjet/{id}")

	public List<CountDto> chargeAssismentProjet(@PathVariable long id){
	
	return projectService.chargeAssismentProjet(id);
	
	}
	@GetMapping("/kpiGateProjectLeader")
	public List<CountDto> getProjectsProjectLeaderTimeGap() {

		return projectService.getProjectsProjectLeaderTimeGap();
	}
	
	@GetMapping("/ProjectTeamDetail/{id}")
	public ProjectDto getProjectTeamDetail(@PathVariable long id) {

		return projectService.getProjectTeamDetail(id);
	}
	}
