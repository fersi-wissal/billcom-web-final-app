package com.billcom.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billcom.app.entity.Event;

public interface EventRepository  extends JpaRepository<Event, Long>{

}
