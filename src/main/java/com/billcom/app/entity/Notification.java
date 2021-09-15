package com.billcom.app.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.billcom.app.enumeration.NotificationType;


@Entity

public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String message;
	private String subject;
	private long idCreatedBy;
	private LocalDateTime createdAt;
	private boolean  seen;
	private NotificationType notificationType;
	
	public Notification() {
		notificationType = 	NotificationType.notification;
	    this.seen = false;	
	}
	

	public Notification(String message, String subject, long idCreatedBy, LocalDateTime createdAt) {
		this();
		this.message = message;
		this.subject = subject;
		this.idCreatedBy = idCreatedBy;
		this.createdAt = createdAt;
	}


	public Notification(String message, String subject, LocalDateTime createdAt,
			NotificationType notificationType) {
		this();
		this.message = message;
		this.subject = subject;
		this.createdAt = createdAt;
		this.notificationType = notificationType;
	}

	
	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	public void setIdCreatedBy(long idCreatedBy) {
		this.idCreatedBy = idCreatedBy;
	}


	public long getIdCreatedBy() {
		return idCreatedBy;
	}




	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public boolean isSeen() {
		return seen;
	}
	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}



	

	
}
