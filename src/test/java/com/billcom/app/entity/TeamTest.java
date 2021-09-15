package com.billcom.app.entity;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


 class TeamTest {
	private Team team;

	@Test
	void should_be_initialized_with_localDate_and_pending_status() {
		// when
		team = new Team();

		// then
		//Assertions.assertThat(team.getDuetDate()).isEqualTo(LocalDateTime.now());

	}

	@Test
	void should_be_initialized_correctly() {
		Set<TeamMember> teamMember = new HashSet<>();
		TeamLeader leader = new TeamLeader();
		// when

	     team = new Team("teamName" );
		// then
		Assertions.assertThat(team.getTeamName()).isEqualTo("teamName");
		
	}
}
