package com.ewallet.heropay.walletservice.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    private String walletId;

    private Long balance;

    private String currency;

    @UpdateTimestamp
    private Date updatedOn;

    @CreationTimestamp
    private Date createdOn;
}
