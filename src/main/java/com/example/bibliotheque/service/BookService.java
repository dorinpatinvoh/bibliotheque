package com.example.bibliotheque.service;

import com.example.bibliotheque.entity.Book;
import com.example.bibliotheque.exception.ResourceNotFoundException;
import com.example.bibliotheque.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }

    public List<Book> getAllBooks() { // Pourrait être utile pour un admin
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Livre non trouvé avec l'ID : " + bookId));
    }
}