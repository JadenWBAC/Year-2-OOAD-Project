package banking.view;

import banking.controller.TellerDashboardController;
import banking.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TellerDashboardView {
    private Stage stage;
    private TellerDashboardController controller;

    public TellerDashboardView(Stage stage) {
        this.stage = stage;
        this.controller = new TellerDashboardController(stage);
        this.stage.setTitle("Teller Dashboard - Banking System");
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // Top bar
        HBox topBar = createTopBar();

        // Center content with tabs for different functionalities
        TabPane tabPane = new TabPane();

        // Customer Management Tab
        Tab customersTab = new Tab("Customer Management");
        customersTab.setContent(createCustomersView());
        customersTab.setClosable(false);

        // Transactions Tab
        Tab transactionsTab = new Tab("Transactions");
        transactionsTab.setContent(createTransactionsView());
        transactionsTab.setClosable(false);

        // Account Management Tab
        Tab accountsTab = new Tab("Account Management");
        accountsTab.setContent(createAccountsView());
        accountsTab.setClosable(false);

        // Reports Tab
        Tab reportsTab = new Tab("Reports");
        reportsTab.setContent(createReportsView());
        reportsTab.setClosable(false);

        tabPane.getTabs().addAll(customersTab, transactionsTab, accountsTab, reportsTab);

        layout.setTop(topBar);
        layout.setCenter(tabPane);

        Scene scene = new Scene(layout, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        Label titleLabel = new Label("Teller Dashboard - Banking System");
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

    private VBox createCustomersView() {
        VBox customersBox = new VBox(15);
        customersBox.setPadding(new Insets(20));
        customersBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Customer Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Customer list
        ListView<String> customerList = new ListView<>();
        for (Customer customer : controller.getAllCustomers()) {
            String customerInfo = String.format("%s - %s (%s) - %d accounts",
                    customer.getCustomerId(), customer.getName(),
                    customer.getClass().getSimpleName(), customer.getAccounts().size());
            customerList.getItems().add(customerInfo);
        }

        // Action buttons for customer management
        HBox buttonBox = new HBox(10);
        Button viewCustomerBtn = new Button("View Customer Details");
        Button newCustomerBtn = new Button("New Customer");
        Button refreshBtn = new Button("Refresh List");

        viewCustomerBtn.setOnAction(e -> {
            String selected = customerList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                String customerId = selected.split(" - ")[0];
                showCustomerDetails(customerId);
            }
        });

        newCustomerBtn.setOnAction(e -> showNewCustomerDialog());
        refreshBtn.setOnAction(e -> refreshCustomerList(customerList));

        buttonBox.getChildren().addAll(viewCustomerBtn, newCustomerBtn, refreshBtn);

        customersBox.getChildren().addAll(titleLabel, customerList, buttonBox);
        return customersBox;
    }

    private VBox createTransactionsView() {
        VBox transactionsBox = new VBox(15);
        transactionsBox.setPadding(new Insets(20));
        transactionsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Transaction Processing");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Transaction form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(15));
        formGrid.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        TextField accountField = new TextField();
        accountField.setPromptText("Account Number");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        ToggleGroup transactionType = new ToggleGroup();
        RadioButton depositRadio = new RadioButton("Deposit");
        RadioButton withdrawRadio = new RadioButton("Withdraw");
        depositRadio.setToggleGroup(transactionType);
        withdrawRadio.setToggleGroup(transactionType);
        depositRadio.setSelected(true);

        HBox radioBox = new HBox(10, depositRadio, withdrawRadio);

        Button processBtn = new Button("Process Transaction");
        processBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        processBtn.setOnAction(e -> {
            String accountNumber = accountField.getText();
            String amountText = amountField.getText();
            boolean isDeposit = depositRadio.isSelected();

            try {
                double amount = Double.parseDouble(amountText);
                boolean success = isDeposit ?
                        controller.processDeposit(accountNumber, amount) :
                        controller.processWithdrawal(accountNumber, amount);

                if (success) {
                    showAlert("Success", "Transaction processed successfully!");
                    accountField.clear();
                    amountField.clear();
                } else {
                    showAlert("Error", "Transaction failed. Please check account number and amount.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid amount.");
            }
        });

        formGrid.add(new Label("Account Number:"), 0, 0);
        formGrid.add(accountField, 1, 0);
        formGrid.add(new Label("Amount:"), 0, 1);
        formGrid.add(amountField, 1, 1);
        formGrid.add(new Label("Transaction Type:"), 0, 2);
        formGrid.add(radioBox, 1, 2);
        formGrid.add(processBtn, 1, 3);

        transactionsBox.getChildren().addAll(titleLabel, formGrid);
        return transactionsBox;
    }

    private VBox createAccountsView() {
        VBox accountsBox = new VBox(15);
        accountsBox.setPadding(new Insets(20));
        accountsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Account Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Account creation form
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(15));
        formGrid.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Customer ID");

        ComboBox<String> accountTypeCombo = new ComboBox<>();
        accountTypeCombo.getItems().addAll("Savings", "Investment", "Checking");
        accountTypeCombo.setValue("Savings");

        TextField initialBalanceField = new TextField();
        initialBalanceField.setPromptText("Initial Balance");

        TextField employerField = new TextField();
        employerField.setPromptText("Employer Name (for Checking accounts)");
        employerField.setVisible(false);

        TextField employerAddressField = new TextField();
        employerAddressField.setPromptText("Employer Address (for Checking accounts)");
        employerAddressField.setVisible(false);

        // Show/hide employer fields based on account type
        accountTypeCombo.setOnAction(e -> {
            boolean isChecking = "Checking".equals(accountTypeCombo.getValue());
            employerField.setVisible(isChecking);
            employerAddressField.setVisible(isChecking);
        });

        Button createAccountBtn = new Button("Create New Account");
        createAccountBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        createAccountBtn.setOnAction(e -> {
            String customerId = customerIdField.getText();
            String accountType = accountTypeCombo.getValue();
            String balanceText = initialBalanceField.getText();

            try {
                double initialBalance = Double.parseDouble(balanceText);
                Customer customer = controller.findCustomerById(customerId);

                if (customer != null) {
                    Account newAccount = createAccount(accountType, initialBalance, customer,
                            employerField.getText(), employerAddressField.getText());

                    if (newAccount != null) {
                        controller.openNewAccount(customer, newAccount);
                        showAlert("Success", "Account created successfully!");
                        clearForm(customerIdField, initialBalanceField, employerField, employerAddressField);
                    }
                } else {
                    showAlert("Error", "Customer not found!");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid initial balance.");
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        formGrid.add(new Label("Customer ID:"), 0, 0);
        formGrid.add(customerIdField, 1, 0);
        formGrid.add(new Label("Account Type:"), 0, 1);
        formGrid.add(accountTypeCombo, 1, 1);
        formGrid.add(new Label("Initial Balance:"), 0, 2);
        formGrid.add(initialBalanceField, 1, 2);
        formGrid.add(new Label("Employer Name:"), 0, 3);
        formGrid.add(employerField, 1, 3);
        formGrid.add(new Label("Employer Address:"), 0, 4);
        formGrid.add(employerAddressField, 1, 4);
        formGrid.add(createAccountBtn, 1, 5);

        accountsBox.getChildren().addAll(titleLabel, formGrid);
        return accountsBox;
    }

    private VBox createReportsView() {
        VBox reportsBox = new VBox(15);
        reportsBox.setPadding(new Insets(20));
        reportsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Reports and Utilities");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Report buttons
        VBox reportButtons = new VBox(10);

        Button interestBtn = new Button("Apply Monthly Interest to All Accounts");
        interestBtn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        interestBtn.setOnAction(e -> {
            controller.applyInterestToAllAccounts();
            showAlert("Success", "Monthly interest applied to all eligible accounts!");
        });

        Button customerReportBtn = new Button("Generate Customer Report");
        customerReportBtn.setOnAction(e -> generateCustomerReport());

        Button transactionReportBtn = new Button("Generate Transaction Report");
        transactionReportBtn.setOnAction(e -> generateTransactionReport());

        reportButtons.getChildren().addAll(interestBtn, customerReportBtn, transactionReportBtn);

        // Report display area
        TextArea reportArea = new TextArea();
        reportArea.setPrefHeight(300);
        reportArea.setEditable(false);

        reportsBox.getChildren().addAll(titleLabel, reportButtons, reportArea);
        return reportsBox;
    }

    private Account createAccount(String accountType, double initialBalance, Customer customer,
                                  String employerName, String employerAddress) {
        String accountNumber = "ACC" + (System.currentTimeMillis() % 10000);

        switch (accountType) {
            case "Savings":
                return new SavingsAccount(accountNumber, initialBalance, "Main Branch", customer);
            case "Investment":
                return new InvestmentAccount(accountNumber, initialBalance, "Main Branch", customer);
            case "Checking":
                return new CheckingAccount(accountNumber, initialBalance, "Main Branch", customer,
                        employerName, employerAddress);
            default:
                throw new IllegalArgumentException("Invalid account type");
        }
    }

    private void showCustomerDetails(String customerId) {
        Customer customer = controller.findCustomerById(customerId);
        if (customer != null) {
            StringBuilder details = new StringBuilder();
            details.append("Customer Details:\n");
            details.append("ID: ").append(customer.getCustomerId()).append("\n");
            details.append("Name: ").append(customer.getName()).append("\n");
            details.append("Email: ").append(customer.getEmail()).append("\n");
            details.append("Phone: ").append(customer.getPhoneNumber()).append("\n");
            details.append("Address: ").append(customer.getAddress()).append("\n");
            details.append("\nAccounts:\n");

            for (Account account : customer.getAccounts()) {
                details.append("- ").append(account.getClass().getSimpleName())
                        .append(" (").append(account.getAccountNumber()).append("): P")
                        .append(String.format("%.2f", account.getBalance())).append("\n");
            }

            showAlert("Customer Details", details.toString());
        }
    }

    private void showNewCustomerDialog() {
        // Implementation for creating new customers
        showAlert("Coming Soon", "New customer creation will be implemented in the next phase");
    }

    private void refreshCustomerList(ListView<String> customerList) {
        customerList.getItems().clear();
        for (Customer customer : controller.getAllCustomers()) {
            String customerInfo = String.format("%s - %s (%s) - %d accounts",
                    customer.getCustomerId(), customer.getName(),
                    customer.getClass().getSimpleName(), customer.getAccounts().size());
            customerList.getItems().add(customerInfo);
        }
    }

    private void generateCustomerReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== CUSTOMER REPORT ===\n\n");

        for (Customer customer : controller.getAllCustomers()) {
            report.append("Customer: ").append(customer.getName()).append("\n");
            report.append("ID: ").append(customer.getCustomerId()).append("\n");
            report.append("Type: ").append(customer.getClass().getSimpleName()).append("\n");
            report.append("Total Accounts: ").append(customer.getAccounts().size()).append("\n");

            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            report.append("Total Balance: P").append(String.format("%.2f", totalBalance)).append("\n");
            report.append("------------------------\n");
        }

        // In a real application, you would display this in the report area
        showAlert("Customer Report", report.toString());
    }

    private void generateTransactionReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== TRANSACTION REPORT ===\n\n");

        for (Customer customer : controller.getAllCustomers()) {
            report.append("Customer: ").append(customer.getName()).append("\n");

            for (Account account : customer.getAccounts()) {
                report.append("  Account: ").append(account.getAccountNumber())
                        .append(" (").append(account.getClass().getSimpleName()).append(")\n");
                report.append("  Balance: P").append(String.format("%.2f", account.getBalance())).append("\n");
                report.append("  Transactions: ").append(account.getTransactions().size()).append("\n");

                for (Transaction transaction : account.getTransactions()) {
                    report.append("    - ").append(transaction.toString()).append("\n");
                }
                report.append("\n");
            }
            report.append("------------------------\n");
        }

        showAlert("Transaction Report", report.toString());
    }

    private void clearForm(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}