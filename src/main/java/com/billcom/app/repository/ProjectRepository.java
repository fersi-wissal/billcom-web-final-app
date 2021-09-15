package com.billcom.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.billcom.app.entity.Project;

public interface ProjectRepository  extends JpaRepository<Project, Long>{

}
