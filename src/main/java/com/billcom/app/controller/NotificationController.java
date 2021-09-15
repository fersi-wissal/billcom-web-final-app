package com.billcom.app.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.billcom.app.entity.Notification;
import com.billcom.app.service.NotificationService;

@RestController
@RequestMapping(value="notification")

public class NotificationController {
    @Autowired
	private NotificationService notificationService;
  
    @GetMapping("/NotificationList")
    public Set<Notification> getNotificationList(){
	
    return notificationService.getNotificationList() ;  
    
    }
    
    @GetMapping("/NotSeenNotificationList")
    public Set<Notification> getNotificationNotSeen(){
    	return notificationService.getNotificationNotSeen() ;   }
    
    @GetMapping("/getNotifRemember")
    public Set<Notification> getNotifRemember(){
    	return notificationService.getNotifRemember() ;  
    	
    }
    
    
    @PutMapping("/updateNotificationStatus/{id}")
    public void updateNotificationStatus(@PathVariable long id, @RequestBody boolean status){
    	 notificationService.updateNotificationStatus(id,status) ;   }
    
    @DeleteMapping("/deleteNotification/{id}")
    public void deleteNotification(@PathVariable long id){
    	;
    	 notificationService.deleteNotification(id) ;   }

    

}
