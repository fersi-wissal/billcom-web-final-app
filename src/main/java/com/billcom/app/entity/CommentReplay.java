package com.billcom.app.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class CommentReplay {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String replayContent;
	private LocalDateTime createdDate;

	@ManyToOne
	private UserApp fromUser; 
	
	@OneToOne 
	private UserApp toUser;

	

	public CommentReplay() {
           this.createdDate= LocalDateTime.now();
	}


	public CommentReplay(long id, String replayContent, UserApp fromUser, UserApp toUser) {
	 
	    this();	
		this.id = id;
		this.replayContent = replayContent;
		this.fromUser = fromUser;
		this.toUser = toUser;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getReplayContent() {
		return replayContent;
	}


	public void setReplayContent(String replayContent) {
		this.replayContent = replayContent;
	}


	public LocalDateTime getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}


	public UserApp getFromUser() {
		return fromUser;
	}


	public void setFromUser(UserApp fromUser) {
		this.fromUser = fromUser;
	}


	public UserApp getToUser() {
		return toUser;
	}


	public void setToUser(UserApp toUser) {
		this.toUser = toUser;
	}


		
}
