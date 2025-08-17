package com.example.libraryApi.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "books", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "author_id"})
})
public class Book {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 300)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @ManyToMany
    @JoinTable(
            name = "book_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres = new ArrayList<>();

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(length = 5000)
    private String synopsis;

    public Book() {}

    public Book(String title, Author author, List<Genre> genres, LocalDate publicationDate, String coverImageUrl, String synopsis) {
        this.title = title;
        this.author = author;
        this.genres = genres;
        this.publicationDate = publicationDate;
        this.coverImageUrl = coverImageUrl;
        this.synopsis = synopsis;
    }

    // Getters / Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public LocalDate getPublicationDate() { return publicationDate; }
    public void setPublicationDate(LocalDate publicationDate) { this.publicationDate = publicationDate; }

    public String getCoverImageUrl() { return coverImageUrl; }
    public void setCoverImageUrl(String coverImageUrl) { this.coverImageUrl = coverImageUrl; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }
}



