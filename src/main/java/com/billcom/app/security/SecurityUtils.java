package com.billcom.app.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.billcom.app.entity.Team;
import com.billcom.app.entity.UserApp;

import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.UserRepository;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;

@Component
public class SecurityUtils {

	private UserRepository userRepository;

	private static final String ANONYMOUS = "anonymousUser";

	@Autowired
	public SecurityUtils(UserRepository userRepository) {
		this.userRepository = userRepository;

	}

	public boolean checkIfThereIsUserLogged() {

		return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals(ANONYMOUS);
	}

	public UserApp getLoggedUser() {

		if (AppUtils.isEmail(getCurrentUserLogin())) {
			return userRepository.findOneByEmail(getCurrentUserLogin());

		}
		return null;
	}

	public String getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		String userName = null;
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				userName = springSecurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				userName = (String) authentication.getPrincipal();
			}
		}
		return userName;
	}

	public boolean checkUserRole(UserApp user) {

		if ((user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("manager"))
				|| user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("leader"))))
		{return true;
			
		}else {
			throw new NotFoundException("User Does not have role manager or leader");
		}
	
	
	}

	
	
	public boolean checkIfUserLoggedIsManger(UserApp user) {

		if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("manager")))
		{return true;
			
		} return false;
	
	
	}
	
	
	
	
	public boolean checkIfUserLoggedIsProjectLeader(UserApp user) {

		if (user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("Project Leader")  ))
		{return true;
			
		}else {
			throw new NotFoundException("User Does not have project Leader");
		}
	
	
	}
	
	
	
	
	public boolean checkIfUserLoggedIsTeamLeader(UserApp user, Team team) {

		return user.getId() == team.getLeader().getUser().getId();
	}

	public boolean checkifUserLoggedIsManagerOrTeamMemberorTeamLeader(UserApp user, Team team) {
		return (user.getId() == team.getLeader().getUser().getId()
				||  team.getTeamMember().stream().anyMatch(t -> t.getUser().getId() == user.getId()))
				|| user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("manager"));

	}

	public boolean checkifUserLoggedIsManagerOrProjectLeaderorTeamLeader(UserApp user, Team team) {
		return (user.getId() == team.getLeader().getUser().getId()
				||  user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("Project Leader"))
				|| user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("manager")));

	}
	public boolean checkifUserLoggedIsManager(UserApp user) {
		if ((user.getRoles().stream().anyMatch(role -> role.getName().equalsIgnoreCase("manager")))) {
			return true;

	}else {
		throw new NotFoundException("User Does not have role manager");
	}


	}
}