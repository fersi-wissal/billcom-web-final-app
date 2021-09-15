package com.billcom.app.security;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.billcom.app.entity.Role;
import com.billcom.app.entity.UserApp;



@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Security Utils")

 class SecurityUtilsTest {

	@Mock
	SecurityUtils securityUtils;

	@Mock
	UserApp user;

	@BeforeEach
	void init() {
		given(securityUtils.getLoggedUser()).willReturn(user);
	}
	@Test
	void should_return_user_when_Userlogged() {
		// given
		UserApp user = new UserApp();
		given(securityUtils.getLoggedUser()).willReturn(user);
		// when & then
		Assertions.assertThat(securityUtils.getLoggedUser()).isNotNull();
	}

	@Test
	void checkUserRole_should_be_called() {
		UserApp user = new UserApp();
		when(securityUtils.checkUserRole(user)).thenReturn(true);
		 
		   boolean check =securityUtils.checkUserRole(user);
	     assertTrue(check);

	}
	
	@Test
	void should_throw_exception_when_user_not_logged_as_manager() {
		// given
		Role role = new Role(6, "manager");
		Set<Role> roleList = new HashSet<>();

		roleList.add(role);
		UserApp user = new UserApp("Fersi", "Wissal", "98635245", "fersi@yahoo.fr", "wissal1234", roleList, "Kairouan");		 

		// when & then
		when(securityUtils.checkifUserLoggedIsManager(user)).thenReturn(true);

		}
	
	
	
	
}
