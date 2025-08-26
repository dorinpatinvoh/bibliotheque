package com.example.bibliotheque.controller;

import com.example.bibliotheque.dto.LoanRequestDto;
import com.example.bibliotheque.dto.MessageResponseDto;
import com.example.bibliotheque.entity.Loan;
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

@RestController
@RequestMapping("/loans")
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
    private static class LoanResponse {
        public Long id;
        public Long bookId;
        public String bookTitle;
        public Long userId;
        public String username;
        public java.time.LocalDate startDate;
        public java.time.LocalDate endDate;

        public LoanResponse(Loan loan) {
            this.id = loan.getId();
            this.bookId = loan.getBook().getId();
            this.bookTitle = loan.getBook().getTitle();
            this.userId = loan.getUser().getId();
            this.username = loan.getUser().getUsername();
            this.startDate = loan.getStartDate();
            this.endDate = loan.getEndDate();
        }
    }


    @PostMapping
    public ResponseEntity<?> borrowBook(@Valid @RequestBody LoanRequestDto loanRequestDto) {
        String username = getCurrentUsername();
        Loan loan = loanService.borrowBook(loanRequestDto.getBookId(), username);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoanResponse(loan));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long id) {
        String username = getCurrentUsername();
        Loan loan = loanService.returnBook(id, username);
        return ResponseEntity.ok(new LoanResponse(loan));
    }

    @GetMapping("/my-loans")
    public ResponseEntity<List<LoanResponse>> getMyLoans() {
        String username = getCurrentUsername();
        List<Loan> loans = loanService.getUserLoans(username);
        List<LoanResponse> loanResponses = loans.stream().map(LoanResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(loanResponses);
    }
}