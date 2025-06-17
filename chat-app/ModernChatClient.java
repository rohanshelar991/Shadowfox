import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class ModernChatClient extends JFrame {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5001;
    private JTextPane chatPane;
    private JTextField messageField;
    private JButton sendButton;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private String userColor;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private String currentRoom = "Lobby";
    private DefaultListModel<String> roomListModel;
    private JList<String> roomList;

    public ModernChatClient() {
        setupGUI();
        connectToServer();
    }

    private void setupGUI() {
        setTitle("Modern Chat - Real-time Rooms");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));

        JPanel mainPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(36, 37, 130);
                Color color2 = new Color(255, 107, 107);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        // Sidebar for users and rooms
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(new Color(36, 37, 130, 220));

        JLabel usersLabel = new JLabel(" Users", JLabel.LEFT);
        usersLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        usersLabel.setForeground(Color.WHITE);
        usersLabel.setBorder(new EmptyBorder(10, 10, 0, 0));
        sidebar.add(usersLabel, BorderLayout.NORTH);

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userList.setBackground(new Color(255, 255, 255, 180));
        userList.setForeground(new Color(36, 37, 130));
        userList.setSelectionBackground(new Color(255, 107, 107, 120));
        sidebar.add(new JScrollPane(userList), BorderLayout.CENTER);

        // Room list
        JPanel roomPanel = new JPanel(new BorderLayout());
        roomPanel.setOpaque(false);
        JLabel roomsLabel = new JLabel(" Rooms", JLabel.LEFT);
        roomsLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        roomsLabel.setForeground(Color.WHITE);
        roomsLabel.setBorder(new EmptyBorder(10, 10, 0, 0));
        roomPanel.add(roomsLabel, BorderLayout.NORTH);
        roomListModel = new DefaultListModel<>();
        roomList = new JList<>(roomListModel);
        roomList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roomList.setBackground(new Color(255, 255, 255, 180));
        roomList.setForeground(new Color(36, 37, 130));
        roomList.setSelectionBackground(new Color(255, 107, 107, 120));
        roomPanel.add(new JScrollPane(roomList), BorderLayout.CENTER);
        sidebar.add(roomPanel, BorderLayout.SOUTH);
        mainPanel.add(sidebar, BorderLayout.WEST);

        // Chat area
        chatPane = new JTextPane();
        chatPane.setEditable(false);
        chatPane.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        chatPane.setBackground(new Color(255, 255, 255, 220));
        chatPane.setForeground(new Color(44, 44, 44));
        JScrollPane scrollPane = new JScrollPane(chatPane);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setOpaque(false);
        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageField.setBackground(new Color(255, 255, 255, 220));
        messageField.setForeground(new Color(44, 44, 44));
        messageField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sendButton.setBackground(new Color(255, 107, 107));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorderPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        add(mainPanel);
    }

    private void connectToServer() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            username = JOptionPane.showInputDialog(this, "Enter your name:", "Welcome to Modern Chat", JOptionPane.PLAIN_MESSAGE);
            if (username == null || username.trim().isEmpty()) {
                username = "User" + System.currentTimeMillis() % 1000;
            }
            out.println(username);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        final String finalMessage = message;
                        if (finalMessage.startsWith("COLOR:")) {
                            userColor = finalMessage.substring(6);
                        } else if (finalMessage.startsWith("USERS:")) {
                            updateUserList(finalMessage.substring(6));
                        } else if (finalMessage.startsWith("ROOMS:")) {
                            updateRoomList(finalMessage.substring(6));
                        } else {
                            SwingUtilities.invokeLater(() -> appendMessage(finalMessage));
                        }
                    }
                } catch (IOException e) {
                    appendMessage("Error: Lost connection to server");
                }
            }).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not connect to server: " + e.getMessage(),
                "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            out.println(message);
            messageField.setText("");
        }
    }

    private void appendMessage(String message) {
        Pattern pattern = Pattern.compile("(\\d{2}:\\d{2}:\\d{2}) \\[(#[0-9A-Fa-f]+)\\] (.*?): (.*)");
        Matcher matcher = pattern.matcher(message);
        try {
            if (matcher.matches()) {
                String timestamp = matcher.group(1);
                String color = matcher.group(2);
                String sender = matcher.group(3);
                String content = matcher.group(4);
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, Color.GRAY);
                chatPane.getDocument().insertString(chatPane.getDocument().getLength(), "[" + timestamp + "] ", attr);
                if (sender.equals("SYSTEM")) {
                    StyleConstants.setForeground(attr, Color.GRAY);
                    StyleConstants.setBold(attr, true);
                    chatPane.getDocument().insertString(chatPane.getDocument().getLength(), content + "\n", attr);
                } else {
                    StyleConstants.setForeground(attr, Color.decode(color));
                    StyleConstants.setBold(attr, true);
                    chatPane.getDocument().insertString(chatPane.getDocument().getLength(), sender, attr);
                    StyleConstants.setForeground(attr, Color.DARK_GRAY);
                    StyleConstants.setBold(attr, false);
                    chatPane.getDocument().insertString(chatPane.getDocument().getLength(), ": " + content + "\n", attr);
                }
            } else {
                SimpleAttributeSet attr = new SimpleAttributeSet();
                StyleConstants.setForeground(attr, Color.GRAY);
                chatPane.getDocument().insertString(chatPane.getDocument().getLength(), message + "\n", attr);
            }
            chatPane.setCaretPosition(chatPane.getDocument().getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUserList(String users) {
        SwingUtilities.invokeLater(() -> {
            userListModel.clear();
            for (String user : users.split(",")) {
                if (!user.trim().isEmpty()) userListModel.addElement(user.trim());
            }
        });
    }

    private void updateRoomList(String rooms) {
        SwingUtilities.invokeLater(() -> {
            roomListModel.clear();
            for (String room : rooms.split(",")) {
                if (!room.trim().isEmpty()) roomListModel.addElement(room.trim());
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            new ModernChatClient().setVisible(true);
        });
    }
} 