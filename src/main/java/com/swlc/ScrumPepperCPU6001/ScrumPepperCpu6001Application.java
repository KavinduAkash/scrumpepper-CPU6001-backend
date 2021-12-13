package com.swlc.ScrumPepperCPU6001;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ScrumPepperCpu6001Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ScrumPepperCpu6001Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ScrumPepperCpu6001Application.class, args);
	}

}
