package com.anecdote.chat.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class ChatRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatRegistryApplication.class, args);
	}
}
