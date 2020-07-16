package com.build.websocketchat.handler;

import static j2html.TagCreator.article;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BroadcastHandler {

	private static final Logger logger = LoggerFactory.getLogger(BroadcastHandler.class);

	private final WebSocketSession webSocketSession = WebSocketSession.initWebsocketSession();

	// TODO: Send privately also can be option after deploy
	
	public boolean broadcastAll(Session session, String message) {

		String senderName = webSocketSession.getUsername(session);

		webSocketSession.getAllActiveSession().stream().filter(Session::isOpen).forEach(connectedSessions -> {
			try {
				connectedSessions.getRemote()
						.sendString(new JSONObject().put("response", wrapMessageInArticle(senderName, message))
								.put("activeUsers", webSocketSession.getAllActiveUsername()).toString());

			} catch (IOException e) {
				logger.error("Exception in broadcasting to a session", e);
			}
		});

		return true;
	}

	private static String wrapMessageInArticle(String sender, String message) {
		return article(b(sender),
				span(attrs(".timestamp"),
						DateTimeFormatter.ofPattern("hh:mm a").format(LocalTime.now(ZoneId.of("Asia/Kolkata")))),
				p(message)).render();
	}

}
