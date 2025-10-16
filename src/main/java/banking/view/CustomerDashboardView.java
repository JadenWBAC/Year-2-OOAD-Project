package banking.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerDashboardView {
    private Stage stage;

    public CustomerDashboardView(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Customer Dashboard");
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // Top bar
        HBox topBar = createTopBar();

        // Center content with tabs for different functionalities
        TabPane tabPane = new TabPane();

        // Accounts Tab
        Tab accountsTab = new Tab("My Accounts");
        accountsTab.setContent(createAccountsView());
        accountsTab.setClosable(false);

        // Transactions Tab
        Tab transactionsTab = new Tab("Transactions");
        transactionsTab.setContent(createTransactionsView());
        transactionsTab.setClosable(false);

        // Profile Tab
        Tab profileTab = new Tab("My Profile");
        profileTab.setContent(createProfileView());
        profileTab.setClosable(false);

        tabPane.getTabs().addAll(accountsTab, transactionsTab, profileTab);

        layout.setTop(topBar);
        layout.setCenter(tabPane);

        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #27ae60;");

        Label titleLabel = new Label("My Accounts");
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
        return topBar;
    }

    private VBox createAccountsView() {
        VBox accountsBox = new VBox(15);
        accountsBox.setPadding(new Insets(20));
        accountsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("My Accounts");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Account data display
        VBox accountsList = new VBox(10);
        accountsList.getChildren().addAll(
                createAccountCard("Savings Account", "ACC001", 1500.00),
                createAccountCard("Investment Account", "ACC002", 5000.00),
                createAccountCard("Checking Account", "ACC003", 2500.00)
        );

        // Action buttons
        HBox buttonBox = new HBox(10);
        Button depositBtn = new Button("Make Deposit");
        Button withdrawBtn = new Button("Make Withdrawal");
        Button transferBtn = new Button("Transfer Funds");

        buttonBox.getChildren().addAll(depositBtn, withdrawBtn, transferBtn);

        accountsBox.getChildren().addAll(titleLabel, accountsList, buttonBox);
        return accountsBox;
    }

    private HBox createAccountCard(String accountType, String accountNumber, double balance) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label typeLabel = new Label(accountType);
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label numberLabel = new Label("Account: " + accountNumber);
        Label balanceLabel = new Label(String.format("Balance: P%.2f", balance));
        balanceLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        VBox details = new VBox(5);
        details.getChildren().addAll(typeLabel, numberLabel, balanceLabel);

        Button viewBtn = new Button("View Details");

        card.getChildren().addAll(details, new Region(), viewBtn);
        HBox.setHgrow(card.getChildren().get(1), Priority.ALWAYS);

        return card;
    }

    private VBox createTransactionsView() {
        VBox transactionsBox = new VBox(15);
        transactionsBox.setPadding(new Insets(20));
        transactionsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Transaction History");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label infoLabel = new Label("Transaction history will be displayed here...");

        transactionsBox.getChildren().addAll(titleLabel, infoLabel);
        return transactionsBox;
    }

    private VBox createProfileView() {
        VBox profileBox = new VBox(15);
        profileBox.setPadding(new Insets(20));
        profileBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("My Profile");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label infoLabel = new Label("Profile information will be displayed here...");

        profileBox.getChildren().addAll(titleLabel, infoLabel);
        return profileBox;
    }
}