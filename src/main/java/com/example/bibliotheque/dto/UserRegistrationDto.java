package com.example.bibliotheque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {
    @NotBlank(message = "Le nom d'utilisateur est requis")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 6, max = 100)
    private String password;
}