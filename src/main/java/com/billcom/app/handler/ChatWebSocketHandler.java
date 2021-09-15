package com.billcom.app.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.billcom.app.entity.Chat;
import com.billcom.app.entity.Team;
import com.billcom.app.repository.TeamRepository;

public class ChatWebSocketHandler extends TextWebSocketHandler {

	@Autowired
	private TeamRepository teamRepository;
	private final List<WebSocketSession> webSocketSessions = new ArrayList<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		webSocketSessions.add(session);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		Long id = (long)0;
		String privious = "kjhjknjknj";
		String ul = "ul";

		for (WebSocketSession webSocketSession : webSocketSessions) {
			webSocketSession.sendMessage(message);
			 String[] words = message.getPayload().split("[\\:,]");
	           
	           Long teamId = new Long(words[1].substring(1, words[1].length()-1));
	           Long userId = new Long(words[3]);
             
			  if( id != userId & ! privious.equalsIgnoreCase(words[7].substring(1, words[7].length()-2)) & ! ul.equalsIgnoreCase(words[7].substring(1, words[7].length()-2)) ) {
			  Team team = teamRepository.findById(teamId).get();
			  team.getChatList().add(new Chat(userId, words[5].substring(1, words[5].length()-1),words[7].substring(1, words[7].length()-2)));
			  teamRepository.save(team);
			  id = userId;
			  privious= words[7].substring(1, words[7].length()-2);
			  
			  
			  
			  }
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		webSocketSessions.remove(session);
	}
}