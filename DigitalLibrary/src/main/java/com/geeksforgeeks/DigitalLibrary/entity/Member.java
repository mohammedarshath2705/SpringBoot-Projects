package com.geeksforgeeks.DigitalLibrary.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.geeksforgeeks.DigitalLibrary.enums.IssueStatus;
import com.geeksforgeeks.DigitalLibrary.enums.SubscriptionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String username;

   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String role;

    private String mobileNumber;

    // Corrected the type of issueDataList to IssueData
    @OneToMany(mappedBy = "member")
    private List<IssueData> issueDataList;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;
}
