package com.iotproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.iotproject"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    
}
