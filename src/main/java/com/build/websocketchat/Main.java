package com.build.websocketchat;

import static spark.Spark.init;
import static spark.Spark.port;

public class Main {

	public static void main(String[] args) {
		port(ServerConfigs.getHerokuAssignedPort());
		ServerConfigs.initStatic();
		ServerConfigs.websocketInitializer();
		init();
	}

}
