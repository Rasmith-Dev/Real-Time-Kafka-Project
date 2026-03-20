package com.example;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class SpringBootConsumerApplication {
    public static void main(String[] args) throws SQLException {
        Server.createWebServer("-webPort", "8082", "-webAllowOthers").start();
        SpringApplication.run(SpringBootConsumerApplication.class);
    }
}
