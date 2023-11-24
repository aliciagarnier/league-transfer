package com.example.demodatabasepj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class DemodatabasepjApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemodatabasepjApplication.class, args);
    }
}
