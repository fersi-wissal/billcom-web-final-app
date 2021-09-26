package com.billcom.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class WorkDtoList {
	private LocalDateTime date;
	private List<WorkDto> listWork;
	
	public WorkDtoList(LocalDateTime localDateTime, List<WorkDto> listWork) {
		this.date = localDateTime;
		this.listWork = listWork;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public List<WorkDto> getListWork() {
		return listWork;
	}
	public void setListWork(List<WorkDto> listWork) {
		this.listWork = listWork;
	}
	@Override
	public String toString() {
		return "WorkDtoList [date=" + date + ", listWork=" + listWork + "]";
	}
	

}
