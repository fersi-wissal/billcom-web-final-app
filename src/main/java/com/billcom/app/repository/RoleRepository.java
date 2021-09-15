package com.billcom.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.billcom.app.entity.Role;

@Repository
public interface RoleRepository  extends JpaRepository<Role, Long>{

	boolean existsByName(String r);

	Role findByName(String r);


}
