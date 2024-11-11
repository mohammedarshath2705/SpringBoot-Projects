package com.geeksforgeeks.DigitalLibrary.controller;

import com.geeksforgeeks.DigitalLibrary.entity.Book;
import com.geeksforgeeks.DigitalLibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/Books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService=bookService;
    }

    @PostMapping("/addbook")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        Book savedbook=this.bookService.addBook(book);
        return new ResponseEntity<>(savedbook, HttpStatus.CREATED);
    }

    @GetMapping("/listbook")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> bookList = this.bookService.getAllBooks();
        return new ResponseEntity<>(bookList,HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable UUID bookId){
        Book book = this.bookService.getBookById(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
}
