package com.billcom.app.entity;



import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TeamLeader {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String codeLeader;
	@OneToOne
	private UserApp user;

	public TeamLeader() {
		this.codeLeader = UUID.randomUUID().toString();
	}

	public TeamLeader(UserApp user) {
		this();
		this.user = user;
	}

	
	public TeamLeader(long id, String codeLeader, UserApp user) {
		this.id = id;
		this.codeLeader = codeLeader;
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodeLeader() {
		return codeLeader;
	}

	public void setCodeLeader(String codeLeader) {
		this.codeLeader = codeLeader;
	}

	public UserApp getUser() {
		return user;
	}

	public void setUser(UserApp user) {
		this.user = user;
	}

}
