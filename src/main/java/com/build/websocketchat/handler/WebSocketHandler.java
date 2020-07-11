package com.build.websocketchat.handler;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.build.websocketchat.Main;

@WebSocket
public class WebSocketHandler {

	private String sender;
	private String msg;
	
	
	@OnWebSocketConnect
	public void onConnect(Session session) throws IOException, InterruptedException, ExecutionException, TimeoutException {
		/*String username = "User-" + Main.nextUserNumber++;
		Main.userNameMap.put(session,username);
		Main.broadcastMessage(sender = "Server", msg = (username + " joined the chat"));*/
	}
	
	@OnWebSocketClose
	public void onClose(Session session, int statusCode, String reason) {
		String userName = Main.userNameMap.get(session);
		Main.userNameMap.remove(session);
		Main.broadcastMessage(sender = "SocketServer ", msg = (userName + " left the chat"));
	}
	@OnWebSocketMessage
	public void onMessage(Session session, String message) {
		if(StringUtil.isNotBlank(message)){
			if(message.contains("webSocketAyushUserName###")){
				String[] userName = message.split("###");
				Main.userNameMap.put(session, userName[1]);
				Main.broadcastMessage(sender = "SocketServer ", msg = (userName[1] + " joined the chat"));
			}else if(message.equals("###webSocketAnonymousUserName###")){
				Main.userNameMap.put(session, "Anonymous-"+Main.anonymousUserNumber++);
				Main.broadcastMessage(sender = "SocketServer ", msg = (Main.userNameMap.get(session)+ " joined the chat. He might be too shy to say his name"));
			}else
				Main.broadcastMessage(sender = Main.userNameMap.get(session), msg = message);
			}
		}
	
}
