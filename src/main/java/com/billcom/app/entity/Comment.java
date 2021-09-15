package com.billcom.app.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String content;
	private LocalDateTime createdDate;

	@OneToOne
	private UserApp user;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name ="comment_id",referencedColumnName = "id")
    private Set<CommentReplay> replay;
	
	public Comment() {
		this.createdDate = LocalDateTime.now();
	}

	public Comment(String content, UserApp user) {
		this();
		this.content = content;
		this.user = user;
	}

	public long getId() {
		return id;
	}
	

	public Comment(long id, String content, LocalDateTime createdDate, UserApp user, Set<CommentReplay> replay) {
		this();
		this.id = id;
		this.content = content;
		this.createdDate = createdDate;
		this.user = user;
		this.replay = replay;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserApp getUser() {
		return user;
	}

	public void setUser(UserApp user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", createdDate=" + createdDate + ", user=" + user + "]";
	}

	public Set<CommentReplay> getReplay() {
		return replay;
	}

	public void setReplay(Set<CommentReplay> replay) {
		this.replay = replay;
	}

}
