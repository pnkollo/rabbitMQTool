package com.example.rabbitshell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestRabbitshellApplication {

	public static void main(String[] args) {
		SpringApplication.from(RabbitshellApplication::main).with(TestRabbitshellApplication.class).run(args);
	}

}
