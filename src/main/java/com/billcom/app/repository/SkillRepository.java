package com.billcom.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billcom.app.entity.Skill;
@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>{

}
