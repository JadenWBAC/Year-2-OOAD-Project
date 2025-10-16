package banking.view;

import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginView {
    private Stage stage;
    private TextField usernameField;
    private PasswordField passwordField;
    private ComboBox<String> userTypeCombo;
    private Label messageLabel;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Banking System - Login");
    }

    public void show() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setStyle("-fx-background-color: #f5f5f5;");

        Label titleLabel = new Label("Banking System Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        VBox formContainer = new VBox(15);
        formContainer.setMaxWidth(400);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        Label userTypeLabel = new Label("Login As:");
        userTypeCombo = new ComboBox<>();
        userTypeCombo.getItems().addAll("Customer", "Bank Teller");
        userTypeCombo.setValue("Customer");
        userTypeCombo.setMaxWidth(Double.MAX_VALUE);

        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        messageLabel = new Label();
        messageLabel.setVisible(false);

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-background-color: #27ae60; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10 30; " +
                        "-fx-background-radius: 5;"
        );
        loginButton.setOnAction(e -> handleLogin());

        Button clearButton = new Button("Clear");
        clearButton.setStyle(
                "-fx-background-color: #e74c3c; " +
                        "-fx-text-fill: white; " +
                        "-fx-padding: 10 30; " +
                        "-fx-background-radius: 5;"
        );
        clearButton.setOnAction(e -> clearForm());

        formContainer.getChildren().addAll(
                userTypeLabel, userTypeCombo,
                usernameLabel, usernameField,
                passwordLabel, passwordField,
                messageLabel
        );

        buttonContainer.getChildren().addAll(loginButton, clearButton);
        mainContainer.getChildren().addAll(titleLabel, formContainer, buttonContainer);

        Scene scene = new Scene(mainContainer, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String userType = userTypeCombo.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please fill in all fields", false);
            return;
        }

        if (password.length() < 4) {
            showMessage("Password must be at least 4 characters", false);
            return;
        }

        // Simulate authentication (I'll replace this with real auth later)
        if (authenticateUser(username, password, userType)) {
            showMessage("Login successful!", true);

            // Small delay before switching views
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                if (userType.equals("Bank Teller")) {
                    TellerDashboardView dashboard = new TellerDashboardView(stage);
                    dashboard.show();
                } else {
                    CustomerDashboardView dashboard = new CustomerDashboardView(stage);
                    dashboard.show();
                }
            });
            pause.play();
        } else {
            showMessage("Invalid username or password", false);
        }
    }

    private boolean authenticateUser(String username, String password, String userType) {
        // Temporary simulation - replace with real authentication later
        return username.equals("Jacob") && password.equals("123445") && userType.equals("Customer") ||
                username.equals("Jaden") && password.equals("021103") && userType.equals("Bank Teller") ||
                (username.equals("Admin") && password.equals("admin123") && userType.equals("Bank Teller")) ||
                (username.equals("Theo") && password.equals("Theo2024") && userType.equals("Customer"));
    }

    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        messageLabel.setVisible(false);
    }

    private void showMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " +
                (isSuccess ? "#27ae60" : "#e74c3c") + ";");
        messageLabel.setVisible(true);
    }
}