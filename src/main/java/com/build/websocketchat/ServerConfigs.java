package com.build.websocketchat;

import static spark.Spark.staticFiles;
import static spark.Spark.webSocket;
import static spark.Spark.webSocketIdleTimeoutMillis;

import com.build.websocketchat.handler.WebSocketHandler;

public class ServerConfigs {

	public static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567;
	}

	public static void initStatic() {
		staticFiles.location("/webchat");
		staticFiles.expireTime(360);
	}

	public static void websocketInitializer() {
		webSocket("/chat", WebSocketHandler.class);
		webSocketIdleTimeoutMillis(3600000);  // 60 minutes
	}

}
