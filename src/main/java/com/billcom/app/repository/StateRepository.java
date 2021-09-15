package com.billcom.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billcom.app.entity.Status;

public interface StateRepository extends JpaRepository<Status, Long> {

	Optional<Status> findByStatusDescritpion(String status);

}
