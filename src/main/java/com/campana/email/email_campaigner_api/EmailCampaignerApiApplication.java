package com.campana.email.email_campaigner_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailCampaignerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailCampaignerApiApplication.class, args);
	}

}
