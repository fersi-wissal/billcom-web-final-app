package com.billcom.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class WorkMember {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    private double  nbHour;
    private LocalDateTime dateHour;
    @OneToOne
    private Team team;
    @OneToOne

    private UserApp user;
	    
    public WorkMember() {
	}
	public WorkMember(double nbHour, LocalDateTime dateHour, Team team, UserApp user) {
		this.nbHour = nbHour;
		this.dateHour = dateHour;
		this.team = team;
		this.user = user;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public double getNbHour() {
		return nbHour;
	}
	public void setNbHour(double nbHour) {
		this.nbHour = nbHour;
	}
	public LocalDateTime getDateHour() {
		return dateHour;
	}
	public void setDateHour(LocalDateTime dateHour) {
		this.dateHour = dateHour;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public UserApp getUser() {
		return user;
	}
	public void setUser(UserApp user) {
		this.user = user;
	}
    
    
	
	
	
	
	
}
