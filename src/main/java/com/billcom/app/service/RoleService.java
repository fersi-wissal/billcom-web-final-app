package com.billcom.app.service;


import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.billcom.app.entity.Role;
import com.billcom.app.entity.UserApp;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.RoleRepository;
import com.billcom.app.repository.UserRepository;

@Service
public class RoleService {
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	
	public RoleService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	public Role saveRole(Role role){
		 return roleRepository.save(role);
		
	}
	public Set<Role> getListUser(long id){
		UserApp user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));
		return user.getRoles();
		
	}
	/** Add Role To Users*/
	public void updateAccesRoleUsers(Long id, List<String> role) {

		UserApp user = userRepository.findById(id).orElseThrow(()-> new NotFoundException("User Not Found"));
		role.stream().forEach(r -> {
			if (roleRepository.existsByName(r)) {
				Role newRole = roleRepository.findByName(r);
				user.getRoles().add(newRole);
				userRepository.save(user);
			}});
	}

	/** delete User role* */
	public void deleteUserRole(Long id, String role) {
		UserApp user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
		user.getRoles().removeIf(r -> r.getName().equals(role));
		userRepository.save(user);		
	}


}