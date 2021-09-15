package com.billcom.app.dto.project;

import java.time.LocalDateTime;
import com.billcom.app.entity.Project;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;


public class ProjectDto {
	
	private String name;
	private String description;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime endDate;
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime startedDate;
	private int charge;
	
	private String projectLeader;
	private long idProjectLeader;
    
	

	public ProjectDto() {
	}


	public ProjectDto(String name, String description, LocalDateTime startedDate, LocalDateTime endDate,
			long idProjectLeader) {
		this.name = name;
		this.description = description;
		this.endDate = endDate;
		this.startedDate = startedDate;
		this.idProjectLeader = idProjectLeader;
	}





	public int getCharge() {
		return charge;
	}


	public void setCharge(int charge) {
		this.charge = charge;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public LocalDateTime getStartedDate() {
		return startedDate;
	}


	public void setStartedDate(LocalDateTime startedDate) {
		this.startedDate = startedDate;
	}


	public LocalDateTime getEndDate() {
		return endDate;
	}


	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}


	public long getIdProjectLeader() {
		return idProjectLeader;
	}


	public void setIdProjectLeader(long idProjectLeader) {
		this.idProjectLeader = idProjectLeader;
	}


	public Project fromDtoToProject() {
		return new Project( name, description, endDate, startedDate);
	}


	public String getProjectLeader() {
		return projectLeader;
	}


	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}


	public ProjectDto(String name, String description, LocalDateTime endDate, LocalDateTime startedDate,
			String projectLeader, long idProjectLeader) {
		this.name = name;
		this.description = description;
		this.endDate = endDate;
		this.startedDate = startedDate;
		this.projectLeader = projectLeader;
		this.idProjectLeader = idProjectLeader;
	}




	
}
