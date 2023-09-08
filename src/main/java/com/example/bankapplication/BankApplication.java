package com.example.bankapplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Samuel Bank Application",
                description = "Backend RestApIs for SBA",
                version = "v1.0",
                contact = @Contact(
                        name = "Samuel Okafor",
                        email = "okaforsamuel1000@gmail.com",
                        url = "https://github.com/Okafor-Samuel/BankApplication.git"
                ),
                license = @License(
                        name = "SAMUEL OKAFOR",
                        url = "https://github.com/Okafor-Samuel/BankApplication.git"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "SAMUEL BANK APPLICATION DOCUMENTATION",
                url = "https://github.com/Okafor-Samuel/BankApplication.git"
        )
)
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }

}
