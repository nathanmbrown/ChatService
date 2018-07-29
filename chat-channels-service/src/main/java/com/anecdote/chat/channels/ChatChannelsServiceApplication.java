package com.anecdote.chat.channels;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ChatChannelsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatChannelsServiceApplication.class, args);
	}
}
