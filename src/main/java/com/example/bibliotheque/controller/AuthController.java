package com.example.bibliotheque.controller;

import com.example.bibliotheque.dto.JwtResponseDto;
import com.example.bibliotheque.dto.LoginRequestDto;
import com.example.bibliotheque.dto.MessageResponseDto;
import com.example.bibliotheque.dto.UserRegistrationDto;
import com.example.bibliotheque.entity.User;
import com.example.bibliotheque.security.jwt.JwtUtils;
import com.example.bibliotheque.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException; // Importez cette classe
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException; // Importez cette classe
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            User newUser = userService.registerNewUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new MessageResponseDto("Utilisateur enregistré avec succès! ID: " + newUser.getId()));
        } catch (IllegalArgumentException e) {
            // Cette exception est déjà gérée pour les noms d'utilisateur existants
            return ResponseEntity.badRequest().body(new MessageResponseDto(e.getMessage()));
        } catch (Exception e) {
            // Pour toute autre erreur inattendue lors de l'enregistrement
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponseDto("Erreur inattendue lors de l'enregistrement de l'utilisateur: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            // 1. Authentifier l'utilisateur via l'AuthenticationManager
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // 2. Mettre l'objet Authentication dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. Générer le token JWT
            String jwt = jwtUtils.generateJwtToken(authentication);

            // 4. Récupérer les détails de l'utilisateur et ses rôles
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User userEntity = userService.findByUsername(userDetails.getUsername()); // Pour récupérer l'ID
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // 5. Retourner le token JWT et les informations de l'utilisateur
            return ResponseEntity.ok(new JwtResponseDto(jwt,
                    userEntity.getId(),
                    userDetails.getUsername(),
                    roles));
        } catch (BadCredentialsException e) {
            // Cette exception est levée si le nom d'utilisateur ou le mot de passe est incorrect
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDto("Identifiants de connexion invalides. Veuillez vérifier votre nom d'utilisateur et votre mot de passe."));
        } catch (AuthenticationException e) {
            // Pour toute autre erreur d'authentification (moins courante pour BadCredentials ici)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponseDto("Échec de l'authentification: " + e.getMessage()));
        } catch (Exception e) {
            // Catch générique pour toute autre erreur inattendue
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponseDto("Une erreur inattendue s'est produite lors de la connexion: " + e.getMessage()));
        }
    }
}