package com.shadowfox;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Rectangle;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import com.shadowfox.GradientPane;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.effect.Glow;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.SepiaTone;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.Shadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.DisplacementMap;
import javafx.scene.effect.FloatMap;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.effect.ImageInput;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.MotionBlur;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.effect.Reflection;
import javafx.scene.effect.SepiaTone;
import javafx.scene.effect.Shadow;

public class ModernBankSystem extends Application {
    private double balance = 1000.0;
    private TextField amountField;
    private Label balanceLabel;
    private VBox transactionHistory;
    private Circle profileCircle;
    private Label welcomeLabel;
    private VBox mainContainer;
    private HBox quickActionsBox;
    private ScrollPane transactionScrollPane;
    private VBox notificationBox;
    private int notificationCount = 0;

    @Override
    public void start(Stage primaryStage) {
        // Create main container with animated gradient background
        mainContainer = new VBox(20);
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #3498db, #2980b9);");
        mainContainer.setPadding(new Insets(20));
        mainContainer.setAlignment(Pos.CENTER);

        // Create profile section
        HBox profileBox = createProfileSection();
        
        // Create welcome message with animation
        welcomeLabel = new Label("Welcome Back!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setEffect(new DropShadow(10, Color.BLACK));
        
        // Animate welcome message
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), welcomeLabel);
        st.setFromX(0.5);
        st.setFromY(0.5);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();

        // Create balance display with glass effect and animation
        Rectangle balanceCard = new Rectangle(300, 150);
        balanceCard.setArcWidth(20);
        balanceCard.setArcHeight(20);
        balanceCard.setFill(Color.rgb(255, 255, 255, 0.2));
        balanceCard.setEffect(new DropShadow(10, Color.BLACK));
        
