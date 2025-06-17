package com.shadowfox;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty genre;
    private final SimpleIntegerProperty available;

    public Book(int id, String title, String author, String isbn, String genre, int available) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.genre = new SimpleStringProperty(genre);
        this.available = new SimpleIntegerProperty(available);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public String getIsbn() { return isbn.get(); }
    public String getGenre() { return genre.get(); }
    public int getAvailable() { return available.get(); }

    // Property Getters for TableView
    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty titleProperty() { return title; }
    public SimpleStringProperty authorProperty() { return author; }
    public SimpleStringProperty isbnProperty() { return isbn; }
    public SimpleStringProperty genreProperty() { return genre; }
    public SimpleIntegerProperty availableProperty() { return available; }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setTitle(String title) { this.title.set(title); }
    public void setAuthor(String author) { this.author.set(author); }
    public void setIsbn(String isbn) { this.isbn.set(isbn); }
    public void setGenre(String genre) { this.genre.set(genre); }
    public void setAvailable(int available) { this.available.set(available); }

    @Override
    public String toString() {
        return "Book{" +
               "id=" + id.get() +
               ", title='" + title.get() + '\'' +
               ", author='" + author.get() + '\'' +
               ", isbn='" + isbn.get() + '\'' +
               ", genre='" + genre.get() + '\'' +
               ", available=" + available.get() +
               '}';
    }
} 