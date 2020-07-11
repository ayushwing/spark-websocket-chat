package com.build.websocketchat;

import static j2html.TagCreator.article;
import static j2html.TagCreator.attrs;
import static j2html.TagCreator.b;
import static j2html.TagCreator.p;
import static j2html.TagCreator.span;
import static spark.Spark.init;
import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONException;
import org.json.JSONObject;

import com.build.websocketchat.handler.WebSocketHandler;	
public class Main {
	
	
	public static Map<org.eclipse.jetty.websocket.api.Session, String> userNameMap = new ConcurrentHashMap<>();
    static int nextUserNumber = 1;
    public static int anonymousUserNumber = 1;

	
	public static void main(String[] args) {
		port(getHerokuAssignedPort());
        staticFiles.location("/public");
        staticFiles.expireTime(600);
        webSocket("/chat", WebSocketHandler.class);
		init();
	}
	
	
	 public static void broadcastMessage(String sender, String message) {
		 
		 userNameMap.keySet().stream().filter(Session::isOpen).forEach(session -> {
			 try {
				session.getRemote().sendString(String.valueOf(new JSONObject()
				            .put("userMessage", createHtmlMessageFromSender(sender, message))
				            .put("userlist", userNameMap.values())
				        ));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 });
	 }
	 
	  static int getHerokuAssignedPort() {
	        ProcessBuilder processBuilder = new ProcessBuilder();
	        if (processBuilder.environment().get("PORT") != null) {
	            return Integer.parseInt(processBuilder.environment().get("PORT"));
	        }
	        return 4567;
	    }
	 
	 private static String createHtmlMessageFromSender(String sender, String message) {
	        return article(
	            b(sender + " says:"),
	            span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
	            p(message)
	        ).render();
	    }

	 
}
