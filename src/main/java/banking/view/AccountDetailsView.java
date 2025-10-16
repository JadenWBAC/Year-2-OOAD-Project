package banking.view;
import banking.model.Transaction;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AccountDetailsView {
    private Stage stage;

    public AccountDetailsView(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Account Details");
    }

    public void show(String accountNumber) {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // Top bar with back button
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #3498db;");

        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-text-fill: white;");
        backButton.setOnAction(e -> {
            CustomerDashboardView dashboard = new CustomerDashboardView(stage);
            dashboard.show();
        });

        Label titleLabel = new Label("Account Details: " + accountNumber);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(backButton, titleLabel, spacer);

        // Center content
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.TOP_LEFT);

        // Account information
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(15));
        infoGrid.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        infoGrid.add(new Label("Account Type:"), 0, 0);
        infoGrid.add(new Label("Savings Account"), 1, 0);

        infoGrid.add(new Label("Account Number:"), 0, 1);
        infoGrid.add(new Label(accountNumber), 1, 1);

        infoGrid.add(new Label("Current Balance:"), 0, 2);
        Label balanceLabel = new Label("P1,500.00");
        balanceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        infoGrid.add(balanceLabel, 1, 2);

        infoGrid.add(new Label("Interest Rate:"), 0, 3);
        infoGrid.add(new Label("0.025% monthly"), 1, 3);

        // Transaction history
        Label historyLabel = new Label("Recent Transactions");
        historyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<Transaction> transactionTable = new TableView<>();

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        TableColumn<Transaction, String> descriptionCol = new TableColumn<>("Description");
        TableColumn<Transaction, String> amountCol = new TableColumn<>("Amount");
        TableColumn<Transaction, String> balanceCol = new TableColumn<>("Balance");

        transactionTable.getColumns().addAll(dateCol, descriptionCol, amountCol, balanceCol);

        // Quick actions
        HBox actionBox = new HBox(10);
        Button depositBtn = new Button("Deposit");
        Button withdrawBtn = new Button("Withdraw");
        Button statementBtn = new Button("Print Statement");

        actionBox.getChildren().addAll(depositBtn, withdrawBtn, statementBtn);

        centerContent.getChildren().addAll(infoGrid, historyLabel, transactionTable, actionBox);

        layout.setTop(topBar);
        layout.setCenter(centerContent);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}