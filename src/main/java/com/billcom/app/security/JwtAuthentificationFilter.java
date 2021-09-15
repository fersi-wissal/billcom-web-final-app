package com.billcom.app.security;

import java.io.IOException;




import java.util.Date;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.billcom.app.entity.UserApp;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtAuthentificationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;

	@Autowired
	public JwtAuthentificationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/*
	 * cette methode est avant l'authentification,permet de charger l'utilisateur
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		UserApp user = null;
		try {
			user = new ObjectMapper().readValue(request.getInputStream(), UserApp.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
		return authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

	}
	/*
	 * Dans cette methode on génére le token
	 */

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {

// 1 . Recupération de l'objet user à partir du résultat authentification result 
		User springUser = (User) authResult
				.getPrincipal();
		String jwtToken = Jwts.builder().setSubject(springUser.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 24))
				.signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET)
				.claim("roles", springUser.getAuthorities()).compact();
		response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + jwtToken);
	}

}
