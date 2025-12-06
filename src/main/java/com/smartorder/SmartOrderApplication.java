package com.smartorder;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.smartorder"})
public class SmartOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartOrderApplication.class, args);
	}

}
