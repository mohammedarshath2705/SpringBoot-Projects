package com.geeksforgeeks.DigitalLibrary.repository;

import com.geeksforgeeks.DigitalLibrary.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