        // Add hover effect to balance card
        balanceCard.setOnMouseEntered(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), balanceCard);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();
        });
        
        balanceCard.setOnMouseExited(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), balanceCard);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });

        VBox balanceBox = new VBox(10);
        balanceBox.setAlignment(Pos.CENTER);
        Label balanceTitle = new Label("Current Balance");
        balanceTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        balanceTitle.setTextFill(Color.WHITE);
        
        balanceLabel = new Label(String.format("$%.2f", balance));
        balanceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        balanceLabel.setTextFill(Color.WHITE);
        
        balanceBox.getChildren().addAll(balanceTitle, balanceLabel);
        balanceBox.setPadding(new Insets(20));

        // Create quick action buttons
        quickActionsBox = createQuickActions();

        // Create transaction controls with modern styling
        HBox controlsBox = new HBox(20);
        controlsBox.setAlignment(Pos.CENTER);
        
        Button depositBtn = createStyledButton("Deposit", "#2ecc71");
        Button withdrawBtn = createStyledButton("Withdraw", "#e74c3c");
        Button transferBtn = createStyledButton("Transfer", "#3498db");
        
        controlsBox.getChildren().addAll(depositBtn, withdrawBtn, transferBtn);

        // Create amount input field with modern styling
        amountField = createStyledTextField("Enter amount");
        amountField.setMaxWidth(200);

        // Create transaction history with scroll pane
        transactionScrollPane = new ScrollPane();
        transactionScrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        transactionScrollPane.setMaxHeight(200);
        
        transactionHistory = new VBox(10);
        transactionHistory.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-padding: 10; -fx-background-radius: 10;");
        transactionScrollPane.setContent(transactionHistory);

        // Create notification box
        notificationBox = new VBox(10);
        notificationBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-padding: 10; -fx-background-radius: 10;");
        notificationBox.setMaxHeight(100);

        // Add all components to main container
        mainContainer.getChildren().addAll(
            profileBox,
            welcomeLabel,
            balanceCard,
            balanceBox,
            quickActionsBox,
            amountField,
            controlsBox,
            new Label("Transaction History"),
            transactionScrollPane,
            notificationBox
        );

        // Set up event handlers
        depositBtn.setOnAction(e -> handleTransaction(true));
        withdrawBtn.setOnAction(e -> handleTransaction(false));
        transferBtn.setOnAction(e -> showTransferDialog());

        // Create and show scene
        Scene scene = new Scene(mainContainer, 800, 800);
        primaryStage.setTitle("Modern Banking System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add initial animation
        addInitialAnimation();
    }

    private HBox createProfileSection() {
        HBox profileBox = new HBox(20);
        profileBox.setAlignment(Pos.CENTER_LEFT);
        
        // Create profile circle with image
        profileCircle = new Circle(30);
        profileCircle.setFill(Color.WHITE);
        profileCircle.setEffect(new DropShadow(10, Color.BLACK));
        
        // Add hover effect
        profileCircle.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), profileCircle);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
        });
        
        profileCircle.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), profileCircle);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        // Create profile info
        VBox profileInfo = new VBox(5);
        Label nameLabel = new Label("Rohan Shelar");
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        nameLabel.setTextFill(Color.WHITE);
        
        Label accountLabel = new Label("Account: 1234 5678 9012 ");
        accountLabel.setFont(Font.font("Arial", 12));
        accountLabel.setTextFill(Color.WHITE);

        profileInfo.getChildren().addAll(nameLabel, accountLabel);
        profileBox.getChildren().addAll(profileCircle, profileInfo);
        
        return profileBox;
    }

    private HBox createQuickActions() {
        // Return an empty HBox since we are removing the quick action buttons
        HBox quickActions = new HBox();
        quickActions.setAlignment(Pos.CENTER);
        return quickActions;
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setMaxWidth(200);
        field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                      "-fx-text-fill: white; " +
                      "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7); " +
                      "-fx-background-radius: 20; " +
                      "-fx-padding: 10 20;");
        
        // Add focus effect
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.3); " +
                             "-fx-text-fill: white; " +
                             "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7); " +
                             "-fx-background-radius: 20; " +
                             "-fx-padding: 10 20;");
            } else {
                field.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2); " +
                             "-fx-text-fill: white; " +
                             "-fx-prompt-text-fill: rgba(255, 255, 255, 0.7); " +
                             "-fx-background-radius: 20; " +
                             "-fx-padding: 10 20;");
            }
        });
        
        return field;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(String.format(
            "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
            "-fx-padding: 10 20; -fx-background-radius: 20;", color));
        
        // Add hover effect
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.1);
            st.setToY(1.1);
            st.play();
            button.setStyle(String.format(
                "-fx-background-color: derive(%s, 20%%); -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10 20; -fx-background-radius: 20;", color));
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-padding: 10 20; -fx-background-radius: 20;", color));
        });
        
        return button;
    }

    private void handleTransaction(boolean isDeposit) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showNotification("Invalid Amount", "Please enter a positive amount.", "error");
                return;
            }

            if (isDeposit) {
                balance += amount;
                addTransactionToHistory("Deposit", amount);
                showNotification("Success", "Amount deposited successfully!", "success");
            } else {
                if (amount > balance) {
                    showNotification("Insufficient Funds", "You don't have enough balance.", "error");
                    return;
                }
                balance -= amount;
                addTransactionToHistory("Withdrawal", amount);
                showNotification("Success", "Amount withdrawn successfully!", "success");
            }

            updateBalanceDisplay();
            amountField.clear();

        } catch (NumberFormatException e) {
            showNotification("Invalid Input", "Please enter a valid number.", "error");
        }
    }

    private void updateBalanceDisplay() {
        // Animate balance update
        FadeTransition ft = new FadeTransition(Duration.millis(500), balanceLabel);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        
        balanceLabel.setText(String.format("$%.2f", balance));
    }

    private void addTransactionToHistory(String type, double amount) {
        HBox transaction = new HBox(10);
        transaction.setAlignment(Pos.CENTER_LEFT);
        transaction.setStyle("-fx-background-color: rgba(255, 255, 255, 0.1); -fx-padding: 10; -fx-background-radius: 10;");
        
        Label typeLabel = new Label(type);
        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        typeLabel.setTextFill(Color.WHITE);
        
        Label amountLabel = new Label(String.format("$%.2f", amount));
        amountLabel.setFont(Font.font("Arial", 14));
        amountLabel.setTextFill(type.equals("Deposit") ? Color.LIGHTGREEN : Color.LIGHTCORAL);
        
        Label dateLabel = new Label(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("MM/dd HH:mm")));
        dateLabel.setFont(Font.font("Arial", 12));
        dateLabel.setTextFill(Color.LIGHTGRAY);
        
        transaction.getChildren().addAll(typeLabel, amountLabel, dateLabel);
        
        // Add animation
        transaction.setOpacity(0);
        transactionHistory.getChildren().add(0, transaction);
        
        FadeTransition ft = new FadeTransition(Duration.millis(500), transaction);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void showNotification(String title, String message, String type) {
        HBox notification = new HBox(10);
        notification.setAlignment(Pos.CENTER_LEFT);
        notification.setStyle(String.format(
            "-fx-background-color: %s; -fx-padding: 10; -fx-background-radius: 10;",
            type.equals("error") ? "rgba(231, 76, 60, 0.9)" : "rgba(46, 204, 113, 0.9)"
        ));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.WHITE);
        
        Label messageLabel = new Label(message);
        messageLabel.setFont(Font.font("Arial", 12));
        messageLabel.setTextFill(Color.WHITE);
        
        notification.getChildren().addAll(titleLabel, messageLabel);
        
        // Add animation
        notification.setOpacity(0);
        notificationBox.getChildren().add(0, notification);
        
        FadeTransition ft = new FadeTransition(Duration.millis(500), notification);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        
        // Remove notification after 3 seconds
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                javafx.application.Platform.runLater(() -> {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(500), notification);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> notificationBox.getChildren().remove(notification));
                    fadeOut.play();
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showTransferDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Transfer Money");
        dialog.setHeaderText("Enter transfer details");
        
        // Create custom dialog content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        
        TextField recipientField = createStyledTextField("Recipient Account");
        TextField amountField = createStyledTextField("Amount");
        
        content.getChildren().addAll(recipientField, amountField);
        dialog.getDialogPane().setContent(content);
        
        // Add buttons
        ButtonType transferButtonType = new ButtonType("Transfer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(transferButtonType, ButtonType.CANCEL);
        
        // Show dialog
        dialog.showAndWait().ifPresent(response -> {
            if (response == transferButtonType) {
                // Handle transfer
                showNotification("Transfer Initiated", "Transfer request has been sent.", "success");
            }
        });
    }

    private void addInitialAnimation() {
        // Animate all components on startup
        for (javafx.scene.Node node : mainContainer.getChildren()) {
            node.setOpacity(0);
            FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setDelay(Duration.millis(mainContainer.getChildren().indexOf(node) * 200));
            ft.play();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 