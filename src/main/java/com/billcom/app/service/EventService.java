package com.billcom.app.service;

import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.billcom.app.dto.EventDto;
import com.billcom.app.entity.Event;
import com.billcom.app.entity.Notification;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.TeamMember;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.EventRepository;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.repository.UserRepository;
import com.billcom.app.security.SecurityUtils;

@Service
public class EventService {
	private EventRepository eventRepository;
	private TeamRepository teamRepository;
	private JavaMailSender mailSender;
	private SecurityUtils securityUtils;
	private UserRepository userRepository;

	public EventService(EventRepository eventRepository, TeamRepository teamRepository, JavaMailSender mailSender,UserRepository userRepository
			,SecurityUtils securityUtils) {
		this.eventRepository = eventRepository;
		this.teamRepository = teamRepository;
		this.mailSender = mailSender;
		this.securityUtils = securityUtils;
		this.userRepository =userRepository;

	}

	public void sendEmailEvent(long id, String object, String title, String started, String ends) {

		Set<TeamMember> teamMemberList = teamRepository.findById(id).get().getTeamMember();
		String[] emailListArray = new String[teamMemberList.size()];
		int i = -1;
		for (TeamMember m : teamMemberList) {

			i = i + 1;
			emailListArray[i] = m.getUser().getEmail();
			Set<Notification> notificationList = m.getUser().getNotifcation();
			
			notificationList.add(new Notification(securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName() +" added an event assigned to team",teamRepository.findById(id).get().getTeamName(),securityUtils.getLoggedUser().getId() , LocalDateTime.now()));

			
			m.getUser().setNotifcation(notificationList);
			userRepository.save(m.getUser());
		}

		/*SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("apptestbillcom@gmail.com");
		message.setTo(emailListArray);
		message.setText("Good morning, You have an event assigned to team " + title + "," + "\n" + "assigned to team :"
				+ teamRepository.findById(id).get().getTeamName() + "," + "\n" + "Started At : " + started + "\n"
				+ "Ends At   :   " + ends + "\n" + " cordially");

		message.setSubject("[" + object + "]");

		mailSender.send(message);*/

	}

	public void addEvent(long id, EventDto eventDto) {
		teamRepository.findById(id).map(team -> {
			Set<Event> events = team.getEvents();
			events.add(eventDto.fromDtoToEvent());
			team.setEvents(events);

			return teamRepository.save(team);

		}).orElseThrow(() -> new NotFoundException("team Not Found"));
		sendEmailEvent(id, "New Event", eventDto.getTitle(), eventDto.getStart(), eventDto.getEnd());

	}

	public void updateEvent(long idTeam, EventDto eventDto) {

		Team team = teamRepository.findById(idTeam).get();

		team.getEvents().stream().filter(e -> e.getId() == eventDto.getId()).findAny().map(e -> {

			e.setTitle(eventDto.getTitle());
			e.setStart(LocalDateTime.parse(eventDto.getStart(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
			e.setEnd(LocalDateTime.parse(eventDto.getEnd(), DateTimeFormatter.ISO_OFFSET_DATE_TIME));
			eventRepository.save(e);
			return teamRepository.save(team);

		});
		
		Set<TeamMember> teamMemberList = team.getTeamMember();
		for (TeamMember m : teamMemberList) {
			Set<Notification> notificationList = m.getUser().getNotifcation();
			
			notificationList.add(new Notification(securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName() +" updated "+ eventDto.getTitle() + "assigned to team",team.getTeamName(),securityUtils.getLoggedUser().getId() , LocalDateTime.now()));

			
			m.getUser().setNotifcation(notificationList);
	         //	sendEmailEvent(idTeam, "Changed  Event", eventDto.getTitle(), eventDto.getStart(), eventDto.getEnd());

			userRepository.save(m.getUser());

		}}

	public void updateDetailEvent(long idTeam, EventDto eventDto) {

		Team team = teamRepository.findById(idTeam).get();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[yyyy-MM-dd'T'HH:mm:ss][yyyy-MM-dd'T'HH:mm]");

		team.getEvents().stream().filter(e -> e.getId() == eventDto.getId()).findAny().map(e -> {

			e.setTitle(eventDto.getTitle());

			e.setStart(LocalDateTime.parse(eventDto.getStart(), formatter));
			e.setEnd(LocalDateTime.parse(eventDto.getEnd(), formatter));
			eventRepository.save(e);
			return teamRepository.save(team);

		});
	
		Set<TeamMember> teamMemberList = team.getTeamMember();
		for (TeamMember m : teamMemberList) {
			Set<Notification> notificationList = m.getUser().getNotifcation();
			
			notificationList.add(new Notification(securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName() +" updated "+ eventDto.getTitle() + " assigned to team",team.getTeamName(),securityUtils.getLoggedUser().getId() , LocalDateTime.now()));

			
			m.getUser().setNotifcation(notificationList);
	         //	sendEmailEvent(idTeam, "Changed  Event", eventDto.getTitle(), eventDto.getStart(), eventDto.getEnd());

			userRepository.save(m.getUser());

		}}


	public Set<Event> listTeamEvent(long id) {

		Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("team Not Found"));
		return team.getEvents();

	}

	public void deleteEventFromList(Long id, EventDto eventDto) {
		Team team = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("team not found"));
		Set<TeamMember> teamMemberList = team.getTeamMember();
		for (TeamMember m : teamMemberList) {
			Set<Notification> notificationList = m.getUser().getNotifcation();
			
			notificationList.add(new Notification(securityUtils.getLoggedUser().getFirstName() + " " + securityUtils.getLoggedUser().getLastName() +" canceled  "+ eventDto.getTitle() + " event assigned to team",team.getTeamName(),securityUtils.getLoggedUser().getId() , LocalDateTime.now()));

			
			m.getUser().setNotifcation(notificationList);
	         //	sendEmailEvent(idTeam, "Changed  Event", eventDto.getTitle(), eventDto.getStart(), eventDto.getEnd());

	}
		
		
		team.getEvents().removeIf(e -> e.getId() == eventDto.getId());
		teamRepository.save(team);
		eventRepository.deleteById(eventDto.getId());

	
	
	
	}
	
	public Event getEventDetail(Long id) {
		return eventRepository.findById(id).orElseThrow(() -> new NotFoundException("event not found"));
	}

}
