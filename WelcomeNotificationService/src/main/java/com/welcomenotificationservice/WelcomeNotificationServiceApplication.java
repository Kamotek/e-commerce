package com.welcomenotificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@SpringBootApplication
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class WelcomeNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WelcomeNotificationServiceApplication.class, args);
    }

}
