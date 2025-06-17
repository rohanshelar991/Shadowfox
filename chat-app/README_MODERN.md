# Modern Chat Application

A beautiful and feature-rich chat application with a modern graphical user interface built using Java Swing and Socket Programming.

## Features

- Modern and attractive GUI with gradient background
- Real-time messaging
- Colored usernames for better message distinction
- Timestamp for each message
- System notifications for user join/leave events
- Command support (/users to see online users)
- Smooth scrolling and message history
- User-friendly interface with modern design elements

## Requirements

- Java Development Kit (JDK) 8 or higher
- Basic knowledge of using Java applications

## How to Run

1. First, compile both Java files:
   ```bash
   javac ModernChatServer.java
   javac ModernChatClient.java
   ```

2. Start the server in one terminal window:
   ```bash
   java ModernChatServer
   ```

3. Start the client application:
   ```bash
   java ModernChatClient
   ```

4. For each new client:
   - Run `java ModernChatClient` in a new terminal
   - Enter your name when prompted
   - Start chatting!

## Usage

1. The server runs on port 5001
2. When you start the client, you'll be prompted to enter your name
3. The chat window features:
   - A large message area showing the chat history
   - A text field at the bottom for typing messages
   - A send button to send messages
   - Press Enter to send messages quickly
4. Each user gets a unique color for their messages
5. System messages appear in gray
6. Type `/users` to see the list of online users

## Design Features

- Gradient background with modern color scheme
- Semi-transparent message area
- Modern font (Segoe UI)
- Smooth animations and transitions
- Responsive layout
- Professional-looking buttons and input fields

## Notes

- Make sure to start the server before starting any clients
- The server must be running on the same machine as the clients (localhost)
- If you want to connect from different machines, modify the SERVER_IP in ModernChatClient.java 