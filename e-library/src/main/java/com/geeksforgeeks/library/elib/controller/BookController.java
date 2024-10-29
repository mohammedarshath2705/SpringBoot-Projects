package com.geeksforgeeks.library.elib.controller;


import com.geeksforgeeks.library.elib.entity.Book;
import com.geeksforgeeks.library.elib.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService=bookService;
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        Book savedbook=this.bookService.addBook(book);
        return new ResponseEntity<>(savedbook, HttpStatus.CREATED);
    }

    @GetMapping("/List")
    public ResponseEntity<List<Book> > getAllBooks(){
        List<Book> bookList = this.bookService.getAllBooks();
        return new ResponseEntity<>(bookList,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Book> getBookById(@RequestParam UUID bookId){
        Book book = this.bookService.getBookById(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
    }


