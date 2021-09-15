package com.billcom.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billcom.app.entity.Event;
import com.billcom.app.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

}
