package banking.view;

import banking.dao.impl.TextFileUserDAO;
import banking.controller.TellerDashboardController;
import banking.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * TellerDashboardView - Boundary Class
 * This view represents the bank teller's main dashboard interface.
 * Why this?: Provide teller with tools to manage customers, accounts, and transactions
 * - Teller can create customers, open accounts, process transactions, generate reports
 */
public class TellerDashboardView {
    // ===== ATTRIBUTES =====
    private Stage stage;                              // JavaFX stage for displaying the view
    private TellerDashboardController controller;     // Controller for handling teller actions

    /**
     * Constructor - Initialize the Teller Dashboard View
     *
     * @param stage The JavaFX stage to display the view
     */
    public TellerDashboardView(Stage stage) {
        this.stage = stage;
        this.controller = new TellerDashboardController(stage);
        this.stage.setTitle("Teller Dashboard - Banking System");
    }

    /**
     * Display the teller dashboard interface
     * This method creates and shows the main dashboard layout with tabs
     */
    public void show() {
        // ===== MAIN LAYOUT SETUP =====
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // ===== CREATE TOP BAR =====
        HBox topBar = createTopBar();

        // ===== CREATE TABBED INTERFACE FOR TELLER FUNCTIONS =====
        TabPane tabPane = new TabPane();

        // Tab 1: Customer Management - View and create customers
        Tab customersTab = new Tab("Customer Management");
        customersTab.setContent(createCustomersView());
        customersTab.setClosable(false);

        // Tab 2: Transactions - Process deposits and withdrawals
        Tab transactionsTab = new Tab("Transactions");
        transactionsTab.setContent(createTransactionsView());
        transactionsTab.setClosable(false);

        // Tab 3: Account Management - Open new accounts
        Tab accountsTab = new Tab("Account Management");
        accountsTab.setContent(createAccountsView());
        accountsTab.setClosable(false);

        // Tab 4: Reports - Generate reports and apply interest
        Tab reportsTab = new Tab("Reports");
        reportsTab.setContent(createReportsView());
        reportsTab.setClosable(false);

        tabPane.getTabs().addAll(customersTab, transactionsTab, accountsTab, reportsTab);

        // ===== ASSEMBLE THE LAYOUT =====
        layout.setTop(topBar);
        layout.setCenter(tabPane);

        // ===== CREATE AND DISPLAY THE SCENE =====
        Scene scene = new Scene(layout, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Create the top navigation bar
     * Contains title and logout button
     */
    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50;");  // Dark blue-gray

        Label titleLabel = new Label("Teller Dashboard - Banking System");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        logoutButton.setOnAction(e -> {
            // ACTION: Return to login screen
            LoginView loginView = new LoginView(stage);
            loginView.show();
        });

        topBar.getChildren().addAll(titleLabel, spacer, logoutButton);
        return topBar;
    }

    /**
     * Create the Customer Management View Tab
     * Displays list of all customers and allows creating new customers
     */
    private VBox createCustomersView() {
        VBox customersBox = new VBox(15);
        customersBox.setPadding(new Insets(20));
        customersBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Customer Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== CUSTOMER LIST =====
        ListView<String> customerList = new ListView<>();
        // Populate list with all customers
        for (Customer customer : controller.getAllCustomers()) {
            String customerInfo = String.format("%s - %s (%s) - %d accounts",
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getClass().getSimpleName(),
                    customer.getAccounts().size());
            customerList.getItems().add(customerInfo);
        }

        // ===== ACTION BUTTONS =====
        HBox buttonBox = new HBox(10);

        Button viewCustomerBtn = new Button("View Customer Details");
        viewCustomerBtn.setOnAction(e -> {
            String selected = customerList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                // Extract customer ID from selection
                String customerId = selected.split(" - ")[0];
                // ACTION: Show customer details
                showCustomerDetails(customerId);
            }
        });

        Button newCustomerBtn = new Button("New Customer");
        newCustomerBtn.setOnAction(e -> {
            // ACTION: Open new customer creation dialog
            showNewCustomerDialog();
            // Refresh list after creation
            refreshCustomerList(customerList);
        });

        Button refreshBtn = new Button("Refresh List");
        refreshBtn.setOnAction(e -> refreshCustomerList(customerList));

        buttonBox.getChildren().addAll(viewCustomerBtn, newCustomerBtn, refreshBtn);

        customersBox.getChildren().addAll(titleLabel, customerList, buttonBox);
        return customersBox;
    }

