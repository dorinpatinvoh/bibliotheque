package com.example.bibliotheque.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanRequestDto {
    @NotNull(message = "L'ID du livre est requis pour l'emprunt")
    private Long bookId;
}