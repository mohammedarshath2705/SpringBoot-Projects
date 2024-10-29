package com.geeksforgeeks.library.elib.entity;

import com.geeksforgeeks.library.elib.enums.SubscriptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@With
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;

    private String lastName;

    private String mobileNumber; // Updated for consistent camelCase naming

    @Email
    private String email; // Updated for consistent camelCase naming

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;



}
