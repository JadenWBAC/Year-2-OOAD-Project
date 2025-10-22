package banking.view;

import banking.controller.CustomerDashboardController;
import banking.model.Account;
import banking.model.Customer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CustomerDashboardView {
    private Stage stage;
    private CustomerDashboardController controller;

    public CustomerDashboardView(Stage stage, CustomerDashboardController controller) {
        this.stage = stage;
        this.controller = controller;
        this.stage.setTitle("Customer Dashboard - " + controller.getCustomer().getName());
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // Top bar
        HBox topBar = createTopBar();

        // Center content with tabs
        TabPane tabPane = new TabPane();

        Tab accountsTab = new Tab("My Accounts");
        accountsTab.setContent(createAccountsView());
        accountsTab.setClosable(false);

        Tab transactionsTab = new Tab("Transactions");
        transactionsTab.setContent(createTransactionsView());
        transactionsTab.setClosable(false);

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

        Label titleLabel = new Label("Welcome, " + controller.getCustomer().getName());
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

        // Account data display - using real customer accounts
        VBox accountsList = new VBox(10);
        for (Account account : controller.getCustomer().getAccounts()) {
            accountsList.getChildren().add(createAccountCard(account));
        }

        // Action buttons
        HBox buttonBox = new HBox(10);
        Button depositBtn = new Button("Make Deposit");
        depositBtn.setOnAction(e -> showDepositDialog());

        Button withdrawBtn = new Button("Make Withdrawal");
        withdrawBtn.setOnAction(e -> showWithdrawDialog());

        Button transferBtn = new Button("Transfer Funds");
        transferBtn.setOnAction(e -> showTransferDialog());

        buttonBox.getChildren().addAll(depositBtn, withdrawBtn, transferBtn);

        accountsBox.getChildren().addAll(titleLabel, accountsList, buttonBox);
        return accountsBox;
    }

    private HBox createAccountCard(Account account) {
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        card.setAlignment(Pos.CENTER_LEFT);

        Label typeLabel = new Label(account.getClass().getSimpleName());
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label numberLabel = new Label("Account: " + account.getAccountNumber());
        Label balanceLabel = new Label(String.format("Balance: P%.2f", account.getBalance()));
        balanceLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        VBox details = new VBox(5);
        details.getChildren().addAll(typeLabel, numberLabel, balanceLabel);

        Button viewBtn = new Button("View Details");
        viewBtn.setOnAction(e -> controller.handleViewAccountDetails(account.getAccountNumber()));

        card.getChildren().addAll(details, new Region(), viewBtn);
        HBox.setHgrow(card.getChildren().get(1), Priority.ALWAYS);

        return card;
    }

    private void showDepositDialog() {
        // Create a custom dialog with account selection
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Deposit Funds");
        dialog.setHeaderText("Select account and enter amount:");

        // Create account selection
        ComboBox<String> accountCombo = new ComboBox<>();
        for (Account account : controller.getCustomer().getAccounts()) {
            accountCombo.getItems().add(account.getAccountNumber() + " - " +
                    account.getClass().getSimpleName() + " (P" + account.getBalance() + ")");
        }

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Account:"), 0, 0);
        grid.add(accountCombo, 1, 0);
        grid.add(new Label("Amount:"), 0, 1);
        grid.add(amountField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK && accountCombo.getValue() != null) {
                try {
                    String accountNumber = accountCombo.getValue().split(" - ")[0];
                    double amount = Double.parseDouble(amountField.getText());
                    controller.handleDeposit(accountNumber, amount);
                    refreshView(); // Refresh to show updated balances
                } catch (NumberFormatException e) {
                    showAlert("Invalid amount", "Please enter a valid number");
                }
            }
        });
    }

    private void refreshView() {
        // Refresh the current view to show updated account balances
        show();
    }

    private void showWithdrawDialog() {
        // Similar implementation to deposit dialog
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Withdraw Funds");
        dialog.setHeaderText("Enter withdrawal details");
        dialog.setContentText("Account Number:");

        dialog.showAndWait().ifPresent(accountNumber -> {
            TextInputDialog amountDialog = new TextInputDialog();
            amountDialog.setTitle("Withdraw Amount");
            amountDialog.setHeaderText("Enter amount to withdraw:");
            amountDialog.setContentText("Amount:");

            amountDialog.showAndWait().ifPresent(amountStr -> {
                try {
                    double amount = Double.parseDouble(amountStr);
                    controller.handleWithdraw(accountNumber, amount);
                } catch (NumberFormatException e) {
                    showAlert("Invalid amount", "Please enter a valid number");
                }
            });
        });
    }

    private void showTransferDialog() {
        // Implementation for transfer dialog
        showAlert("Coming Soon", "Transfer functionality will be implemented in the next phase");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Rest of the methods (createTransactionsView, createProfileView) remain similar
    private VBox createTransactionsView() {
        VBox transactionsBox = new VBox(15);
        transactionsBox.setPadding(new Insets(20));
        transactionsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Transaction History");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // You would populate this with real transaction data
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

        // Display actual customer information
        Customer customer = controller.getCustomer();
        Label infoLabel = new Label("Name: " + customer.getName() +
                "\nCustomer ID: " + customer.getCustomerId() +
                "\nEmail: " + customer.getEmail() +
                "\nPhone: " + customer.getPhoneNumber());

        profileBox.getChildren().addAll(titleLabel, infoLabel);
        return profileBox;
    }
}