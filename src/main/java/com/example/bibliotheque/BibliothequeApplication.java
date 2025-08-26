package com.example.bibliotheque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliothequeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliothequeApplication.class, args);
        System.out.println("Application Gestion de Bibliothèque démarrée !");
        System.out.println("Console H2 accessible sur : http://localhost:8080/h2-console");
    }
}