package com.build.websocketchat.handler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.eclipse.jetty.websocket.api.Session;

public class WebSocketSession {

	private static WebSocketSession instance;

	private AtomicInteger userCount = new AtomicInteger(1001);

	private Map<Session, String> sessionUserMap;

	/**
	 * Singleton reference
	 */
	private WebSocketSession() {
		sessionUserMap = new ConcurrentHashMap<>();
	}

	public static WebSocketSession initWebsocketSession() {
		if (instance == null) {
			instance = new WebSocketSession();
		}
		return instance;
	}

	public void addUser(Session session, String username) {
		if (username == null) {
			sessionUserMap.put(session, "user_" + userCount.getAndIncrement());
		} else {
			sessionUserMap.put(session, username);
		}
	}

	public String getUsername(Session session) {
		return sessionUserMap.get(session);
	}

	public void purgeSession(Session session) {
		sessionUserMap.remove(session);
	}

	public Set<Session> getAllActiveSession() {
		return sessionUserMap.keySet();
	}

	public List<String> getAllActiveUsername() {
		return sessionUserMap.values().stream().collect(Collectors.toList());
	}
}
