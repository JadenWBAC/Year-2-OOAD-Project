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

/**
 * CustomerDashboardView - Boundary Class
 * This view represents the customer's main dashboard interface.
 * Display customer information, accounts, and provide transaction functionality
 */
public class CustomerDashboardView {
    // ===== ATTRIBUTES =====
    private Stage stage;                              // JavaFX stage for displaying the view
    private CustomerDashboardController controller;    // Controller for handling user actions

    /**
     * Constructor - Initialize the Customer Dashboard View
     */
    public CustomerDashboardView(Stage stage, CustomerDashboardController controller) {
        this.stage = stage;
        this.controller = controller;
        this.stage.setTitle("Customer Dashboard - " + controller.getCustomer().getName());
    }

    /**
     * Display the customer dashboard interface
     * This method creates and shows the main dashboard layout with tabs
     */
    public void show() {
        // ===== MAIN LAYOUT SETUP =====
        // BorderPane allows us to organize content: Top, Center, Bottom, Left, Right
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // ===== CREATE TOP BAR WITH WELCOME MESSAGE AND LOGOUT =====
        HBox topBar = createTopBar();

        // ===== CREATE TABBED INTERFACE FOR DIFFERENT SECTIONS =====
        TabPane tabPane = new TabPane();

        // Tab 1: My Accounts - View all customer accounts
        Tab accountsTab = new Tab("My Accounts");
        accountsTab.setContent(createAccountsView());
        accountsTab.setClosable(false);  // Prevent user from closing this tab

        // Tab 2: Transactions - View transaction history
        Tab transactionsTab = new Tab("Transactions");
        transactionsTab.setContent(createTransactionsView());
        transactionsTab.setClosable(false);

        // Tab 3: My Profile - View customer profile information
        Tab profileTab = new Tab("My Profile");
        profileTab.setContent(createProfileView());
        profileTab.setClosable(false);

        // Add all tabs to the tab pane
        tabPane.getTabs().addAll(accountsTab, transactionsTab, profileTab);

        // ===== ASSEMBLE THE LAYOUT =====
        layout.setTop(topBar);
        layout.setCenter(tabPane);

        // ===== CREATE AND DISPLAY THE SCENE =====
        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create the top navigation bar
     * Contains welcome message and logout button
     */
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #27ae60;");  // Green color scheme

        // Welcome message with customer name
        Label titleLabel = new Label("Welcome, " + controller.getCustomer().getName());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        // Spacer to push logout button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            // ACTION: Return to login screen when logout is clicked
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        topBar.getChildren().addAll(titleLabel, spacer, logoutButton);
        return topBar;
    }

    /**
     * Create the Accounts View Tab
     * Displays all customer accounts and provides transaction buttons
     */
    private VBox createAccountsView() {
        VBox accountsBox = new VBox(15);
        accountsBox.setPadding(new Insets(20));
        accountsBox.setAlignment(Pos.TOP_LEFT);

        // Section title
        Label titleLabel = new Label("My Accounts");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== DISPLAY ALL CUSTOMER ACCOUNTS =====
        // Iterate through customer's accounts and create a card for each
        VBox accountsList = new VBox(10);
        for (Account account : controller.getCustomer().getAccounts()) {
            // Create a visual card for each account
            accountsList.getChildren().add(createAccountCard(account));
        }

        // ===== ACTION BUTTONS =====
        HBox buttonBox = new HBox(10);

        // Deposit button - allows customer to deposit money
        Button depositBtn = new Button("Make Deposit");
        depositBtn.setOnAction(e -> showDepositDialog());

        // Withdraw button - allows customer to withdraw money
        Button withdrawBtn = new Button("Make Withdrawal");
        withdrawBtn.setOnAction(e -> showWithdrawDialog());

        // Transfer button - allows customer to transfer between accounts
        Button transferBtn = new Button("Transfer Funds");
        transferBtn.setOnAction(e -> showTransferDialog());

        buttonBox.getChildren().addAll(depositBtn, withdrawBtn, transferBtn);

        // Assemble the accounts view
        accountsBox.getChildren().addAll(titleLabel, accountsList, buttonBox);
        return accountsBox;
    }

    /**
     * Create a visual card to display account information
     */
    private HBox createAccountCard(Account account) {
        // Card container
        HBox card = new HBox(15);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        card.setAlignment(Pos.CENTER_LEFT);

        // Account type label (e.g., "SavingsAccount", "CheckingAccount")
        Label typeLabel = new Label(account.getClass().getSimpleName());
        typeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Account number
        Label numberLabel = new Label("Account: " + account.getAccountNumber());

        // Current balance
        Label balanceLabel = new Label(String.format("Balance: P%.2f", account.getBalance()));
        balanceLabel.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        // Group account details vertically
        VBox details = new VBox(5);
        details.getChildren().addAll(typeLabel, numberLabel, balanceLabel);

        // "View Details" button to see full account information
        Button viewBtn = new Button("View Details");
        viewBtn.setOnAction(e -> {
            // ACTION: Delegate to controller to handle viewing account details
            controller.handleViewAccountDetails(account.getAccountNumber());
        });

        // Spacer to push button to the right
        card.getChildren().addAll(details, new Region(), viewBtn);
        HBox.setHgrow(card.getChildren().get(1), Priority.ALWAYS);

        return card;
    }

