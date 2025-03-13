package com.whilter.audit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@SpringBootApplication
@RestController
public class AuditManagementService {

	public static void main(String[] args) {
		SpringApplication.run(AuditManagementService.class, args);
	}

	@RequestMapping("/v3/api/am/ping")
	public Message test() {
		return new Message("Pong from Audit Management");
	}

	class Message {
		private String id = UUID.randomUUID().toString();
		private String content;

		public Message(String content) {
			this.content = content;
		}

		public String getId() {
			return id;
		}

		public String getContent() {
			return content;
		}
	}

}
