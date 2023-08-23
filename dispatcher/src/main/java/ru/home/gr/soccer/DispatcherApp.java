package ru.home.gr.soccer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.home.gr.soccer.parse.JsonResult;
import ru.home.gr.soccer.parse.RequestBot;

@SpringBootApplication
@EnableScheduling
public class DispatcherApp {
    public static void main(String[] args) {
        SpringApplication.run(DispatcherApp.class);
        RequestBot rq = new RequestBot();
        var jsonStr = rq.getBody();

    }
}
