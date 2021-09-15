package com.billcom.app.controller;

import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billcom.app.dto.EventDto;
import com.billcom.app.entity.Event;
import com.billcom.app.service.EventService;

@RestController
@RequestMapping(value="event/")

public class EventController {

	private EventService eventService;

	public EventController(EventService eventService) {
		this.eventService = eventService;
	}

	@PutMapping("/addEventToList/{idTeam}")
	public  void addEventToTeamList(@PathVariable long idTeam , @RequestBody EventDto event) {
		  eventService.addEvent(idTeam,event);
	}
	
	@GetMapping("/eventTeamList/{idTeam}")
	public  Set<Event> getEventTeamList(@PathVariable long idTeam) {
		 return eventService.listTeamEvent(idTeam);
	}	
	
	@PutMapping("/updateEvent/{idTeam}")
	public void updateEvent(@PathVariable long idTeam,  @RequestBody EventDto eventDto) {
		eventService.updateEvent(idTeam, eventDto);
	}
	@PutMapping("/updateDetailEvent/{idTeam}")
	public void updateDetailEvent(@PathVariable long idTeam,  @RequestBody EventDto eventDto) {
		eventService.updateDetailEvent(idTeam, eventDto);
	}
	@GetMapping("/getEventDetail/{id}")
	public Event getEventDetail(@PathVariable long id) {
		 return eventService.getEventDetail(id);
	}
	
	@PutMapping("/deleteEventFromList/{idTeam}")
	public void deleteEventFromList(@PathVariable long idTeam,@RequestBody EventDto eventDto) {
		eventService.deleteEventFromList(idTeam, eventDto);
	}
}
