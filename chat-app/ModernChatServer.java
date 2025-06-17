import java.io.*;
import java.net.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModernChatServer {
    private static final int PORT = 5001;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Map<String, Set<ClientHandler>> rooms = new HashMap<>();
    private static final Set<ClientHandler> allClients = new HashSet<>();
    private static final Map<String, String> userColors = new HashMap<>();
    private static final String[] COLORS = {"#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEEAD", "#D4A5A5", "#9B59B6", "#F67280", "#355C7D", "#6C5B7B"};
    private static int colorIndex = 0;

    public static void main(String[] args) {
        System.out.println("Modern Chat Server is running on port " + PORT);
        rooms.put("Lobby", new HashSet<>());
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.out.println("Error in the server: " + e.getMessage());
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String name;
        private String color;
        private String currentRoom = "Lobby";

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                name = in.readLine();
                synchronized (ModernChatServer.class) {
                    color = COLORS[colorIndex % COLORS.length];
                    colorIndex++;
                    userColors.put(name, color);
                }
                out.println("COLOR:" + color);
                synchronized (allClients) {
                    allClients.add(this);
                }
                joinRoom(currentRoom);
                broadcastSystem(currentRoom, name + " has joined the chat!");
                sendUserListToAll();
                sendRoomListToAll();
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/join ")) {
                        String newRoom = message.substring(6).trim();
                        if (!rooms.containsKey(newRoom)) {
                            rooms.put(newRoom, new HashSet<>());
                            sendRoomListToAll();
                        }
                        switchRoom(newRoom);
                    } else if (message.equals("/users")) {
                        sendUserList();
                    } else if (message.equals("/rooms")) {
                        sendRoomList();
                    } else {
                        broadcastMessage(currentRoom, name, message, color);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error handling client: " + e.getMessage());
            } finally {
                leaveRoom(currentRoom);
                synchronized (allClients) {
                    allClients.remove(this);
                }
                userColors.remove(name);
                broadcastSystem(currentRoom, name + " has left the chat!");
                sendUserListToAll();
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error closing socket: " + e.getMessage());
                }
            }
        }

        private void joinRoom(String room) {
            synchronized (rooms) {
                rooms.get(room).add(this);
            }
        }

        private void leaveRoom(String room) {
            synchronized (rooms) {
                if (rooms.containsKey(room)) {
                    rooms.get(room).remove(this);
                }
            }
        }

        private void switchRoom(String newRoom) {
            leaveRoom(currentRoom);
            broadcastSystem(currentRoom, name + " has left the room.");
            currentRoom = newRoom;
            joinRoom(currentRoom);
            broadcastSystem(currentRoom, name + " has joined the room.");
            sendUserListToAll();
        }

        private void broadcastMessage(String room, String sender, String message, String color) {
            String timestamp = LocalDateTime.now().format(formatter);
            String formattedMessage = String.format("%s [%s] %s: %s", timestamp, color, sender, message);
            synchronized (rooms) {
                for (ClientHandler client : rooms.get(room)) {
                    client.out.println(formattedMessage);
                }
            }
        }

        private void broadcastSystem(String room, String message) {
            String timestamp = LocalDateTime.now().format(formatter);
            String formattedMessage = String.format("%s [#808080] SYSTEM: %s", timestamp, message);
            synchronized (rooms) {
                for (ClientHandler client : rooms.get(room)) {
                    client.out.println(formattedMessage);
                }
            }
        }

        private void sendUserList() {
            StringBuilder sb = new StringBuilder();
            synchronized (rooms) {
                for (ClientHandler client : rooms.get(currentRoom)) {
                    sb.append(client.name).append(",");
                }
            }
            out.println("USERS:" + sb.toString());
        }

        private void sendUserListToAll() {
            StringBuilder sb = new StringBuilder();
            synchronized (rooms) {
                for (ClientHandler client : rooms.get(currentRoom)) {
                    sb.append(client.name).append(",");
                }
                for (ClientHandler client : rooms.get(currentRoom)) {
                    client.out.println("USERS:" + sb.toString());
                }
            }
        }

        private void sendRoomList() {
            StringBuilder sb = new StringBuilder();
            synchronized (rooms) {
                for (String room : rooms.keySet()) {
                    sb.append(room).append(",");
                }
            }
            out.println("ROOMS:" + sb.toString());
        }

        private void sendRoomListToAll() {
            StringBuilder sb = new StringBuilder();
            synchronized (rooms) {
                for (String room : rooms.keySet()) {
                    sb.append(room).append(",");
                }
                for (ClientHandler client : allClients) {
                    client.out.println("ROOMS:" + sb.toString());
                }
            }
        }
    }
} 