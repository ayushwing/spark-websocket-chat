package com.build.websocketchat;

import static spark.Spark.init;
import static spark.Spark.port;

public class Main {

	static int nextUserNumber = 1;
	public static int anonymousUserNumber = 1;

	public static void main(String[] args) {
		port(ServerConfigs.getHerokuAssignedPort());
		ServerConfigs.initStatic();
		ServerConfigs.websocketInitializer();
		init();
	}

}
