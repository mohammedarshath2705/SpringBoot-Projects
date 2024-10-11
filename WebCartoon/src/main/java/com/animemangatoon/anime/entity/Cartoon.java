package com.animemangatoon.anime.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class Cartoon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // UUID generation handled here
    private UUID id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Characters cannot be blank")
    private String characters;

    @Override
    public String toString() {
        return "Cartoon{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", characters='" + characters + '\'' +
                '}';
    }
}
