package com.billcom.app.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Chattable")

public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private long idUser;
	private String userApp;
	private String messageUser;
	private LocalDateTime createdAt;

	public Chat() {
		this.createdAt = LocalDateTime.now();
	}
	
	public Chat(long idUser, String user, String message) {
	    this();
		this.idUser = idUser;
		this.userApp = user;
		this.messageUser = message;
	
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUser() {
		return userApp;
	}

	public void setUser(String user) {
		this.userApp = user;
	}

	public String getMessage() {
		return this.messageUser;
	}

	public void setMessage(String message) {
		this.messageUser = message;
	}

	public long getIdUser() {
		return idUser;
	}

	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
