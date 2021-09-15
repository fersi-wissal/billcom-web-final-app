package com.billcom.app.entity;


import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserAppTest {

	private UserApp userApp;

	@Test
	void should_be_initialized_with_true() {
		// when
		userApp = new UserApp();

		// then
		Assertions.assertThat(userApp.isActive()).isTrue();
	}

	@Test
	void should_be_initialized_correctly() {
		Set<Role> roleList = new HashSet<>();
		// when
		userApp = new UserApp("firstName", "lastName", "mobile", "email", "hashPassword", roleList, "adresse");
		// then
		Assertions.assertThat(userApp.getFirstName()).isEqualTo("firstName");
		Assertions.assertThat(userApp.getLastName()).isEqualTo("lastName");
		Assertions.assertThat(userApp.getMobile()).isNotNull();
		Assertions.assertThat(userApp.getEmail()).isNotNull();
		Assertions.assertThat(userApp.getPassword()).isEqualTo("hashPassword");
		Assertions.assertThat(userApp.getRoles()).isEqualTo(roleList);
		Assertions.assertThat(userApp.getAdresse()).isEqualTo("adresse");
	}
}