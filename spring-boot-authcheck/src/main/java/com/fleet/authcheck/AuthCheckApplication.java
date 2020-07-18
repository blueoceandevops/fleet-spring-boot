package com.fleet.authcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AuthCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthCheckApplication.class, args);
    }
}
