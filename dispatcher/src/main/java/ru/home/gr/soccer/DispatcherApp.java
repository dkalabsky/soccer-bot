package ru.home.gr.soccer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = "ru.home.gr.soccer.bot")
@EnableScheduling
public class DispatcherApp {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(DispatcherApp.class);
    }
}
