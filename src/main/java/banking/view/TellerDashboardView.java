package banking.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TellerDashboardView {
    private Stage stage;

    public TellerDashboardView(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Teller Dashboard");
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // Top bar
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        Label titleLabel = new Label("Teller Dashboard");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        topBar.getChildren().addAll(titleLabel, spacer, logoutButton);

        // Center content
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(40));
        centerContent.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome, Teller!");
        welcomeLabel.setStyle("-fx-font-size: 18px;");

        Label infoLabel = new Label("More features will be added here...");

        centerContent.getChildren().addAll(welcomeLabel, infoLabel);

        layout.setTop(topBar);
        layout.setCenter(centerContent);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}