package com.billcom.app.dto.project;

import java.util.Set;

public class ProjectTeamDto {
	private String teamName;
	private Set<Long> idmembers;
	private long idLeader;
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public Set<Long> getIdmembers() {
		return idmembers;
	}
	public void setIdmembers(Set<Long> idmembers) {
		this.idmembers = idmembers;
	}
	public long getIdLeader() {
		return idLeader;
	}
	public void setIdLeader(long idLeader) {
		this.idLeader = idLeader;
	}
	
	
	
}
