package com.geeksforgeeks.DigitalLibrary.entity;


import com.geeksforgeeks.DigitalLibrary.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String author;

    @Column(unique = true)
    private String isbn;

    private Double price;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;
}
