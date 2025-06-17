package com.shadowfox;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty role;

    public User(int id, String username, String password, String role) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getUsername() { return username.get(); }
    public String getPassword() { return password.get(); }
    public String getRole() { return role.get(); }

    // Property Getters for TableView (if needed)
    public SimpleIntegerProperty idProperty() { return id; }
    public SimpleStringProperty usernameProperty() { return username; }
    public SimpleStringProperty passwordProperty() { return password; }
    public SimpleStringProperty roleProperty() { return role; }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setUsername(String username) { this.username.set(username); }
    public void setPassword(String password) { this.password.set(password); }
    public void setRole(String role) { this.role.set(role); }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id.get() +
               ", username='" + username.get() + '\'' +
               ", role='" + role.get() + '\'' +
               '}';
    }
} 