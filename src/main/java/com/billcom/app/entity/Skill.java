package com.billcom.app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Skill {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String roleLabel;
	private int eval;
	
	public Skill() {
		
	}
	public Skill(long id, String roleLabel, int value) {
		this.id = id;
		this.roleLabel = roleLabel;
		this.eval = value;
	}
	
	
	public Skill(String roleLabel, int eval) {
		super();
		this.roleLabel = roleLabel;
		this.eval = eval;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRoleLabel() {
		return roleLabel;
	}
	public void setRoleLabel(String roleLabel) {
		this.roleLabel = roleLabel;
	}
	public int getEval() {
		return eval;
	}
	public void setEval(int eval) {
		this.eval = eval;
	}
	
	
}
