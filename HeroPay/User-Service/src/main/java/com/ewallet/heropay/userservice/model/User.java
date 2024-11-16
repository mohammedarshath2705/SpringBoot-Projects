package com.ewallet.heropay.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true,nullable = false)
    @Min(10)
    private String phone;

    @Column(unique = true,nullable = false)
    private String email;

    private int age;

    @CreationTimestamp
    private  Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;
}
