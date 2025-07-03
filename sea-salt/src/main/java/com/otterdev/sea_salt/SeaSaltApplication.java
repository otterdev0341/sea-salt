package com.otterdev.sea_salt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
// This annotation enables Spring WebFlux, which is a reactive web framework.
@ComponentScan({"com.otterdev.sea_salt.controller", 
				"com.otterdev.sea_salt.config",
				"com.otterdev.sea_salt.service",
				"com.otterdev.sea_salt.repository"
				// Add other packages as needed
				})
// This annotation allows Spring to scan for components in the specified packages.
public class SeaSaltApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeaSaltApplication.class, args);
	}

}
