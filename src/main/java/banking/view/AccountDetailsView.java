package banking.view;
import banking.controller.AccountDetailsController;
import banking.model.Transaction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AccountDetailsView {
    private Stage stage;
    private AccountDetailsController controller;

    // Updated constructor to accept controller
    public AccountDetailsView(Stage stage, AccountDetailsController controller) {
        this.stage = stage;
        this.controller = controller;
        this.stage.setTitle("Account Details - " + controller.getAccount().getAccountNumber());
    }

    public void show() {
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f5f5f5;");

        // Top bar with back button
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #3498db;");

        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-text-fill: white;");
        backButton.setOnAction(e -> {
            // Go back to customer dashboard
            controller.getParentController().refreshDashboard();
        });

        Label titleLabel = new Label("Account Details: " + controller.getAccount().getAccountNumber());
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(backButton, titleLabel, spacer);

        // Center content
        VBox centerContent = new VBox(20);
        centerContent.setPadding(new Insets(20));
        centerContent.setAlignment(Pos.TOP_LEFT);

        // Account information - using real account data
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(10);
        infoGrid.setPadding(new Insets(15));
        infoGrid.setStyle("-fx-background-color: white; -fx-border-color: #ddd;");

        infoGrid.add(new Label("Account Type:"), 0, 0);
        infoGrid.add(new Label(controller.getAccount().getClass().getSimpleName()), 1, 0);

        infoGrid.add(new Label("Account Number:"), 0, 1);
        infoGrid.add(new Label(controller.getAccount().getAccountNumber()), 1, 1);

        infoGrid.add(new Label("Current Balance:"), 0, 2);
        Label balanceLabel = new Label(String.format("P%.2f", controller.getAccount().getBalance()));
        balanceLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #27ae60;");
        infoGrid.add(balanceLabel, 1, 2);

        infoGrid.add(new Label("Minimum Balance:"), 0, 3);
        infoGrid.add(new Label(String.format("P%.2f", controller.getAccount().getMinimumBalance())), 1, 3);

        // Transaction history
        Label historyLabel = new Label("Recent Transactions");
        historyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<String> transactionList = new ListView<>();
        for (Transaction transaction : controller.getAccount().getTransactions()) {
            transactionList.getItems().add(transaction.toString());
        }

        // Quick actions
        HBox actionBox = new HBox(10);
        Button depositBtn = new Button("Deposit");
        depositBtn.setOnAction(e -> showDepositDialog());

        Button withdrawBtn = new Button("Withdraw");
        withdrawBtn.setOnAction(e -> showWithdrawDialog());

        Button statementBtn = new Button("Print Statement");
        statementBtn.setOnAction(e -> controller.handlePrintStatement());

        actionBox.getChildren().addAll(depositBtn, withdrawBtn, statementBtn);

        centerContent.getChildren().addAll(infoGrid, historyLabel, transactionList, actionBox);

        layout.setTop(topBar);
        layout.setCenter(centerContent);

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void showDepositDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Deposit Funds");
        dialog.setHeaderText("Enter amount to deposit:");
        dialog.setContentText("Amount:");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                controller.handleDeposit(amount);
                // Refresh the view to show updated balance
                show();
            } catch (NumberFormatException e) {
                showAlert("Invalid amount", "Please enter a valid number");
            }
        });
    }

    private void showWithdrawDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Withdraw Funds");
        dialog.setHeaderText("Enter amount to withdraw:");
        dialog.setContentText("Amount:");

        dialog.showAndWait().ifPresent(amountStr -> {
            try {
                double amount = Double.parseDouble(amountStr);
                controller.handleWithdraw(amount);
                // Refresh the view to show updated balance
                show();
            } catch (NumberFormatException e) {
                showAlert("Invalid amount", "Please enter a valid number");
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}