    /**
     * Create the Transactions View Tab
     * Allows teller to process deposits and withdrawals for any account
     */
    private VBox createTransactionsView() {
        VBox transactionsBox = new VBox(15);
        transactionsBox.setPadding(new Insets(20));
        transactionsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Transaction Processing");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== TRANSACTION FORM =====
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(15));
        formGrid.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        // Account number input
        TextField accountField = new TextField();
        accountField.setPromptText("Account Number");

        // Amount input
        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        // Transaction type selection
        ToggleGroup transactionType = new ToggleGroup();
        RadioButton depositRadio = new RadioButton("Deposit");
        RadioButton withdrawRadio = new RadioButton("Withdraw");
        depositRadio.setToggleGroup(transactionType);
        withdrawRadio.setToggleGroup(transactionType);
        depositRadio.setSelected(true);

        HBox radioBox = new HBox(10, depositRadio, withdrawRadio);

        // Process button
        Button processBtn = new Button("Process Transaction");
        processBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");

        processBtn.setOnAction(e -> {
            String accountNumber = accountField.getText();
            String amountText = amountField.getText();
            boolean isDeposit = depositRadio.isSelected();

            try {
                double amount = Double.parseDouble(amountText);

                // ACTION: Delegate to controller to process transaction
                boolean success = isDeposit ?
                        controller.processDeposit(accountNumber, amount) :
                        controller.processWithdrawal(accountNumber, amount);

                if (success) {
                    showAlert("Success", "Transaction processed successfully!");
                    // Clear form
                    accountField.clear();
                    amountField.clear();
                } else {
                    showAlert("Error", "Transaction failed. Please check account number and amount.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid amount.");
            }
        });

        // Layout form
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

    /**
     * Create the Account Management View Tab
     * Allows teller to open new accounts for existing customers
     */
    private VBox createAccountsView() {
        VBox accountsBox = new VBox(15);
        accountsBox.setPadding(new Insets(20));
        accountsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Account Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== ACCOUNT CREATION FORM =====
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(15));
        formGrid.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        // Customer ID input
        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Customer ID");

        // Account type selection
        ComboBox<String> accountTypeCombo = new ComboBox<>();
        accountTypeCombo.getItems().addAll("Savings", "Investment", "Checking");
        accountTypeCombo.setPromptText("Select Account Type");

        // Initial balance input
        TextField initialBalanceField = new TextField();
        initialBalanceField.setPromptText("Initial Balance");

        // Employer fields (for Checking accounts only)
        TextField employerNameField = new TextField();
        employerNameField.setPromptText("Employer Name (for Checking only)");
        employerNameField.setDisable(true);

        TextField employerAddressField = new TextField();
        employerAddressField.setPromptText("Employer Address (for Checking only)");
        employerAddressField.setDisable(true);

        // Enable employer fields when Checking is selected
        accountTypeCombo.setOnAction(e -> {
            boolean isChecking = "Checking".equals(accountTypeCombo.getValue());
            employerNameField.setDisable(!isChecking);
            employerAddressField.setDisable(!isChecking);
        });

        // Open Account button
        Button openAccountBtn = new Button("Open Account");
        openAccountBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        openAccountBtn.setOnAction(e -> {
            String customerId = customerIdField.getText();
            String accountType = accountTypeCombo.getValue();
            String balanceText = initialBalanceField.getText();
            String employerName = employerNameField.getText();
            String employerAddress = employerAddressField.getText();

            // VALIDATION: Check all required fields
            if (customerId.isEmpty() || accountType == null || balanceText.isEmpty()) {
                showAlert("Error", "Please fill in all required fields.");
                return;
            }

            try {
                double initialBalance = Double.parseDouble(balanceText);

                // ACTION: Delegate to controller to open account
                boolean success = controller.openAccount(customerId, accountType,
                        initialBalance, employerName, employerAddress);

                if (success) {
                    showAlert("Success", "Account opened successfully!");
                    // Clear form
                    clearForm(customerIdField, initialBalanceField,
                            employerNameField, employerAddressField);
                    accountTypeCombo.setValue(null);
                } else {
                    showAlert("Error", "Failed to open account. Please check customer ID and requirements.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid initial balance.");
            }
        });

        // Layout form
        formGrid.add(new Label("Customer ID:"), 0, 0);
        formGrid.add(customerIdField, 1, 0);
        formGrid.add(new Label("Account Type:"), 0, 1);
        formGrid.add(accountTypeCombo, 1, 1);
        formGrid.add(new Label("Initial Balance:"), 0, 2);
        formGrid.add(initialBalanceField, 1, 2);
        formGrid.add(new Label("Employer Name:"), 0, 3);
        formGrid.add(employerNameField, 1, 3);
        formGrid.add(new Label("Employer Address:"), 0, 4);
        formGrid.add(employerAddressField, 1, 4);
        formGrid.add(openAccountBtn, 1, 5);

        accountsBox.getChildren().addAll(titleLabel, formGrid);
        return accountsBox;
    }

    /**
     * Create the Reports View Tab
     * Allows teller to generate reports and apply interest
     */
    private VBox createReportsView() {
        VBox reportsBox = new VBox(15);
        reportsBox.setPadding(new Insets(20));
        reportsBox.setAlignment(Pos.TOP_LEFT);

        Label titleLabel = new Label("Reports and Utilities");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ===== REPORT BUTTONS =====
        VBox reportButtons = new VBox(10);

        // Apply Interest button
        Button interestBtn = new Button("Apply Monthly Interest to All Accounts");
        interestBtn.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        interestBtn.setOnAction(e -> {
            // ACTION: Apply interest to all eligible accounts
            controller.applyInterestToAllAccounts();
            showAlert("Success", "Monthly interest applied to all eligible accounts!");
        });

        // Customer Report button
        Button customerReportBtn = new Button("Generate Customer Report");
        customerReportBtn.setOnAction(e -> generateCustomerReport());

        // Transaction Report button
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

    /**
     * Create an account object based on type
     * Helper method to instantiate the correct Account subclass
     *
     * @param accountType Type of account (Savings, Investment, Checking)
     * @param initialBalance Starting balance
     * @param customer The customer who owns the account
     * @param employerName Employer name (for Checking accounts)
     * @param employerAddress Employer address (for Checking accounts)
     * @return Account object of the specified type
     */
    private Account createAccount(String accountType, double initialBalance, Customer customer,
                                  String employerName, String employerAddress) {
        // Generate unique account number
        String accountNumber = "ACC" + (System.currentTimeMillis() % 10000);

        // Create appropriate account type
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

    /**
     * Display detailed information about a customer
     *
     * customerId The customer ID to display
     */
    private void showCustomerDetails(String customerId) {
        // ACTION: Retrieve customer from controller
        Customer customer = controller.findCustomerById(customerId);

        if (customer != null) {
            // Build detailed customer information
            StringBuilder details = new StringBuilder();
            details.append("Customer Details:\n");
            details.append("ID: ").append(customer.getCustomerId()).append("\n");
            details.append("Name: ").append(customer.getName()).append("\n");
            details.append("Email: ").append(customer.getEmail()).append("\n");
            details.append("Phone: ").append(customer.getPhoneNumber()).append("\n");
            details.append("Address: ").append(customer.getAddress()).append("\n");
            details.append("\nAccounts:\n");

            // List all customer accounts
            for (Account account : customer.getAccounts()) {
                details.append("- ").append(account.getClass().getSimpleName())
                        .append(" (").append(account.getAccountNumber()).append("): P")
                        .append(String.format("%.2f", account.getBalance())).append("\n");
            }

            showAlert("Customer Details", details.toString());
        }
    }

    /**
     * Display dialog to create a new customer
     */
    /**
     * Display dialog to create a new customer WITH username/password
     */
    private void showNewCustomerDialog() {
        // ===== CREATE CUSTOMER CREATION DIALOG =====
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Customer");
        dialog.setHeaderText("Enter customer information and login credentials:");

        // ===== CUSTOMER TYPE SELECTION =====
        ComboBox<String> customerTypeCombo = new ComboBox<>();
        customerTypeCombo.getItems().addAll("Individual", "Company");
        customerTypeCombo.setPromptText("Select Customer Type");
        customerTypeCombo.setPrefWidth(250);

        // ===== COMMON FIELDS =====
        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Customer ID (e.g., CUST001)");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        // ===== LOGIN CREDENTIALS FIELDS =====
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username (for login)");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password (min 4 characters)");

        // ===== INDIVIDUAL CUSTOMER FIELDS =====
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField surnameField = new TextField();
        surnameField.setPromptText("Surname");

        TextField nationalIdField = new TextField();
        nationalIdField.setPromptText("National ID");

        // ===== COMPANY CUSTOMER FIELDS =====
        TextField companyNameField = new TextField();
        companyNameField.setPromptText("Company Name");

        TextField companyNumberField = new TextField();
        companyNumberField.setPromptText("Company Registration Number");

        // ===== DYNAMIC FORM LAYOUT =====
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Always show common fields
        grid.add(new Label("Customer Type:"), 0, 0);
        grid.add(customerTypeCombo, 1, 0);
        grid.add(new Label("Customer ID:"), 0, 1);
        grid.add(customerIdField, 1, 1);

        // Create containers for type-specific fields
        VBox individualFields = new VBox(10);
        individualFields.getChildren().addAll(
                new Label("First Name:"),
                firstNameField,
                new Label("Surname:"),
                surnameField,
                new Label("National ID:"),
                nationalIdField
        );

        VBox companyFields = new VBox(10);
        companyFields.getChildren().addAll(
                new Label("Company Name:"),
                companyNameField,
                new Label("Company Number:"),
                companyNumberField
        );

        // Initially hide both
        individualFields.setVisible(false);
        companyFields.setVisible(false);

        // ===== DYNAMIC FIELD DISPLAY BASED ON CUSTOMER TYPE =====
        customerTypeCombo.setOnAction(e -> {
            String selectedType = customerTypeCombo.getValue();
            individualFields.setVisible("Individual".equals(selectedType));
            companyFields.setVisible("Company".equals(selectedType));
        });

        // Add dynamic fields area
        grid.add(new Label("Type-specific:"), 0, 2);
        VBox typeFieldsContainer = new VBox(5);
        typeFieldsContainer.getChildren().addAll(individualFields, companyFields);
        grid.add(typeFieldsContainer, 1, 2);

        // Common fields at bottom
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);

        // Login credentials section
        grid.add(new Label("Login Credentials:"), 0, 6);
        grid.add(new Label(""), 1, 6); // Spacer

        grid.add(new Label("Username:"), 0, 7);
        grid.add(usernameField, 1, 7);
        grid.add(new Label("Password:"), 0, 8);
        grid.add(passwordField, 1, 8);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // ===== HANDLE CUSTOMER CREATION =====
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                String customerType = customerTypeCombo.getValue();

                // VALIDATION: Check customer type is selected
                if (customerType == null) {
                    showAlert("Error", "Please select a customer type");
                    return;
                }

                // Get common fields
                String customerId = customerIdField.getText().trim();
                String address = addressField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();

                // VALIDATION: Check all required fields
                if (customerId.isEmpty() || address.isEmpty() || phone.isEmpty() ||
                        email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    showAlert("Error", "Please fill in all required fields");
                    return;
                }

                // VALIDATION: Check password length
                if (password.length() < 4) {
                    showAlert("Error", "Password must be at least 4 characters long");
                    return;
                }

                try {
                    Customer newCustomer = null;

                    if ("Individual".equals(customerType)) {
                        // ===== CREATE INDIVIDUAL CUSTOMER =====
                        String firstName = firstNameField.getText().trim();
                        String surname = surnameField.getText().trim();
                        String nationalId = nationalIdField.getText().trim();

                        // VALIDATION: Check individual-specific fields
                        if (firstName.isEmpty() || surname.isEmpty() || nationalId.isEmpty()) {
                            showAlert("Error", "Please fill in all individual customer fields");
                            return;
                        }

                        // Create IndividualCustomer object
                        newCustomer = new IndividualCustomer(
                                customerId, firstName, surname, nationalId,
                                address, phone, email
                        );

                    } else if ("Company".equals(customerType)) {
                        // ===== CREATE COMPANY CUSTOMER =====
                        String companyName = companyNameField.getText().trim();
                        String companyNumber = companyNumberField.getText().trim();

                        // VALIDATION: Check company-specific fields
                        if (companyName.isEmpty() || companyNumber.isEmpty()) {
                            showAlert("Error", "Please fill in all company customer fields");
                            return;
                        }

                        // Create CompanyCustomer object
                        newCustomer = new CompanyCustomer(
                                customerId, companyName, companyNumber,
                                address, phone, email
                        );
                    }

                    // ACTION: Save new customer through controller
                    if (newCustomer != null) {
                        // First save the customer
                        controller.createNewCustomer(newCustomer);

                        // Then create user account with provided credentials
                        User user = new User(username, password, "CUSTOMER", newCustomer);
                        TextFileUserDAO userDAO = new TextFileUserDAO();
                        userDAO.saveUser(user);

                        // Show success message
                        showAlert("Success",
                                String.format("Customer %s (%s) created successfully!\n\n" +
                                                "Login Credentials:\nUsername: %s\nPassword: [hidden]",
                                        newCustomer.getName(), newCustomer.getCustomerId(), username));

                        System.out.println("New customer created: " + newCustomer.getName() + " with username: " + username);
                    }

                } catch (Exception e) {
                    showAlert("Error", "Failed to create customer: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Refresh the customer list view
     * Reloads all customers from the database/file
     */
    private void refreshCustomerList(ListView<String> customerList) {
        // Clear existing items
        customerList.getItems().clear();

        // Reload from controller
        for (Customer customer : controller.getAllCustomers()) {
            String customerInfo = String.format("%s - %s (%s) - %d accounts",
                    customer.getCustomerId(), customer.getName(),
                    customer.getClass().getSimpleName(), customer.getAccounts().size());
            customerList.getItems().add(customerInfo);
        }
    }

    /**
     * Generate a customer report
     * Shows a summary of all customers and their accounts
     */
    private void generateCustomerReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== CUSTOMER REPORT ===\n\n");

        // Iterate through all customers
        for (Customer customer : controller.getAllCustomers()) {
            report.append("Customer: ").append(customer.getName()).append("\n");
            report.append("ID: ").append(customer.getCustomerId()).append("\n");
            report.append("Type: ").append(customer.getClass().getSimpleName()).append("\n");
            report.append("Total Accounts: ").append(customer.getAccounts().size()).append("\n");

            // Calculate total balance across all accounts
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            report.append("Total Balance: P").append(String.format("%.2f", totalBalance)).append("\n");
            report.append("------------------------\n");
        }

        showAlert("Customer Report", report.toString());
    }

    /**
     * Generate a transaction report
     * Shows all transactions for all accounts
     */
    private void generateTransactionReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== TRANSACTION REPORT ===\n\n");

        // Iterate through all customers and their accounts
        for (Customer customer : controller.getAllCustomers()) {
            report.append("Customer: ").append(customer.getName()).append("\n");

            for (Account account : customer.getAccounts()) {
                report.append("  Account: ").append(account.getAccountNumber())
                        .append(" (").append(account.getClass().getSimpleName()).append(")\n");
                report.append("  Balance: P").append(String.format("%.2f", account.getBalance())).append("\n");
                report.append("  Transactions: ").append(account.getTransactions().size()).append("\n");

                // List all transactions
                for (Transaction transaction : account.getTransactions()) {
                    report.append("    - ").append(transaction.toString()).append("\n");
                }
                report.append("\n");
            }
            report.append("------------------------\n");
        }

        showAlert("Transaction Report", report.toString());
    }

    /**
     * Clear multiple text fields
     * Helper method to reset form inputs
     */
    private void clearForm(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    /**
     * Display an alert dialog
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
