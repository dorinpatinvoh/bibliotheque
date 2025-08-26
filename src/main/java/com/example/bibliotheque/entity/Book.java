package com.example.bibliotheque.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "livres") // Nom de table en français
@Getter
@Setter
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre ne peut pas être vide")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "L'auteur ne peut pas être vide")
    @Column(nullable = false)
    private String author;

    private boolean available = true;

    @OneToMany(mappedBy = "book")
    private Set<Loan> loans;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
    }
}