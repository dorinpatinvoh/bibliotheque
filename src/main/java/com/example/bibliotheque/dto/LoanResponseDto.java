package com.example.bibliotheque.dto;

import com.example.bibliotheque.entity.Loan;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LoanResponseDto {
    public Long id;
    public Long bookId;
    public String bookTitle;
    public Long userId;
    public String username;
    public LocalDate startDate;
    public LocalDate endDate;

    public LoanResponseDto(Loan loan) {
        this.id = loan.getId();
        this.bookId = loan.getBook().getId();
        this.bookTitle = loan.getBook().getTitle();
        this.userId = loan.getUser().getId();
        this.username = loan.getUser().getUsername();
        this.startDate = loan.getStartDate();
        this.endDate = loan.getEndDate();
    }
}