package com.billcom.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WorkDto {
	private double nbHour;

	private LocalDateTime date;
	private String teamName;
	private LocalDateTime startedteam;	
	private LocalDateTime endteam;
	private long idTeam;
	
	public WorkDto(double nbHour, LocalDateTime date, String teamName,LocalDateTime startedteam,
 LocalDateTime endteam) {
		this.nbHour = nbHour;
		this.date = date;
		this.teamName = teamName;
		this.startedteam = startedteam;
		this.endteam = endteam;

	}
	public WorkDto(double nbHour, LocalDateTime date, String teamName,LocalDateTime startedteam,
			 LocalDateTime endteam, long idTeam) {
					this.nbHour = nbHour;
					this.date = date;
					this.teamName = teamName;
					this.startedteam = startedteam;
					this.endteam = endteam;
					this.idTeam = idTeam;
					
					

				}





	public long getIdTeam() {
		return idTeam;
	}
	public void setIdTeam(long idTeam) {
		this.idTeam = idTeam;
	}
	public double getNbHour() {
		return nbHour;
	}





	public void setNbHour(double nbHour) {
		this.nbHour = nbHour;
	}





	public LocalDateTime getDate() {
		return date;
	}





	public void setDate(LocalDateTime date) {
		this.date = date;
	}





	public String getTeamName() {
		return teamName;
	}





	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}





	public LocalDateTime getStartedteam() {
		return startedteam;
	}





	public void setStartedteam(LocalDateTime startedteam) {
		this.startedteam = startedteam;
	}





	public LocalDateTime getEndteam() {
		return endteam;
	}





	public void setEndteam(LocalDateTime endteam) {
		this.endteam = endteam;
	}





	@Override
	public String toString() {
		return "WorkDto [nbHour=" + nbHour + ", date=" + date + ", teamName=" + teamName + "]";
	}

	
}
