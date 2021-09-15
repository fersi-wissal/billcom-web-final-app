package com.billcom.app.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/**
	 * Cette methode permet de définir comment il va chercher les roles des
	 * utilisateur
	 */

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	/**
	 * Cette methode permet de définir les methodes d'accés aux utilisateurs
	 */
    @Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		// on désactive la sécurité par session pour implémenter la sécurité par JWT
		// Session : c'est un systéme d'authentification par résference

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//http.authorizeRequests().antMatchers("/login/**","/**","/chat/**","/notif/**", "/sendEmail/**","/reset/**","/check/**", "/photoUser/**","/getphotoLeader/**").permitAll();
		http.authorizeRequests().antMatchers("/**","/login","/chat/**","/notif/**", "/sendEmail/**","/reset/**","/check/**", "/photoUser/**","/getphotoLeader/**").permitAll();

	//	http.authorizeRequests().antMatchers("/swagger-ui.html/**","/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/webjars/**","/swagger-resources/configuration/ui").permitAll();
		http.authorizeRequests().antMatchers("/swagger-ui.html","/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/webjars/**","/swagger-resources/configuration/ui").permitAll();

		http.authorizeRequests().antMatchers(HttpMethod.POST, "/user/create/**", "/user/add/**").permitAll();

		http.authorizeRequests().anyRequest().authenticated();
		http.addFilter(new JwtAuthentificationFilter(authenticationManager()));
		http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

	}

}
