package com.donicolas.librarymanagementsystem.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name="books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true, nullable = false)
    private String ISBN;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author_name;

    @Column(nullable = false)
    private int totalQntt;

    protected Book(){}

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public int getTotalQntt() {
        return totalQntt;
    }

    public void setTotalQntt(int totalQntt) {
        this.totalQntt = totalQntt;
    }
}
