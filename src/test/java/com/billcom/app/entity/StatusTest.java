package com.billcom.app.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.billcom.app.enumeration.TaskPriority;

public class StatusTest {

	private Status status;
	
	@Test
	void should_initialise_correctly() {
		//when
		status = new Status();
		//then
	    LocalDateTime time = LocalDateTime.now();		
	
		Assertions.assertThat(status.getCreationDate()).isEqualTo(LocalDate.now());

	
	}
}
