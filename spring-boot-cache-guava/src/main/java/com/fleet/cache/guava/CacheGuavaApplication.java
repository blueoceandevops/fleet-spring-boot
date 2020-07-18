package com.fleet.cache.guava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CacheGuavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheGuavaApplication.class, args);
    }
}
