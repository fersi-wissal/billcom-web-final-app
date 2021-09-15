package com.billcom.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.billcom.app.enumeration.TaskPriority;
import com.billcom.app.enumeration.TeamProjectStatus;

public class ProjectTest {

	private Project project;
	
	@Test
	void should_be_initialized_with_localDate_and_pending_status() {
		//when
		project = new Project();
		//then
		Assertions.assertThat(project.getCreatedDate()).isBefore(LocalDateTime.now());
		Assertions.assertThat(project.getStatusProject()).isEqualTo(TeamProjectStatus.PENDING);
		Assertions.assertThat(project.getVersion()).isEqualTo("v1.0.0");

		
	}
	
	
	
	
}
