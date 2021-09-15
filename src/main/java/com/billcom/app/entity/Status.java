package com.billcom.app.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.sun.istack.NotNull;



@Entity
public class Status {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@NotNull
	private String statusDescritpion;
	private LocalDate creationDate;

	public Status() {
		this.creationDate = LocalDate.now();
	}

	public Status(long id, String statusDescritpion) {
		this();
		this.id = id;
		this.statusDescritpion = statusDescritpion;
	}

	public Status(String status) {
		this();
		this.statusDescritpion = status;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStatusDescritpion() {
		return statusDescritpion;
	}

	public void setStatusDescritpion(String statusDescritpion) {
		this.statusDescritpion = statusDescritpion;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "Status [id=" + id + ", statusDescritpion=" + statusDescritpion + ", creationDate=" + creationDate + "]";
	}

}
