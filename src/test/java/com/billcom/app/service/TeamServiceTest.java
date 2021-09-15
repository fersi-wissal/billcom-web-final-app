package com.billcom.app.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import com.billcom.app.dto.project.ProjectDto;
import com.billcom.app.entity.Project;
import com.billcom.app.entity.Role;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.UserApp;
import com.billcom.app.exception.ForbiddenException;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.security.SecurityUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("Team Service Test")
public class TeamServiceTest {
	
	@InjectMocks
	TeamService teamService;
	@Mock
	Team team;
	@Mock
	UserService userService;
	@Mock
	TeamRepository teamRepository;
	@Mock
	SecurityUtils securityUtils;

	@Test
	void should_return_user_logged() {
		// given
		UserApp user = new UserApp();
		given(teamService.getCurrentUser()).willReturn(user);
		// when & then
		Assertions.assertThat(teamService.getCurrentUser()).isNotNull();
		Assertions.assertThat(teamService.getCurrentUser().getEmail()).isEqualTo(user.getEmail());
	}
	@Test
	void should_throw_Exception_when_User_Forbidden() {
		
		Assertions.assertThatThrownBy(() -> {
			teamService.getTeam(999l);
		}).isInstanceOf(ForbiddenException.class);
	}
	
	@Test
	void should_throw_Exception_when_get_Team_list_Forbidden() {
		UserApp user = new UserApp(1L, "firstName", "lastName", true);
		Set<Role> roleList = new HashSet<>();
		roleList.add(new Role(6, "manager"));
		user.setRoles(roleList);
	    Mockito.doReturn(user).when(securityUtils).getLoggedUser();

		teamService.getAllTeamList();

	
	}
	
	
	
	
	
	
	
	
	

}
