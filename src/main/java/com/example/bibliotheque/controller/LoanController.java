package com.example.bibliotheque.controller;

import com.example.bibliotheque.dto.LoanRequestDto;
import com.example.bibliotheque.dto.MessageResponseDto;
import com.example.bibliotheque.entity.Loan;
import com.example.bibliotheque.dto.LoanResponseDto;
import com.example.bibliotheque.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController//Dit a spring cette classe est un controller web.
@RequestMapping("/loans")//Dit tout les urls gerer par cette classe commence par /loans.
public class LoanController {

    @Autowired
    private LoanService loanService;

    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Aucun utilisateur authentifié trouvé.");
        }
        return authentication.getName();
    }
    
    //DTO simple pour la réponse d'un prêt



    @PostMapping//Créer
    public ResponseEntity<?> borrowBook(@Valid @RequestBody LoanRequestDto loanRequestDto) {
        String username = getCurrentUsername();
        Loan loan = loanService.borrowBook(loanRequestDto.getBookId(), username);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoanResponseDto(loan));
    }

    @PutMapping("/{id}/return")//Mettre a jour.
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        String username = getCurrentUsername();
        Loan loan = loanService.returnBook(id, username);
        return ResponseEntity.ok(new LoanResponseDto(loan));
    }

    @GetMapping("/my-loans")//Lire
    public ResponseEntity<List<LoanResponseDto>> getMyLoans() {
        String username = getCurrentUsername();
        List<Loan> loans = loanService.getUserLoans(username);
        List<LoanResponseDto> loanResponses = loans.stream().map(LoanResponseDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(loanResponses);
    }
}