package com.billcom.app.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


public class TeamMemberTest {
 
	private TeamMember teamMember;
	
	@Test
	void should_be_initialized_with_localDate_and_code_not_null() {
		// when
		teamMember = new TeamMember();

		// then
		Assertions.assertThat(teamMember.getDate()).isEqualTo(LocalDate.now());
		Assertions.assertThat(teamMember.getCodeMember()).isNotNull();

	}
}
