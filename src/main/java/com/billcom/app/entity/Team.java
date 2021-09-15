package com.billcom.app.entity;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

@Entity(name = "team")
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String teamName;
	private LocalDateTime startDate;
	private LocalDateTime dueDate;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "team_id", referencedColumnName = "id")
	private Set<TeamMember> teamMember;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "team_id", referencedColumnName = "id")

	private Set<Event> events;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "team_id", referencedColumnName = "id")

	private Set<Chat> chatList;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private TeamLeader leader;

	public Team() {

	}

	public Team(String teamName) {
		this.teamName = teamName;

	}
	

	public Team(Long id,String teamName) {
       this.id = id;
		this.teamName = teamName;
	}

	public Team(long id, String teamName, Set<TeamMember> teamMember, TeamLeader leader) {
		this.id = id;
		this.teamName = teamName;
		this.teamMember = teamMember;
		this.leader = leader;
	}

	public Team(String teamName, Set<TeamMember> teamMember, TeamLeader leader, LocalDateTime startedDated,
			LocalDateTime dueDated) {
		this.teamName = teamName;
		this.startDate = startedDated;
		this.dueDate = dueDated;
		this.teamMember = teamMember;
		this.leader = leader;
	}

	public Team(long id, String teamName) {
		this();
		this.id = id;
		this.teamName = teamName;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Set<TeamMember> getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(Set<TeamMember> teamMember) {
		this.teamMember = teamMember;
	}

	public TeamLeader getLeader() {
		return leader;
	}

	public void setLeader(TeamLeader leader) {
		this.leader = leader;
	}



	public LocalDateTime getDuetDate() {
		return dueDate;
	}

	public void setDuetDate(LocalDateTime duetDate) {
		this.dueDate = duetDate;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}
	

	

	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public Set<Chat> getChatList() {
		return chatList;
	}

	public void setChatList(Set<Chat> chatList) {
		this.chatList = chatList;
	}

	@Override
	public String toString() {
		return "Team [id=" + id + ", teamName=" + teamName + ", startDate=" + startDate + ", teamMember=" + teamMember
				+ ", leader=" + leader + "]";
	}

}
