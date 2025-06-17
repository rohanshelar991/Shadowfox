package com.shadowfox;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryManagementSystem extends Application {

    private TableView<Book> bookTable;
    private ObservableList<Book> bookList;
    private TextField titleField, authorField, isbnField;
    private ComboBox<String> genreComboBox;
    private CheckBox availableCheckBox;

    private TableView<User> userTable;
    private ObservableList<User> userList;
    private TextField usernameField, passwordField, roleField;

    @Override
    public void start(Stage primaryStage) {
        // Initialize database and tables
        DatabaseManager.createNewDatabase();
        DatabaseManager.createTables();

        // Initialize book list
        bookList = FXCollections.observableArrayList();
        loadBooksFromDatabase();

        // Main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #2b2b2b;");

        // Header
        Label headerLabel = new Label("Library Management System");
        headerLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        headerLabel.setTextFill(Color.WHITE);
        HBox header = new HBox(headerLabel);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #1e1e1e;");
        root.setTop(header);

        // Tabs for Books and Users
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab bookTab = new Tab("Books");
        bookTab.setContent(createBookManagementTab());

        Tab userTab = new Tab("Users");
        userTab.setContent(createUserManagementTab());

        tabPane.getTabs().addAll(bookTab, userTab);
        root.setCenter(tabPane);

        // Scene and Stage setup
        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Library Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createBookManagementTab() {
        // Book Table
        bookTable = new TableView<>();
        bookTable.setItems(bookList);
        bookTable.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Book, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);

        TableColumn<Book, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorCol.setPrefWidth(150);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        isbnCol.setPrefWidth(120);

        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreCol.setPrefWidth(100);

        TableColumn<Book, Integer> availableCol = new TableColumn<>("Available");
        availableCol.setCellValueFactory(new PropertyValueFactory<>("available"));
        availableCol.setPrefWidth(80);

        bookTable.getColumns().addAll(idCol, titleCol, authorCol, isbnCol, genreCol, availableCol);

        // Book Form
        GridPane bookForm = new GridPane();
        bookForm.setHgap(10);
        bookForm.setVgap(10);
        bookForm.setPadding(new Insets(20));
        bookForm.setStyle("-fx-background-color: #363636; -fx-background-radius: 10;");

        titleField = createStyledTextField("Title");
        authorField = createStyledTextField("Author");
        isbnField = createStyledTextField("ISBN (e.g., 978-3-16-148410-0)");
        genreComboBox = new ComboBox<>();
        genreComboBox.getItems().addAll(
            "Fiction",
            "Non-Fiction",
            "Mystery",
            "Science Fiction",
            "Fantasy",
            "Romance",
            "Biography",
            "History",
            "Science",
            "Technology",
            "Philosophy",
            "Poetry",
            "Drama",
            "Comedy",
            "Horror",
            "Thriller",
            "Adventure",
            "Children's",
            "Young Adult",
            "Reference"
        );
        genreComboBox.setPromptText("Select Genre");
        genreComboBox.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white;");
        availableCheckBox = new CheckBox("Available");
        availableCheckBox.setTextFill(Color.WHITE);
        availableCheckBox.setSelected(true);

        // Add ISBN validation
        isbnField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty()) {
                // Remove any non-alphanumeric characters except hyphens
                String cleaned = newVal.replaceAll("[^0-9X-]", "");
                if (!cleaned.equals(newVal)) {
                    isbnField.setText(cleaned);
                }
            }
        });

        bookForm.add(createStyledLabel("Title:"), 0, 0);
        bookForm.add(titleField, 1, 0);
        bookForm.add(createStyledLabel("Author:"), 0, 1);
        bookForm.add(authorField, 1, 1);
        bookForm.add(createStyledLabel("ISBN:"), 0, 2);
        bookForm.add(isbnField, 1, 2);
        bookForm.add(createStyledLabel("Genre:"), 0, 3);
        bookForm.add(genreComboBox, 1, 3);
        bookForm.add(availableCheckBox, 1, 4);

        // Book Buttons
        HBox bookButtons = new HBox(10);
        bookButtons.setAlignment(Pos.CENTER);
        bookButtons.setPadding(new Insets(10));
        Button addBookBtn = createStyledButton("Add Book", "#4CAF50");
        Button updateBookBtn = createStyledButton("Update Book", "#2196F3");
        Button deleteBookBtn = createStyledButton("Delete Book", "#f44336");
        Button clearBookFormBtn = createStyledButton("Clear Form", "#9E9E9E");
        bookButtons.getChildren().addAll(addBookBtn, updateBookBtn, deleteBookBtn, clearBookFormBtn);

        // Event Handlers for Books
        addBookBtn.setOnAction(e -> addBook());
        updateBookBtn.setOnAction(e -> updateBook());
        deleteBookBtn.setOnAction(e -> deleteBook());
        clearBookFormBtn.setOnAction(e -> clearBookForm());

        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                titleField.setText(newSelection.getTitle());
                authorField.setText(newSelection.getAuthor());
                isbnField.setText(newSelection.getIsbn());
                genreComboBox.setValue(newSelection.getGenre());
                availableCheckBox.setSelected(newSelection.getAvailable() == 1);
            } else {
                clearBookForm();
            }
        });

        VBox bookTabContent = new VBox(10);
        bookTabContent.setPadding(new Insets(20));
        bookTabContent.getChildren().addAll(bookTable, bookForm, bookButtons);
        return bookTabContent;
    }

    private VBox createUserManagementTab() {
        // User Table (basic)
        userTable = new TableView<>();
        userList = FXCollections.observableArrayList();
        loadUsersFromDatabase();
        userTable.setItems(userList);
        userTable.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

        TableColumn<User, Integer> userIdCol = new TableColumn<>("ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userIdCol.setPrefWidth(50);

        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(150);

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setPrefWidth(100);

        userTable.getColumns().addAll(userIdCol, usernameCol, roleCol);

        // User Form
        GridPane userForm = new GridPane();
        userForm.setHgap(10);
        userForm.setVgap(10);
        userForm.setPadding(new Insets(20));
        userForm.setStyle("-fx-background-color: #363636; -fx-background-radius: 10;");

        usernameField = createStyledTextField("Username");
        passwordField = createStyledTextField("Password");
        roleField = createStyledTextField("Role (user/admin)");

        userForm.add(createStyledLabel("Username:"), 0, 0);
        userForm.add(usernameField, 1, 0);
        userForm.add(createStyledLabel("Password:"), 0, 1);
        userForm.add(passwordField, 1, 1);
        userForm.add(createStyledLabel("Role:"), 0, 2);
        userForm.add(roleField, 1, 2);

        // User Buttons
        HBox userButtons = new HBox(10);
        userButtons.setAlignment(Pos.CENTER);
        userButtons.setPadding(new Insets(10));
        Button addUserBtn = createStyledButton("Add User", "#4CAF50");
        Button updateUserBtn = createStyledButton("Update User", "#2196F3");
        Button deleteUserBtn = createStyledButton("Delete User", "#f44336");
        Button clearUserFormBtn = createStyledButton("Clear Form", "#9E9E9E");
        userButtons.getChildren().addAll(addUserBtn, updateUserBtn, deleteUserBtn, clearUserFormBtn);

        // Event Handlers for Users (Basic Implementation)
        addUserBtn.setOnAction(e -> addUser());
        updateUserBtn.setOnAction(e -> updateUser());
        deleteUserBtn.setOnAction(e -> deleteUser());
        clearUserFormBtn.setOnAction(e -> clearUserForm());

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                usernameField.setText(newSelection.getUsername());
                passwordField.setText(newSelection.getPassword()); // Note: In real app, never display plain password
                roleField.setText(newSelection.getRole());
            } else {
                clearUserForm();
            }
        });

        VBox userTabContent = new VBox(10);
        userTabContent.setPadding(new Insets(20));
        userTabContent.getChildren().addAll(userTable, userForm, userButtons);
        return userTabContent;
    }

    // Helper Methods for UI Components
    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle("-fx-background-color: #4a4a4a; -fx-text-fill: white; -fx-prompt-text-fill: #888888;");
        return field;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        return label;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format(
            "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-padding: 10 20; -fx-background-radius: 5;", color));
        return button;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- Book Management CRUD Operations and DB Interaction ---
    private void loadBooksFromDatabase() {
        bookList.clear();
        String sql = "SELECT id, title, author, isbn, genre, available FROM books";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                bookList.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("genre"),
                        rs.getInt("available")
                ));
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load books: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void addBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String genre = genreComboBox.getValue();
        int available = availableCheckBox.isSelected() ? 1 : 0;

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || genre == null) {
            showAlert("Validation Error", "All fields must be filled out.");
            return;
        }

        // Validate ISBN format
        if (!isValidISBN(isbn)) {
            showAlert("Validation Error", "Please enter a valid ISBN-10 or ISBN-13 number.");
            return;
        }

        String sql = "INSERT INTO books(title, author, isbn, genre, available) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, isbn);
            pstmt.setString(4, genre);
            pstmt.setInt(5, available);
            pstmt.executeUpdate();
            showAlert("Success", "Book added successfully!");
            loadBooksFromDatabase();
            clearBookForm();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to add book: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private boolean isValidISBN(String isbn) {
        // Remove hyphens and spaces
        isbn = isbn.replaceAll("[\\s-]", "");
        
        // Check if it's ISBN-10 or ISBN-13
        if (isbn.length() == 10) {
            return isValidISBN10(isbn);
        } else if (isbn.length() == 13) {
            return isValidISBN13(isbn);
        }
        return false;
    }

    private boolean isValidISBN10(String isbn) {
        if (isbn.length() != 10) return false;
        
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            if (digit < 0 || digit > 9) return false;
            sum += (digit * (10 - i));
        }
        
        char lastChar = isbn.charAt(9);
        if (lastChar == 'X' || lastChar == 'x') {
            sum += 10;
        } else {
            int digit = Character.getNumericValue(lastChar);
            if (digit < 0 || digit > 9) return false;
            sum += digit;
        }
        
        return sum % 11 == 0;
    }

    private boolean isValidISBN13(String isbn) {
        if (isbn.length() != 13) return false;
        
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            if (digit < 0 || digit > 9) return false;
            sum += (digit * (i % 2 == 0 ? 1 : 3));
        }
        
        return sum % 10 == 0;
    }

    private void updateBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Selection Error", "Please select a book to update.");
            return;
        }

        String title = titleField.getText();
        String author = authorField.getText();
        String isbn = isbnField.getText();
        String genre = genreComboBox.getValue();
        int available = availableCheckBox.isSelected() ? 1 : 0;

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || genre == null) {
            showAlert("Validation Error", "All fields must be filled out.");
            return;
        }

        // Validate ISBN format
        if (!isValidISBN(isbn)) {
            showAlert("Validation Error", "Please enter a valid ISBN-10 or ISBN-13 number.");
            return;
        }

        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, genre = ?, available = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, isbn);
            pstmt.setString(4, genre);
            pstmt.setInt(5, available);
            pstmt.setInt(6, selectedBook.getId());
            pstmt.executeUpdate();
            showAlert("Success", "Book updated successfully!");
            loadBooksFromDatabase();
            clearBookForm();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update book: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void deleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert("Selection Error", "Please select a book to delete.");
            return;
        }

        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, selectedBook.getId());
            pstmt.executeUpdate();
            showAlert("Success", "Book deleted successfully!");
            loadBooksFromDatabase();
            clearBookForm();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to delete book: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void clearBookForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        genreComboBox.setValue(null);
        availableCheckBox.setSelected(true);
        bookTable.getSelectionModel().clearSelection();
    }

    // --- User Management CRUD Operations and DB Interaction ---
    private void loadUsersFromDatabase() {
        userList.clear();
        String sql = "SELECT id, username, password, role FROM users"; // Include password for now, but handle securely later
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                userList.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load users: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void addUser() {
        String username = usernameField.getText();
        String password = passwordField.getText(); // In a real app, hash this password!
        String role = roleField.getText();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showAlert("Validation Error", "Username, Password, and Role cannot be empty.");
            return;
        }

        String sql = "INSERT INTO users(username, password, role) VALUES(?,?,?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            showAlert("Success", "User added successfully!");
            loadUsersFromDatabase(); // Reload data after adding
            clearUserForm();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to add user: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void updateUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Selection Error", "Please select a user to update.");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            showAlert("Validation Error", "Username, Password, and Role cannot be empty.");
            return;
        }

        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setInt(4, selectedUser.getId());
            pstmt.executeUpdate();
            showAlert("Success", "User updated successfully!");
            loadUsersFromDatabase(); // Reload data after updating
            clearUserForm();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to update user: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void deleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Selection Error", "Please select a user to delete.");
            return;
        }

        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, selectedUser.getId());
            pstmt.executeUpdate();
            showAlert("Success", "User deleted successfully!");
            loadUsersFromDatabase(); // Reload data after deleting
            clearUserForm();
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to delete user: " + e.getMessage());
            System.err.println(e.getMessage());
        }
    }

    private void clearUserForm() {
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
        userTable.getSelectionModel().clearSelection();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 