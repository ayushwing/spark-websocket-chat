package com.build.websocketchat.handler;

import static j2html.TagCreator.article;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadcastHandler {

	private static final Logger logger = LoggerFactory.getLogger(BroadcastHandler.class);
	
	private final WebSocketSession webSocketSession = WebSocketSession.initWebsocketSession();

	public boolean broadcastAll(Session session, String message) {

		String senderName = webSocketSession.getUsername(session);

		webSocketSession.getAllActiveSession().stream()
											  .filter(Session::isOpen)
											  .forEach(connectedSessions -> {
			try {
				connectedSessions.getRemote()
								 .sendString(new JSONObject().put("response", wrapMessageInArticle(senderName, message))
										 					 .put("activeUsers", webSocketSession.getAllActiveUsername()).toString());
				
			} catch (IOException e) {
				logger.error("Exception in broadcasting to a session" , e);
			}
		});

		return true;
	}

	private static String wrapMessageInArticle(String sender, String message) {
		return article(b(sender),
				span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())), p(message)).render();
	}
	
	

}
