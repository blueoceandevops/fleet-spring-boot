package com.fleet.activiti5;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Activiti5Application {

    public static void main(String[] args) {
        SpringApplication.run(Activiti5Application.class, args);
    }
}
