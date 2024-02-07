package com.example.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import com.example.models.Person;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author must not be empty")
    @Size(min = 2, max = 255, message = "Author must be between 2 and 255 characters")
    @Column(name = "author")
    private String author;

    @Min(value = 0, message = "The year of publication of the book must be greater than 0")
    @Column(name = "year")
    private int year;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "taken_at")
    private Date takenAt;

    @Transient
    private boolean expired;

    @ManyToOne()
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Book() {

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Date takenAt) {
        this.takenAt = takenAt;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public boolean isExpired() {
        Date current = new Date();

        long diffInMillis = Math.abs(current.getTime() - getTakenAt().getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);

        return diffInDays > 10;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
