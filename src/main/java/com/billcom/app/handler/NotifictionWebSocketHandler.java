package com.billcom.app.handler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.billcom.app.entity.Chat;
import com.billcom.app.entity.Notification;
import com.billcom.app.entity.Team;
import com.billcom.app.entity.UserApp;
import com.billcom.app.exception.NotFoundException;
import com.billcom.app.repository.NotificationRepository;
import com.billcom.app.repository.TeamRepository;
import com.billcom.app.repository.UserRepository;

public class NotifictionWebSocketHandler extends TextWebSocketHandler {

	private final List<WebSocketSession> webSocketSessions = new ArrayList<>();
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		webSocketSessions.add(session);

			}
	@Override
	protected void handleTextMessage(WebSocketSession session , TextMessage message1) throws Exception {
		for (WebSocketSession webSocketSession : webSocketSessions) {
			webSocketSession.sendMessage(message1);
			 String[] words = message1.getPayload().split("[\\:,]");
			/* UserApp user = userRepository.findById((long) 7)
						.orElseThrow(() -> new NotFoundException("User Not Found"));
			
				System.out.println(message1.getPayload());
				Set<Notification> notificationList = user.getNotifcation();
				notificationList.add(new Notification("wissal fersi","test test test",1 , LocalDateTime.now()));
				user.setNotifcation(notificationList);
	            userRepository.save(user);*/

		}
		webSocketSessions.clear();	
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		webSocketSessions.remove(session);
	}
}
