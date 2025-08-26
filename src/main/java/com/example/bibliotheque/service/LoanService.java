package com.example.bibliotheque.service;

import com.example.bibliotheque.entity.Book;
import com.example.bibliotheque.entity.Loan;
import com.example.bibliotheque.entity.User;
import com.example.bibliotheque.exception.BookNotAvailableException;
import com.example.bibliotheque.exception.OperationNotAllowedException;
import com.example.bibliotheque.exception.ResourceNotFoundException;
import com.example.bibliotheque.repository.BookRepository;
import com.example.bibliotheque.repository.LoanRepository;
import com.example.bibliotheque.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository; // Ou UserService

    @Transactional
    public Loan borrowBook(Long bookId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + username));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'ID : " + bookId));

        if (!book.isAvailable()) {
            throw new BookNotAvailableException("Le livre '" + book.getTitle() + "' n'est pas disponible pour l'emprunt.");
        }
        
        // Vérifier si le livre est déjà emprunté par quelqu'un d'autre (double sécurité)
        loanRepository.findByBookIdAndEndDateIsNull(bookId).ifPresent(l -> {
            throw new BookNotAvailableException("Le livre '" + book.getTitle() + "' est actuellement emprunté.");
        });


        book.setAvailable(false);
        bookRepository.save(book);

        Loan loan = new Loan(user, book, LocalDate.now());
        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnBook(Long loanId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + username));

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Emprunt non trouvé avec l'ID : " + loanId));

        // Vérifier que l'utilisateur qui rend le livre est bien celui qui l'a emprunté
        if (!loan.getUser().getUsername().equals(username)) {
            throw new OperationNotAllowedException("Vous n'êtes pas autorisé à retourner ce livre.");
        }

        if (loan.getEndDate() != null) {
            throw new OperationNotAllowedException("Ce livre a déjà été retourné.");
        }

        loan.setEndDate(LocalDate.now());
        Book book = loan.getBook();
        book.setAvailable(true);

        bookRepository.save(book);
        return loanRepository.save(loan);
    }

    @Transactional(readOnly = true)
    public List<Loan> getUserLoans(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé: " + username));
        return loanRepository.findByUser(user);
    }
}