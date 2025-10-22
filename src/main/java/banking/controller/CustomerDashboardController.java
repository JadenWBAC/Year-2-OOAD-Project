package banking.controller;

import banking.model.*;
import banking.view.*;
import javafx.stage.Stage;

public class CustomerDashboardController {
    private Customer customer;
    private Stage stage;

    public CustomerDashboardController(Customer customer, Stage stage) {
        this.customer = customer;
        this.stage = stage;
    }

    public void handleViewAccountDetails(String accountNumber) {
        Account account = customer.getAccountByNumber(accountNumber);
        if (account != null) {
            // Create and show the account details view with controller
            AccountDetailsController detailsController = new AccountDetailsController(account, stage, this);
            AccountDetailsView detailsView = new AccountDetailsView(stage, detailsController);
            detailsView.show();
        } else {
            System.out.println("Account not found: " + accountNumber);
        }
    }

    public void handleDeposit(String accountNumber, double amount) {
        Account account = customer.getAccountByNumber(accountNumber);
        if (account != null && account.deposit(amount)) {
            System.out.println("Deposit successful for account: " + accountNumber);
            // Refresh the view or show success message
        } else {
            System.out.println("Deposit failed for account: " + accountNumber);
        }
    }

    public void handleWithdraw(String accountNumber, double amount) {
        Account account = customer.getAccountByNumber(accountNumber);
        if (account != null && account.withdraw(amount)) {
            System.out.println("Withdrawal successful for account: " + accountNumber);
        } else {
            System.out.println("Withdrawal failed for account: " + accountNumber);
        }
    }

    public void handleTransfer(String fromAccount, String toAccount, double amount) {
        // Implementation for transfer between accounts
        Account source = customer.getAccountByNumber(fromAccount);
        Account target = customer.getAccountByNumber(toAccount);

        if (source != null && target != null && source.withdraw(amount)) {
            target.deposit(amount);
            System.out.println("Transfer successful from " + fromAccount + " to " + toAccount + ": P" + amount);
        } else {
            System.out.println("Transfer failed");
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    // Add this method to refresh the dashboard
    public void refreshDashboard() {
        CustomerDashboardView dashboard = new CustomerDashboardView(stage, this);
        dashboard.show();
    }
}