package com.example.bibliotheque.controller;

import com.example.bibliotheque.dto.MessageResponseDto;
import com.example.bibliotheque.dto.UserRegistrationDto;
import com.example.bibliotheque.entity.User;
import com.example.bibliotheque.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            User newUser = userService.registerNewUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponseDto("Utilisateur enregistré avec succès! ID: " + newUser.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
        }
    }

    // Pour HTTP Basic, le "login" se fait en fournissant les credentials dans l'en-tête Authorization.
    // Ce endpoint sert juste à confirmer que l'utilisateur est bien loggé (s'il a fourni les bons credentials).
    // Il peut aussi être utilisé par le client pour "tester" les credentials.
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser() {
        // Si on arrive ici avec HTTP Basic, c'est que l'authentification a réussi
        // Spring Security gère la validation des credentials avant d'atteindre ce contrôleur.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return ResponseEntity.ok(new MessageResponseDto("Utilisateur " + currentPrincipalName + " authentifié avec succès. (Session simulée via HTTP Basic)"));
    }
}