package com.billcom.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billcom.app.entity.Status;
import com.billcom.app.service.StateService;

@RestController
public class StateController {

      private StateService stateService;

	public StateController(StateService stateService) {
		this.stateService = stateService;
	}
   
	@GetMapping("task/findAllStatus")
	public List<Status> findAllStatus(){
		return stateService.findAllStatus();
	}
	
	@PostMapping("task/addStatus")
	public void addStatus(@RequestBody String status){
		 stateService.addStatus(status);
	}

}
