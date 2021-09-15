package com.billcom.app.service;

import java.time.LocalDateTime;


import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.billcom.app.entity.Event;
import com.billcom.app.entity.Notification;
import com.billcom.app.entity.Task;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.entity.UserApp;
import com.billcom.app.enumeration.NotificationType;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.NotificationRepository;
import com.billcom.app.repository.TaskRepository;
import com.billcom.app.repository.TeamMemberRepository;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

@Service
public class NotificationService {

	private NotificationRepository notificationRepository;
	private UserRepository userRepository;

	private TaskRepository taskRepository;
	private SecurityUtils securityUtils;
	private TeamMemberRepository teamMemberRepository;
	private TeamRepository teamRepository;

	public NotificationService(NotificationRepository notificationRepository, TeamRepository teamRepository,
			SecurityUtils securityUtils, TaskRepository taskRepository, TeamMemberRepository teamMemberRepository,UserRepository userRepository) {
		this.notificationRepository = notificationRepository;
		this.securityUtils = securityUtils;
		this.taskRepository = taskRepository;
		this.teamMemberRepository = teamMemberRepository;
		this.teamRepository = teamRepository;
		this.userRepository =userRepository;
	}

	public Set<Notification> getNotificationList() {
		return securityUtils.getLoggedUser().getNotifcation().stream().filter(
				notification -> notification.getNotificationType().compareTo(NotificationType.notification) == 0)
				.collect(Collectors.toSet());

	}

	public Set<Notification> getNotificationNotSeen() {

		return securityUtils.getLoggedUser().getNotifcation().stream().filter(
				notif -> !notif.isSeen() & notif.getNotificationType().compareTo(NotificationType.notification) == 0)
				.collect(Collectors.toSet());
	}

	public void updateNotificationStatus(long id, boolean status) {

		notificationRepository.findById(id).map(notif -> {
			notif.setSeen(status);
			return notificationRepository.save(notif);
		}).orElseThrow(() -> new NotFoundException("Notification Not Found"));
	}
	
	public void deleteNotification(long id) {
          UserApp user = securityUtils.getLoggedUser();
          user.getNotifcation().removeIf(notif -> notif.getId() == id);
		userRepository.save(user);
		notificationRepository.deleteById(id);
	}
	
	

	public void getNotifRemember(long id, boolean status) {

		notificationRepository.findById(id).map(notif -> {
			notif.setSeen(status);
			return notificationRepository.save(notif);
		}).orElseThrow(() -> new NotFoundException("Notification Not Found"));
	}

	public Set<Notification> getNotifRemember() {
		Set<Notification> notificationList = new HashSet<>();
		Set<TeamMember> teamMemberList = teamMemberRepository.findAll().stream()
				.filter(t -> t.getUser().getId() == securityUtils.getLoggedUser().getId()).collect(Collectors.toSet());
		for (TeamMember t : teamMemberList) {

			Set<Task> taskList = taskRepository.findAllByTeamMember(t).stream()
					.filter(task -> (ChronoUnit.DAYS.between(LocalDateTime.now(), task.getDeleveryDate()) == 0)
							& !task.getStatus().getStatusDescritpion().equalsIgnoreCase("DONE"))
					.collect(Collectors.toSet());
			long teamId = teamRepository.getJoin(t.getId());
			Set<Event> eventList = teamRepository.getOne(teamId).getEvents().stream()
					.filter(event -> (ChronoUnit.DAYS.between(LocalDateTime.now(), event.getStart()) == 0)
							& LocalDateTime.now().isBefore(event.getStart()))
					.collect(Collectors.toSet());

			for (Task task : taskList) {
				notificationList.add(new Notification(
						" you have a task " + task.getTaskName() + " to deliver today, assigned to team "
								+ teamRepository.getOne(teamId).getTeamName(),
						"Task to deliver", task.getDeleveryDate(), NotificationType.reminder));
				securityUtils.getLoggedUser().setNotifcation(notificationList);
			}
			for (Event event : eventList) {
				notificationList.add(new Notification(
						" you have an event " + event.getTitle() + " today assigned to "
								+ teamRepository.getOne(teamId).getTeamName(),
						"Meeting", event.getStart(), NotificationType.reminder));
				securityUtils.getLoggedUser().setNotifcation(notificationList);
			}

		}
		return notificationList;

	}

}
