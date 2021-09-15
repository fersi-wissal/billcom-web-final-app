package com.billcom.app.service;

import static org.assertj.core.api.Assertions.catchThrowable;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.billcom.app.dto.UserDto;
import com.billcom.app.entity.Role;
import com.billcom.app.entity.Skill;
import com.billcom.app.entity.UserApp;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Test")
class UserServiceTest {

	@InjectMocks
	UserService userService;
	@Mock
	UserApp user;
	@Mock
	Skill skill;
	@Mock
	UserDto userDto;
	@Mock
	SecurityUtils securityUtils;
	@Mock
	UserRepository userRepository;
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;

	/** Test get CurrentUser */
	@Test
	void should_return_user_when_user_is_logged() {
		// given
		UserApp user = new UserApp();
		given(userService.getCurrentUser()).willReturn(user);
		// when & then
		Assertions.assertThat(userService.getCurrentUser()).isNotNull();
		Assertions.assertThat(userService.getCurrentUser().getEmail()).isEqualTo(user.getEmail());
	}

	@Test
	void should_test_get_user_when_logged_user_is_manager() {
		// given
		final UserService userService = spy(this.userService);
		UserApp user = new UserApp(1L, "firstName", "lastName", true);
		given(userService.getCurrentUser()).willReturn(user);
		Mockito.doReturn(true).when(userService).checkifUserLoggedIsManager(user);
		// when
		Assertions.assertThatThrownBy(() -> {
			userService.getUser(1L);
		}).isInstanceOf(NotFoundException.class);

		// then
		Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
	}

	/** Test Return List Of Users **/
	@Test
	void should_return_all_users_when_logged_as_manager() {
		// given

		UserApp user = new UserApp();
		given(userService.getCurrentUser()).willReturn(user);
		final UserService userService = spy(this.userService);
		Mockito.doReturn(true).when(securityUtils).checkifUserLoggedIsManager(user);
		// when
		userService.getUsers();

		Mockito.verify(userRepository, Mockito.times(1)).findAll();
	}

	@Test
	void should_return_role_list_user() {
		// given

		Role role = new Role(6, "manager");
		Set<Role> roleList = new HashSet<>();

		roleList.add(role);

		UserApp user = new UserApp("Fersi", "Wissal", "98635245", "fersi@yahoo.fr", "wissal1234", roleList, "Kairouan");

		// when
		Set<Role> roleUser = userService.getRoleList(user);
		// then
		Assertions.assertThat(roleUser).isNotEmpty();

	}

	@Test
	void should_save_user() {
		// given
		Role role = new Role(6, "manager");
		Set<Role> roleList = new HashSet<>();

		roleList.add(role);
		UserApp user = new UserApp("Fersi", "Wissal", "98635245", "fersi@yahoo.fr", "wissal1234", roleList, "Kairouan");
		given(userService.getCurrentUser()).willReturn(user);
		final UserService userService = spy(this.userService);

		UserDto userDto = new UserDto();

		// when
		userService.addUser(userDto);


		// then
		Mockito.verify(userService, Mockito.times(1)).addUser(userDto);
	}

	@Test
	void should_throw_exception_when_user_not_Found() {
		// given

		Role role = new Role(6, "manager");
		Set<Role> roleList = new HashSet<>();

		roleList.add(role);
		UserApp user = new UserApp("Fersi", "Wissal", "98635245", "fersi@yahoo.fr", "wissal1234", roleList, "Kairouan");
		given(userService.getCurrentUser()).willReturn(user);
		final UserService userService = spy(this.userService);
		Mockito.doReturn(Optional.empty()).when(userRepository).findById(999l);
		// when
		Assertions.assertThatThrownBy(() -> {
			userService.getUser(999l);
		}).isInstanceOf(NotFoundException.class);
	}

	@Test
	void should_return__users_wth_role_as_project_leader() {
		// given

		final UserService userService = spy(this.userService);

		Role roleList = new Role(7, "project Leader");
		Set<Role> roles = new HashSet<>();

		roles.add(roleList);
		UserApp user1 = new UserApp("Fersi", "Wissal", "98635245", "fersi@yahoo.fr", "wissal1234", roles, "Kairouan");
		userRepository.save(user1);
		// when
		userService.getListUserWithRoleProjectLeader();

		Mockito.verify(userRepository, Mockito.times(1)).findAll();
	}

	@Test
	void should_return__users_wth_role_as_leader() {
		// given

		final UserService userService = spy(this.userService);

		Role roleList = new Role(7, "leader");
		Set<Role> roles = new HashSet<>();

		roles.add(roleList);
		UserApp user1 = new UserApp("Fersi", "Wissal", "98635245", "fersi@yahoo.fr", "wissal1234", roles, "Kairouan");
		userRepository.save(user1);
		// when
		userService.getListUserWithRoleLeader();

		Mockito.verify(userRepository, Mockito.times(1)).findAll();
	}

	@Test
	void should_return__users_wth_role_as_member() {
		// given

		final UserService userService = spy(this.userService);

		Role roleList = new Role(7, "member");
		Set<Role> roles = new HashSet<>();

		roles.add(roleList);
		UserApp user1 = new UserApp("Fersi", "Wissal", "98635245", "fersi@yahoo.fr", "wissal1234", roles, "Kairouan");
		userRepository.save(user1);
		// when
		userService.getListUserWithRoleMember();

		Mockito.verify(userRepository, Mockito.times(1)).findAll();
	}

	@Test
	void should_throw__exception_when_reset_token_empty() {
		// given
        Mockito.doReturn(Optional.empty()).when(userRepository).findUserByResetToken("abc");
		
     // when
     		Throwable thrown = catchThrowable(() -> {
     			userService.checkCode("abc");
     		});


    		Assertions.assertThat(thrown).isInstanceOf(NotFoundException.class)
    				.hasMessage("Reset Token is Empty");	}
	@Test
	void should_add__skill() {
		// given
		Skill skill = new Skill("Angular", 55);
		
		UserApp user = new UserApp(8, "firstName", "lastName", true);
		Set<Skill> skills = new HashSet<>();
		skills.add(new Skill("python",23));
		user.setSkills(skills);
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(user.getId());

		
        Throwable thrown = catchThrowable(() -> {
 			userService.adSkillsToUser(user.getId(),skill);
 		});


		Assertions.assertThat(thrown).isInstanceOf(NotFoundException.class)
				.hasMessage("User Not Found");	}
	
	}
	
	


