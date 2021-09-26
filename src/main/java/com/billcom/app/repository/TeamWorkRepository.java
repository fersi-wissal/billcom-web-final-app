package com.billcom.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billcom.app.entity.WorkMember;

@Repository
public interface TeamWorkRepository   extends JpaRepository<WorkMember, Long> {
}



