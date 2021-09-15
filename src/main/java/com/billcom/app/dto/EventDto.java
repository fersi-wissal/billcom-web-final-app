package com.billcom.app.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import com.billcom.app.entity.Event;



public class EventDto {

	private String  title;


	private String start;

	private String end;

	
    private long id;
    
	
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






	public String getStart() {
		return start;
	}




	public void setStart(String start) {
		this.start = start;
	}




	public String getEnd() {
		return end;
	}




	public void setEnd(String end) {
		this.end = end;
	}




	public Event fromDtoToEvent() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		
    	return new Event(title,LocalDateTime.parse(start, formatter) ,LocalDateTime.parse(end, formatter));
    }




	@Override
	public String toString() {
		return "EventDto [title=" + title + ", start=" + start + ", end=" + end + ", id=" + id + "]";
	}





}
