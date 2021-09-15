package com.billcom.app.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import com.billcom.app.entity.TeamMember;

public interface TeamMemberRepository  extends JpaRepository<TeamMember, Long>{

	
	Optional<TeamMember> getMemberById(long id);

}
