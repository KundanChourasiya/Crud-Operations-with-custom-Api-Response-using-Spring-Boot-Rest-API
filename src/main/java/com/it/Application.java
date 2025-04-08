package com.it;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// configure swagger OpenAPIDefinition
@OpenAPIDefinition(
		info = @Info(
				title = "Crud Operations with custom Api Response",
				version = "1.0",
				description = "In this Api we customize Api Response.",
				contact = @Contact(
						name = "Kundan Kumar Chourasiya",
						email = "mailmekundanchourasiya@gmail.com"
				)
		),
		servers = @Server(
				url = "http://localhost:8080",
				description = "Crud Operations with custom Api Response url"
		)
)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
