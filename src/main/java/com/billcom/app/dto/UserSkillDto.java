package com.billcom.app.dto;

import java.util.List;

import com.billcom.app.entity.UserApp;

public class UserSkillDto {
	
	private Long id;
	private String skill;
	private List<UserApp> userList;

	
	public UserSkillDto(Long id, String skill, List<UserApp> userList) {
		this.id = id;
		this.skill = skill;
		this.userList = userList;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public List<UserApp> getUserList() {
		return userList;
	}
	public void setUserList(List<UserApp> userList) {
		this.userList = userList;
	}
	
	
	
	
	

}
