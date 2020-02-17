package ru.abstractcoder.filminfoclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FilmInfoClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilmInfoClientApplication.class, args);
    }

}
