package com.build.websocketchat.handler;

import static spark.utils.StringUtils.isEmpty;
import static spark.utils.StringUtils.isNotEmpty;

import org.eclipse.jetty.io.EofException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class WebSocketHandler {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

	private final BroadcastHandler broadcastHandler;
	private final WebSocketSession webSocketSession;

	public WebSocketHandler() {
		broadcastHandler = new BroadcastHandler();
		webSocketSession = WebSocketSession.initWebsocketSession();
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		webSocketSession.addUser(session, null);
	}

	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		broadcastHandler.broadcastAll(session, "left the chat..");
		webSocketSession.purgeSession(session);
	}

	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		if (isEmpty(message)) {
			return;
		}

		if (message.contains("@#@joined the chat..")) {
			String[] username = message.split("@#@");
			if(isNotEmpty(username[0])) {
				webSocketSession.addUser(session, username[0]);
			}
			broadcastHandler.broadcastAll(session, username[1]);
		} else {
			broadcastHandler.broadcastAll(session, message);
		}
	}

	@OnWebSocketError
	public void onError(Throwable t) {
		if (!(t instanceof EofException)) {
			logger.error("error", t);
		}
	}

}
