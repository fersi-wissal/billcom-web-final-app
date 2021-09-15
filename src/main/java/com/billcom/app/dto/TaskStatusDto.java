package com.billcom.app.dto;

import java.util.List;

import com.billcom.app.entity.Task;

public class TaskStatusDto {
	private Long id;
	private String status;
	private List<Task> taskStatusDto;
	


	public List<Task> getTaskStatusDto() {
		return taskStatusDto;
	}

	public void setTaskStatusDto(List<Task> taskStatusDto) {
		this.taskStatusDto = taskStatusDto;
	}

	public TaskStatusDto(long id, String status,  List<Task> taskStatusDto) {
		this.id = id;
		this.status = status;
		this.taskStatusDto = taskStatusDto;
	}

	public TaskStatusDto(String status, List<Task> taskk) {
		
		this.status = status;
		this.taskStatusDto = taskk;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TaskStatusDto [id=" + id + ", status=" + status + ", taskStatusDto=" + taskStatusDto + "]";
	}
	
	

	
}
