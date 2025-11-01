package banking.controller;

import banking.dao.AccountDAO;
import banking.dao.impl.TextFileAccountDAO;
import banking.model.*;
import banking.view.*;
import javafx.stage.Stage;

public class CustomerDashboardController {
    private Customer customer;
    private Stage stage;
    private AccountDAO accountDAO;

    public CustomerDashboardController(Customer customer, Stage stage) {
        this.customer = customer;
        this.stage = stage;
        this.accountDAO = new TextFileAccountDAO();

        // Load the latest account data from files
        refreshCustomerAccounts();
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
            // Save the updated account to file (this will also save the new transaction)
            accountDAO.updateAccount(account);
            System.out.println("Deposit successful for account: " + accountNumber + ", Amount: P" + amount);
            // Refresh customer accounts to show updated balance
            refreshCustomerAccounts();
        } else {
            System.out.println("Deposit failed for account: " + accountNumber);
        }
    }

    public void handleWithdraw(String accountNumber, double amount) {
        Account account = customer.getAccountByNumber(accountNumber);
        if (account != null && account.withdraw(amount)) {
            // Save the updated account to file (this will also save the new transaction)
            accountDAO.updateAccount(account);
            System.out.println("Withdrawal successful for account: " + accountNumber + ", Amount: P" + amount);
            // Refresh customer accounts to show updated balance
            refreshCustomerAccounts();
        } else {
            System.out.println("Withdrawal failed for account: " + accountNumber);
        }
    }

    public void handleTransfer(String fromAccount, String toAccount, double amount) {
        Account source = customer.getAccountByNumber(fromAccount);
        Account target = customer.getAccountByNumber(toAccount);

        if (source != null && target != null && source.withdraw(amount)) {
            target.deposit(amount);

            // Save both accounts to file
            accountDAO.updateAccount(source);
            accountDAO.updateAccount(target);

            System.out.println("Transfer successful from " + fromAccount + " to " + toAccount + ": P" + amount);
            // Refresh customer accounts to show updated balances
            refreshCustomerAccounts();
        } else {
            System.out.println("Transfer failed");
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    /**
     * Refresh the dashboard view
     */
    public void refreshDashboard() {
        // Reload customer accounts from files first
        refreshCustomerAccounts();

        CustomerDashboardView dashboard = new CustomerDashboardView(stage, this);
        dashboard.show();
    }

    /**
     * Reload customer's accounts from the database/file
     */
    private void refreshCustomerAccounts() {
        // Clear existing accounts
        customer.getAccounts().clear();

        // Reload from file
        java.util.List<Account> accounts = accountDAO.findAccountsByCustomer(customer.getCustomerId());
        for (Account account : accounts) {
            customer.addAccount(account);
        }

        System.out.println("Refreshed accounts for customer: " + customer.getName() +
                " - Total accounts: " + accounts.size());
    }
}