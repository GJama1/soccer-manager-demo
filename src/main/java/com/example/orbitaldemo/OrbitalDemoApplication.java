package com.example.orbitaldemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class OrbitalDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrbitalDemoApplication.class, args);
    }

}
