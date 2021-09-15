package com.billcom.app.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.billcom.app.enumeration.TaskPriority;


 class TaskTest {
 
	private Task task;
	
	@Test
	void should_initialise_correctly() {
 
	//when
		task = new Task();
	    LocalDateTime time = LocalDateTime.now();

		// then
		Assertions.assertThat(task.getTaskPriority()).isEqualTo(TaskPriority.lowPriority);
	}

}
