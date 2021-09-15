package com.billcom.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billcom.app.entity.TeamLeader;
import com.billcom.app.entity.UserApp;

public interface TeamLeaderRepository extends JpaRepository<TeamLeader, Long> {

	List<TeamLeader> findAllByUser(UserApp user);

	Optional<TeamLeader> findByUser(UserApp user);


}
