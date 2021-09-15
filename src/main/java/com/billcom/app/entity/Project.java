package com.billcom.app.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.web.socket.TextMessage;

import com.billcom.app.enumeration.TeamProjectStatus;


@Entity

public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private int charge;
	private String description;
	private LocalDateTime endDate;
	private LocalDateTime startedDate;
	private LocalDateTime createdDate;

	private LocalDateTime effectiveDeleveryDate;

	private String version;

	private TeamProjectStatus statusProject;

	@OneToOne
	private UserApp projectLeader;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })

	private Set<Team> teamList;

	@ElementCollection
	private List<String> files;
	
	public Project() {
		this.statusProject = TeamProjectStatus.PENDING;
		this.version = "v1.0.0";
	    this.createdDate = LocalDateTime.now();	
	}

	public Project(String name, String description, LocalDateTime endDate, LocalDateTime startedDate) {
		this();
		this.name = name;
		this.description = description;
		this.endDate = endDate;
		this.startedDate = startedDate;

	}

	
	
	public Project(long id , String name, TeamProjectStatus statusProject) {
		this.id = id;
		this.name = name;
		this.statusProject = statusProject;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public LocalDateTime getStartedDate() {
		return startedDate;
	}

	public void setStartedDate(LocalDateTime startedDate) {
		this.startedDate = startedDate;
	}

	public UserApp getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(UserApp projectLeader) {
		this.projectLeader = projectLeader;
	}

	public Set<Team> getTeamList() {
		return teamList;
	}

	public void setTeamList(Set<Team> teamList) {
		this.teamList = teamList;
	}

	public TeamProjectStatus getStatusProject() {
		return statusProject;
	}

	public void setStatusProject(TeamProjectStatus statusProject) {
		this.statusProject = statusProject;
	}

	public List<String> getFiles() {
		return files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", description=" + description + ", endDate=" + endDate
				+ ", startedDate=" + startedDate + ", statusProject=" + statusProject + ", projectLeader="
				+ projectLeader + ", teamList=" + teamList + ", files=" + files + "]";
	}

	public LocalDateTime getEffectiveDeleveryDate() {
		return effectiveDeleveryDate;
	}

	public void setEffectiveDeleveryDate(LocalDateTime effectiveDeleveryDate) {
		this.effectiveDeleveryDate = effectiveDeleveryDate;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	

	


}
