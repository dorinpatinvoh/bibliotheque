package com.example.bibliotheque.repository;

import com.example.bibliotheque.entity.Loan;
import com.example.bibliotheque.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    Optional<Loan> findByIdAndUser(Long id, User user); // Pour s'assurer que l'utilisateur ne modifie que ses prêts
    Optional<Loan> findByBookIdAndEndDateIsNull(Long bookId); // Pour vérifier si un livre est déjà emprunté et non rendu
}