    /**
     * Display deposit dialog
     * Allows customer to select an account and deposit money
     */
    private void showDepositDialog() {
        // ===== CREATE DIALOG =====
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Deposit Funds");
        dialog.setHeaderText("Select account and enter amount:");

        // ===== ACCOUNT SELECTION DROPDOWN =====
        ComboBox<String> accountCombo = new ComboBox<>();
        // Populate dropdown with customer's accounts
        for (Account account : controller.getCustomer().getAccounts()) {
            String accountInfo = String.format("%s - %s (P%.2f)",
                    account.getAccountNumber(),
                    account.getClass().getSimpleName(),
                    account.getBalance());
            accountCombo.getItems().add(accountInfo);
        }

        // ===== AMOUNT INPUT FIELD =====
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        // ===== LAYOUT THE FORM =====
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

        // ===== HANDLE USER SUBMISSION =====
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK && accountCombo.getValue() != null) {
                try {
                    // Extract account number from selection
                    String accountNumber = accountCombo.getValue().split(" - ")[0];
                    // Parse amount
                    double amount = Double.parseDouble(amountField.getText());

                    // ACTION: Delegate to CONTROLLER to process deposit
                    // This maintains separation of concerns (MVC pattern)
                    controller.handleDeposit(accountNumber, amount);

                    // Refresh view to show updated balance
                    refreshView();

                    // Show success message
                    showAlert("Success", "Deposit successful!");
                } catch (NumberFormatException e) {
                    // Handle invalid amount input
                    showAlert("Error", "Please enter a valid number");
                }
            }
        });
    }

    /**
     * Display withdrawal dialog
     * Allows customer to select an account and withdraw money
     */
    private void showWithdrawDialog() {
        // ===== CREATE DIALOG =====
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Withdraw Funds");
        dialog.setHeaderText("Select account and enter amount:");

        // ===== ACCOUNT SELECTION DROPDOWN =====
        ComboBox<String> accountCombo = new ComboBox<>();
        for (Account account : controller.getCustomer().getAccounts()) {
            String accountInfo = String.format("%s - %s (P%.2f)",
                    account.getAccountNumber(),
                    account.getClass().getSimpleName(),
                    account.getBalance());
            accountCombo.getItems().add(accountInfo);
        }

        // ===== AMOUNT INPUT FIELD =====
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        // ===== LAYOUT THE FORM =====
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

        // ===== HANDLE USER SUBMISSION =====
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK && accountCombo.getValue() != null) {
                try {
                    String accountNumber = accountCombo.getValue().split(" - ")[0];
                    double amount = Double.parseDouble(amountField.getText());

                    // ACTION: Delegate to CONTROLLER to process withdrawal
                    controller.handleWithdraw(accountNumber, amount);

                    // Refresh view
                    refreshView();

                    // Show success message
                    showAlert("Success", "Withdrawal successful!");
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid number");
                }
            }
        });
    }

    /**
     * Display transfer dialog
     * Allows customer to transfer money between their accounts
     */
    private void showTransferDialog() {
        // ===== CHECK IF CUSTOMER HAS MULTIPLE ACCOUNTS =====
        if (controller.getCustomer().getAccounts().size() < 2) {
            showAlert("Cannot Transfer",
                    "You need at least 2 accounts to perform a transfer.");
            return;
        }

        // ===== CREATE TRANSFER DIALOG =====
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Transfer Funds");
        dialog.setHeaderText("Transfer money between your accounts:");

        // ===== FROM ACCOUNT DROPDOWN =====
        ComboBox<String> fromAccountCombo = new ComboBox<>();
        fromAccountCombo.setPromptText("Select source account");

        // ===== TO ACCOUNT DROPDOWN =====
        ComboBox<String> toAccountCombo = new ComboBox<>();
        toAccountCombo.setPromptText("Select destination account");

        // Populate both dropdowns with customer's accounts
        for (Account account : controller.getCustomer().getAccounts()) {
            String accountInfo = String.format("%s - %s (P%.2f)",
                    account.getAccountNumber(),
                    account.getClass().getSimpleName(),
                    account.getBalance());
            fromAccountCombo.getItems().add(accountInfo);
            toAccountCombo.getItems().add(accountInfo);
        }

        // ===== AMOUNT INPUT FIELD =====
        TextField amountField = new TextField();
        amountField.setPromptText("Amount to transfer");

        // ===== LAYOUT THE TRANSFER FORM =====
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("From Account:"), 0, 0);
        grid.add(fromAccountCombo, 1, 0);

        grid.add(new Label("To Account:"), 0, 1);
        grid.add(toAccountCombo, 1, 1);

        grid.add(new Label("Amount:"), 0, 2);
        grid.add(amountField, 1, 2);

        // Add information label
        Label infoLabel = new Label("Note: Transfer is subject to minimum balance requirements");
        infoLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 10px;");
        grid.add(infoLabel, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // ===== HANDLE TRANSFER SUBMISSION =====
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                // VALIDATION: Check all fields are filled
                if (fromAccountCombo.getValue() == null ||
                        toAccountCombo.getValue() == null ||
                        amountField.getText().isEmpty()) {
                    showAlert("Error", "Please fill in all fields");
                    return;
                }

                try {
                    // Extract account numbers from dropdown selections
                    String fromAccount = fromAccountCombo.getValue().split(" - ")[0];
                    String toAccount = toAccountCombo.getValue().split(" - ")[0];

                    // VALIDATION: Cannot transfer to same account
                    if (fromAccount.equals(toAccount)) {
                        showAlert("Error", "Cannot transfer to the same account");
                        return;
                    }

                    // Parse transfer amount
                    double amount = Double.parseDouble(amountField.getText());

                    // VALIDATION: Amount must be positive
                    if (amount <= 0) {
                        showAlert("Error", "Amount must be greater than zero");
                        return;
                    }

                    // ACTION: Delegate to CONTROLLER to process transfer
                    // Controller will handle business logic:
                    // 1. Withdraw from source account
                    // 2. Deposit to destination account
                    // 3. Update both accounts in the database/file
                    controller.handleTransfer(fromAccount, toAccount, amount);

                    // Refresh view to show updated balances
                    refreshView();

                    // Show success message with details
                    showAlert("Transfer Successful",
                            String.format("Successfully transferred P%.2f\nFrom: %s\nTo: %s",
                                    amount, fromAccount, toAccount));

                } catch (NumberFormatException e) {
                    // Handle invalid amount input
                    showAlert("Error", "Please enter a valid amount");
                }
            }
        });
    }

    /**
     * Refresh the current view
     * This reloads the customer dashboard to show updated information
     */
    private void refreshView() {
        // Refresh the dashboard to display updated account balances
        show();
    }

    /**
     * Create the Transactions View Tab
     * Displays transaction history for all customer accounts
     */
    private VBox createTransactionsView() {
        VBox transactionsBox = new VBox(15);
        transactionsBox.setPadding(new Insets(20));
        transactionsBox.setAlignment(Pos.TOP_LEFT);

        // Section title
        Label titleLabel = new Label("Transaction History");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== DISPLAY TRANSACTIONS FOR EACH ACCOUNT =====
        VBox transactionsList = new VBox(10);

        // Iterate through each account
        for (Account account : controller.getCustomer().getAccounts()) {
            // Account header
            Label accountLabel = new Label(String.format("%s - %s",
                    account.getAccountNumber(),
                    account.getClass().getSimpleName()));
            accountLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            transactionsList.getChildren().add(accountLabel);

            // Display transactions for this account
            if (account.getTransactions().isEmpty()) {
                Label noTransactions = new Label("  No transactions yet");
                noTransactions.setStyle("-fx-text-fill: #7f8c8d;");
                transactionsList.getChildren().add(noTransactions);
            } else {
                // List each transaction
                for (int i = 0; i < account.getTransactions().size(); i++) {
                    Label transactionLabel = new Label("  " + account.getTransactions().get(i).toString());
                    transactionLabel.setStyle("-fx-font-size: 12px;");
                    transactionsList.getChildren().add(transactionLabel);
                }
            }

            // Add separator between accounts
            transactionsList.getChildren().add(new Separator());
        }

        transactionsBox.getChildren().addAll(titleLabel, transactionsList);
        return transactionsBox;
    }

    /**
     * Create the Profile View Tab
     * Displays customer's personal information
     */
    private VBox createProfileView() {
        VBox profileBox = new VBox(15);
        profileBox.setPadding(new Insets(20));
        profileBox.setAlignment(Pos.TOP_LEFT);

        // Section title
        Label titleLabel = new Label("My Profile");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== DISPLAY CUSTOMER INFORMATION =====
        Customer customer = controller.getCustomer();

        // Create formatted label with customer details
        Label infoLabel = new Label(
                "Name: " + customer.getName() +
                        "\nCustomer ID: " + customer.getCustomerId() +
                        "\nEmail: " + customer.getEmail() +
                        "\nPhone: " + customer.getPhoneNumber() +
                        "\nAddress: " + customer.getAddress() +
                        "\n\nTotal Accounts: " + customer.getAccounts().size()
        );
        infoLabel.setStyle("-fx-font-size: 14px;");

        profileBox.getChildren().addAll(titleLabel, infoLabel);
        return profileBox;
    }

    /**
     * Display an alert dialog
     * This is used for showing messages to the user (success, error, info)
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
