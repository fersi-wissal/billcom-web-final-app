package com.billcom.app;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.billcom.app.config.DatabaseConfig;
import com.billcom.app.entity.Role;
import com.billcom.app.entity.Status;
import com.billcom.app.repository.RoleRepository;
import com.billcom.app.repository.StateRepository;
import com.billcom.app.service.ProjectService;
import com.billcom.app.service.TaskService;
import com.billcom.app.service.UserService;

import net.bytebuddy.asm.Advice.This;

@SpringBootApplication

public class BillcomWebFinalApplication {

	@Autowired
	StateRepository stateRepository;

	/*
	 * public void run(String... args) throws Exception {
	 * 
	 * 
	 * roleRepository.save(new Role(1,"manager")); roleRepository.save(new
	 * Role(2,"project leader")); roleRepository.save(new Role(3,"leader"));
	 * roleRepository.save(new Role(4,"member"));
	 * 
	 * 
	 * }
	 */

	public static void main(String[] args) {
		new File(ProjectService.UPLOADdIRECTORY).mkdir();
		new File(UserService.uploadDirectory).mkdir();
		new File(UserService.USERDIRECTORY).mkdir();
		new File(TaskService.TASKDIRECTORY).mkdir();

		SpringApplication.run(BillcomWebFinalApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(4);

	}

	@Bean
	public void addStatus() {
	  if(stateRepository.findAll().size() == 0 ) {
		stateRepository.save(new Status("to-do"));
		stateRepository.save(new Status("doing"));
		stateRepository.save(new Status("test"));
		stateRepository.save(new Status("done"));

	  }
	}
}
