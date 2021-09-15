package com.billcom.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.billcom.app.entity.Status;
import com.billcom.app.repository.StateRepository;

@Service
public class StateService {

	private StateRepository stateRepository;

	public StateService(StateRepository stateRepository) {
		this.stateRepository = stateRepository;
	}
	public List<Status> findAllStatus(){
		return stateRepository.findAll();
		
	}
	public void addStatus(String status){

		if ( ! stateRepository.findByStatusDescritpion(status).isPresent()) {

		stateRepository.save( new Status(status));}
		
	}

}
