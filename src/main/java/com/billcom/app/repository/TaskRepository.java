package com.billcom.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.billcom.app.entity.Task;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.UserApp;

public interface TaskRepository  extends JpaRepository<Task, Long>{


	
	List<Task> findAllByTeamMember(TeamMember teamMember);

	List<Task> findAllById(long id);

	void deleteAllByTeamMember(TeamMember user);




}
