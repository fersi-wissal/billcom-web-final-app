package com.billcom.app.dto.count;

import java.util.List;

import com.billcom.app.entity.TeamMember;

public class TaskMemberDelay {

	private TeamMember teamMember;
	private List<TaskDateStatusDto> task;
	  
	
	public TaskMemberDelay(TeamMember teamMember, List<TaskDateStatusDto> task) {
		this.teamMember = teamMember;
		this.task = task;
	}
	public TaskMemberDelay(List<TaskDateStatusDto> taskDateStatusDto) {
            this.task = taskDateStatusDto;
	}
	public TeamMember getTeamMember() {
		return teamMember;
	}
	public void setTeamMember(TeamMember teamMember) {
		this.teamMember = teamMember;
	}
	public List<TaskDateStatusDto> getTask() {
		return task;
	}
	public void setTask(List<TaskDateStatusDto> task) {
		this.task = task;
	}
	
}
