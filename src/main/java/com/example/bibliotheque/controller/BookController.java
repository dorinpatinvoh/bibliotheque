package com.example.bibliotheque.controller;

import com.example.bibliotheque.entity.Book;
import com.example.bibliotheque.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllAvailableBooks() {
        List<Book> books = bookService.getAllAvailableBooks();
        return ResponseEntity.ok(books);
    }
}