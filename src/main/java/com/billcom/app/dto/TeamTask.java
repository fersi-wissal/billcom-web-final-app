package com.billcom.app.dto;

import com.billcom.app.entity.Task;
import com.billcom.app.entity.Team;

public class TeamTask {
	
	private Task task;
	private Team team;
	
	
	
	public TeamTask(Task task, Team team) {
		this.task = task;
		this.team = team;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	

}
