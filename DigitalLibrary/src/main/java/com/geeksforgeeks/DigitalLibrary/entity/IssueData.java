package com.geeksforgeeks.DigitalLibrary.entity;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.geeksforgeeks.DigitalLibrary.enums.IssueStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class IssueData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "member_id")
    @JsonIncludeProperties({"id", "firstName", "lastName"})
    private Member member;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "book_id")
    @JsonIncludeProperties({"id", "name", "author"})
    private Book book;

    @Builder.Default
    private Instant createdAt = Instant.now ();

    private Instant expirationDate;

    @NotNull
    private double amountPaid;

    @NotNull
    @Builder.Default
    private IssueStatus status = IssueStatus.ISSUED;


    public Instant calculateExpirationDate() {
        this.expirationDate = this.createdAt.plus ( 15, ChronoUnit.DAYS );
        return this.expirationDate;
    }

    public double calculateAmountPaid() {
        this.amountPaid = this.book.getPrice () * 0.05D;
        return this.amountPaid;
    }
}
