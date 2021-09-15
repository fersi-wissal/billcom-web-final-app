package com.billcom.app.dto;


import java.time.LocalDateTime;

import java.util.List;

import com.billcom.app.entity.Task;


public class TaskDto {
	
	private String taskName;
	private LocalDateTime startedDate;
	private LocalDateTime deleveryDate;
	private String descriptionTask;
	private List<String> file;
	private long idMember;
	private String state;
	

	private String priority;
	

	public TaskDto( long id,String taskName) {
		this.taskName = taskName;
		this.idMember = id;
	}


	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public LocalDateTime getDeleveryDate() {
		return deleveryDate;
	}

	public void setDeleveryDate(LocalDateTime deleveryDate) {
		this.deleveryDate = deleveryDate;
	}

	public String getDescriptionTask() {
		return descriptionTask;
	}

	public void setDescriptionTask(String descriptionTask) {
		this.descriptionTask = descriptionTask;
	}

	public List<String> getFile() {
		return file;
	}

	public void setFile(List<String> file) {
		this.file = file;
	}

	public long getIdMember() {
		return idMember;
	}

	public void setI(long idMember) {
		this.idMember = idMember;
	}

	public String getState() {
		return state;
	}

	public void setState(String status) {
		this.state = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public Task fromDtoToTask () {
		
		return new Task(taskName,deleveryDate,descriptionTask,file);
	}

	
	public LocalDateTime getStartedDate() {
		return startedDate;
	}


	public void setStartedDate(LocalDateTime startedDate) {
		this.startedDate = startedDate;
	}


	@Override
	public String toString() {
		return "TaskDto [taskName=" + taskName + ", startedDate=" + startedDate + ", deleveryDate=" + deleveryDate
				+ ", descriptionTask=" + descriptionTask + ", file=" + file + ", idMember=" + idMember + ", state="
				+ state + ", priority=" + priority + "]";
	}


	
		

}
