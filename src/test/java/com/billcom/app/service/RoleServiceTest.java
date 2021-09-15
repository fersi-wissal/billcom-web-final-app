package com.billcom.app.service;

import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

import com.billcom.app.entity.Role;
import com.billcom.app.entity.UserApp;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.RoleRepository;
import com.billcom.app.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Role Service Test")
public class RoleServiceTest {
	
	@InjectMocks
	RoleService roleService;
	@Mock
	Role role;
	@Mock
	UserApp user;
	@Mock
	UserRepository userRepository;
	@Mock
	RoleRepository roleRepository;
	@Test
	void should_return_List_role_when_known_user() {
		// given
		final RoleService roleService = spy(this.roleService);
		Role role = new Role(6, "manager");
		Set<Role> roleList = new HashSet<>();

		roleList.add(role);
		UserApp user = new UserApp(1,"Fersi", "Wissal", true);
        user.setRoles(roleList);
	    Mockito.doReturn(Optional.of(user)).when(userRepository).findById(user.getId());

		// when
		roleService.getListUser(user.getId());
		
	}
	
	@Test
	void should_throw_exception_for_List_role_when_unknown_user() {
		// given

		Mockito.doReturn(Optional.empty()).when(userRepository).findById(999l);
		// when
		Assertions.assertThatThrownBy(() -> {
			roleService.getListUser(999l);
		}).isInstanceOf(NotFoundException.class);
		
	}
	
	@Test
	void should_update_List_role_when_known_user() {

	//given
	Role role = new Role(6, "member");
	Set<Role> roleList = new HashSet<>();

	roleList.add(role);

	UserApp user = new UserApp(1,"Fersi", "Wissal", true);
    user.setRoles(roleList);
    Mockito.doReturn(Optional.of(user)).when(userRepository).findById(user.getId());
	List<String> newRoleList = new ArrayList<>();
	newRoleList.add("manager");
	//when
    roleService.updateAccesRoleUsers(user.getId(), newRoleList);
    
	
	}
}
