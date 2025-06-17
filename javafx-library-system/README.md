# Library Management System

A modern JavaFX application for managing library resources, built with JavaFX and SQLite.

## Features

- Book Management (Add, Update, Delete, Search)
- User Management
- ISBN Validation
- Genre Categorization
- Data Persistence using SQLite
- Modern UI with JavaFX

## Prerequisites

- Java 21 or higher
- Maven
- JavaFX SDK 24.0.1

## Setup

1. Clone the repository:
```bash
git clone https://github.com/shadow-fox/javafx-library-system.git
cd javafx-library-system
```

2. Download JavaFX SDK 24.0.1 and place it in the `lib` directory:
```bash
mkdir -p lib
# Download JavaFX SDK 24.0.1 and extract to lib/javafx-sdk-24.0.1
```

3. Build and run the project:
```bash
mvn clean compile exec:java -Dprism.order=sw
```

## Project Structure

```
javafx-library-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── shadowfox/
│   │   │           ├── Book.java
│   │   │           ├── DatabaseManager.java
│   │   │           ├── LibraryManagementSystem.java
│   │   │           └── User.java
│   │   └── resources/
│   │       └── styles.css
├── lib/
│   └── javafx-sdk-24.0.1/
├── pom.xml
└── README.md
```

## Dependencies

- JavaFX 24.0.1
- SQLite JDBC
- Maven

## Key Learning

- Data persistence with SQLite, API integration (optional), user management.

## License

MIT License 