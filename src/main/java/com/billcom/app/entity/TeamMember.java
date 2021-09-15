package com.billcom.app.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity

public class TeamMember  implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String codeMember;
	private LocalDate date;
 
	@OneToOne
	private UserApp user;

	
	/**
	 * @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER,
	 *                    mappedBy="teamMember") List<Task>tasks;
	 */


	public TeamMember() {
		this.date = LocalDate.now();
		this.codeMember = UUID.randomUUID().toString();

	}

	public TeamMember(long id, UserApp user) {
		this();
		this.id = id;
		this.user = user;
	}

	public TeamMember(long id, String codeMember, LocalDate date, UserApp user) {
		this();
		this.id = id;
		this.codeMember = codeMember;
		this.date = date;
		this.user = user;
	}

	public TeamMember(UserApp user) {
		this();
		this.user = user;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCodeMember() {
		return codeMember;
	}

	public void setCodeMember(String codeMember) {
		this.codeMember = codeMember;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public UserApp getUser() {
		return user;
	}

	public void setUser(UserApp user) {
		this.user = user;
	}

}
