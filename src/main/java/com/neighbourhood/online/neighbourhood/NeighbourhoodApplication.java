package com.neighbourhood.online.neighbourhood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NeighbourhoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeighbourhoodApplication.class, args);
    }

}
