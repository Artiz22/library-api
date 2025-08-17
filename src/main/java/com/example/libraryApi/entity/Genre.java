package com.example.libraryApi.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "genres",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_genre_name", columnNames = "name")
        }
)
public class Genre {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 1000)
    private String description; // English description of the genre

    public Genre() {}

    public Genre(String name, String description) {
        this.name = name;
        this.description = description;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
