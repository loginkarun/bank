package com.myproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.myproject.models.repositories")
public class MyprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyprojectApplication.class, args);
    }
}
