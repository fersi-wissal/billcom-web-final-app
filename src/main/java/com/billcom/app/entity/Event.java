package com.billcom.app.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Eventtable")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	
	private String  title;
	private LocalDateTime start;
	private LocalDateTime endDate;

	
	
	public Event() {
	}


	public Event(String title, LocalDateTime start, LocalDateTime endDate) {
		this.title = title;
		this.start = start;
		this.endDate = endDate;
	}

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public LocalDateTime getStart() {
		return start;
	}



	public void setStart(LocalDateTime start) {
		this.start = start;
	}



	public LocalDateTime getEnd() {
		return endDate;
	}



	public void setEnd(LocalDateTime end) {
		this.endDate = end;
	}



	

	
	
}
