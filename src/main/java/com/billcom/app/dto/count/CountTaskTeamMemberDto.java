package com.billcom.app.dto.count;

import java.util.List;

import com.billcom.app.entity.TeamMember;

public class CountTaskTeamMemberDto {

	private TeamMember teamMember;
	private List<CountDto> countTaskDto;
	private List<TaskDateStatusDto> taskDateStatusDto;
	private List<TaskDateStatusDto> tasksAdvanced;


	public CountTaskTeamMemberDto(TeamMember teamMember, List<CountDto> countTaskDto,
			List<TaskDateStatusDto> taskDateStatusDto, List<TaskDateStatusDto> tasksAdvanced) {
		this.teamMember = teamMember;
		this.countTaskDto = countTaskDto;
		this.taskDateStatusDto = taskDateStatusDto;
		this.tasksAdvanced = tasksAdvanced;
	}
	public List<TaskDateStatusDto> getTaskDateStatusDto() {
		return taskDateStatusDto;
	}
	public void setTaskDateStatusDto(List<TaskDateStatusDto> taskDateStatusDto) {
		this.taskDateStatusDto = taskDateStatusDto;
	}
	public TeamMember getTeamMember() {
		return teamMember;
	}
	public void setTeamMember(TeamMember teamMember) {
		this.teamMember = teamMember;
	}
	public List<CountDto> getCountTaskDto() {
		return countTaskDto;
	}
	public void setCountTaskDto(List<CountDto> countTaskDto) {
		this.countTaskDto = countTaskDto;
	}
	public List<TaskDateStatusDto> getTasksAdvanced() {
		return tasksAdvanced;
	}
	public void setTasksAdvanced(List<TaskDateStatusDto> tasksAdvanced) {
		this.tasksAdvanced = tasksAdvanced;
	}
	
}
