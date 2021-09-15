package com.billcom.app.dto.project;

import com.billcom.app.entity.Project;

public class ProjectListDto {

	private Project project;
	private float   progress;
	 

	
	public ProjectListDto(Project project, float progress) {
		this.project = project;
		this.progress = progress;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public float getProgress() {
		return progress;
	}
	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	
	
}
