package com.billcom.app.security;

import java.util.ArrayList;




import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.billcom.app.entity.UserApp;
import com.billcom.app.repository.UserRepository;



/*
 * Cette classe va Redéfinir la fonction  loadUserByUsername de l'interface 
 * UserDetailsService pour chercher si user existe ou non dans la base 
 * pour s'authentifier
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;

	@Autowired
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository =userRepository;
	}

	/*
	 * Redéfinition de la methode loaduserbyusername qui va implementer comment on
	 * cherche un user par son email et les fonctionnalités autorisés
	 */

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		if (! userRepository.existsByEmail(email))
			throw new UsernameNotFoundException("l'utilisateur n'existe pas");
		UserApp user = userRepository.findUserByEmail(email).get();
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		user.getRoles().forEach(r -> 
			authorities.add(new SimpleGrantedAuthority(r.getName()))
		);

		if (user.isActive() & user.isPendingFirstLogin())
			return User.withUsername(user.getEmail()).password(user.getPassword()).authorities(authorities)
					.disabled(false).build();
		else
			return User.withUsername(user.getEmail()).password(user.getPassword()).authorities(authorities)
					.disabled(true).build();

	}

}
