package com.billcom.app.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.billcom.app.entity.TeamMember;

public class TeamDto {
	private String teamName;
	private Set<Long> idmembers;
	private long idLeader;
    private LocalDateTime startedDate;
    private LocalDateTime dueDate;
	private TeamMember teamMember;
    private long id;
    private long teamMemberid;
    private String leader;
    private long userId;
    


    
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setIdLeader(long idLeader) {
		this.idLeader = idLeader;
	}

	public TeamDto() {
	}

	public TeamDto(String teamName, Set<Long> idmembers, long idLeader) {
		this.teamName = teamName;
		this.idmembers = idmembers;
		this.idLeader = idLeader;
	}


	
	
	
	
	public TeamDto(String teamName, long idLeader, LocalDateTime startedDate, LocalDateTime dueDate, long id,
			long teamMemberid,String leader,TeamMember teamMember) {
		this.teamName = teamName;
		this.idLeader = idLeader;
		this.startedDate = startedDate;
		this.dueDate = dueDate;
		this.id = id;
		this.teamMemberid = teamMemberid;
		this.leader = leader;
	    this.teamMember = teamMember;	
	}


	public TeamDto(String teamName, long idLeader, LocalDateTime startedDate, LocalDateTime dueDate, 
			String leader) {
		this.teamName = teamName;
		this.idLeader = idLeader;
		this.startedDate = startedDate;
		this.dueDate = dueDate;
		this.leader = leader;	}
	public TeamDto(String teamName, long idLeader, TeamMember teamMember,long id, LocalDateTime dueDate,LocalDateTime startedDate) {

		this.teamName = teamName;
		this.idLeader = idLeader;
		this.teamMember = teamMember;
		this.id = id;
		this.dueDate = dueDate;
		this.startedDate = startedDate;

	}

	public TeamDto(String teamName, long idLeader, TeamMember teamMember,long id) {

		this.teamName = teamName;
		this.idLeader = idLeader;
		this.teamMember = teamMember;
		this.id = id;
	}

	public TeamMember getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(TeamMember teamMember) {
		this.teamMember = teamMember;
	}



	public Set<Long> getIdmembers() {
		return idmembers;
	}

	public void setIdmembers(Set<Long> idmembers) {
		this.idmembers = idmembers;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	

	public long getIdLeader() {
		return idLeader;
	}

	public LocalDateTime getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(LocalDateTime startedDate) {
		this.startedDate = startedDate;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public long getTeamMemberid() {
		return teamMemberid;
	}

	public void setTeamMemberid(long teamMemberid) {
		this.teamMemberid = teamMemberid;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	

}
