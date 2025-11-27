package banking.controller;

import banking.dao.*;
import banking.dao.impl.*;
import banking.model.*;
import banking.view.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class TellerDashboardController {
    private Stage stage;
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private UserDAO userDAO;
    private List<Customer> customers;

    public TellerDashboardController(Stage stage) {
        this.stage = stage;
        this.customerDAO = new TextFileCustomerDAO();
        this.accountDAO = new TextFileAccountDAO();
        this.userDAO = new TextFileUserDAO();
        this.customers = new ArrayList<>();

        // Load all data from existing text files
        loadAllDataFromFiles();
    }

    private void loadAllDataFromFiles() {
        System.out.println("Loading data from text files...");

        // Load customers from existing files
        this.customers = customerDAO.findAllCustomers();
        System.out.println("Loaded " + customers.size() + " customers from files");

        // Load accounts for each customer
        for (Customer customer : customers) {
            List<Account> customerAccounts = accountDAO.findAccountsByCustomer(customer.getCustomerId());
            for (Account account : customerAccounts) {
                customer.addAccount(account);
            }
            System.out.println("Customer " + customer.getName() + " has " + customerAccounts.size() + " accounts");
        }
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public Customer findCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    public Customer findCustomerByAccount(String accountNumber) {
        for (Customer customer : customers) {
            if (customer.getAccountByNumber(accountNumber) != null) {
                return customer;
            }
        }
        return null;
    }

    public void createNewCustomer(Customer customer) {
        customers.add(customer);
        customerDAO.saveCustomer(customer);

        // AUTO-CREATE USER ACCOUNT FOR THE NEW CUSTOMER
        User newUser = ((TextFileUserDAO) userDAO).createCustomerUser(customer);

        System.out.println("Customer created: " + customer.getName() +
                " | Login: " + newUser.getUsername() + " / 123456");
    }

    public void openNewAccount(Customer customer, Account account) {
        customer.addAccount(account);
        accountDAO.saveAccount(account);

        // Reload customer accounts to ensure data is synchronized
        List<Account> updatedAccounts = accountDAO.findAccountsByCustomer(customer.getCustomerId());
        System.out.println("Account created. Customer now has " + updatedAccounts.size() + " accounts.");
    }

    public boolean processDeposit(String accountNumber, double amount) {
        Customer customer = findCustomerByAccount(accountNumber);
        if (customer != null) {
            Account account = customer.getAccountByNumber(accountNumber);
            if (account != null && account.deposit(amount)) {
                accountDAO.updateAccount(account);
                System.out.println("Deposit processed: P" + amount + " to account " + accountNumber);
                return true;
            }
        }
        return false;
    }

    public boolean processWithdrawal(String accountNumber, double amount) {
        Customer customer = findCustomerByAccount(accountNumber);
        if (customer != null) {
            Account account = customer.getAccountByNumber(accountNumber);
            if (account != null && account.withdraw(amount)) {
                accountDAO.updateAccount(account);
                System.out.println("Withdrawal processed: P" + amount + " from account " + accountNumber);
                return true;
            }
        }
        return false;
    }

    public void applyInterestToAllAccounts() {
        int accountsProcessed = 0;
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                double interestBefore = account.calculateInterest();
                if (interestBefore > 0) {
                    account.applyInterest();
                    accountDAO.updateAccount(account);
                    accountsProcessed++;
                    System.out.println("Interest applied to account " + account.getAccountNumber() +
                            ": P" + String.format("%.2f", interestBefore));
                }
            }
        }
        System.out.println("Total accounts processed for interest: " + accountsProcessed);
    }

    /**
     * Refresh customer data from files
     */
    public void refreshData() {
        customers.clear();
        loadAllDataFromFiles();
    }

    public boolean openAccount(String customerId, String accountType, double initialBalance,
                               String employerName, String employerAddress) {
        try {
            // Find the customer
            Customer customer = findCustomerById(customerId);
            if (customer == null) {
                System.out.println("Customer not found: " + customerId);
                return false;
            }

            // Generate unique account number
            String accountNumber = "ACC" + String.format("%03d", (int)(System.currentTimeMillis() % 1000));

            // Create the appropriate account type
            Account account;
            String branch = "Main Branch"; // Default branch

            switch (accountType.toUpperCase()) {
                case "SAVINGS":
                    if (initialBalance < 50.0) {
                        System.out.println("Savings account requires minimum P50.00 initial deposit");
                        return false;
                    }
                    account = new SavingsAccount(accountNumber, initialBalance, branch, customer);
                    break;

                case "INVESTMENT":
                    if (initialBalance < 500.0) {
                        System.out.println("Investment account requires minimum P500.00 initial deposit");
                        return false;
                    }
                    account = new InvestmentAccount(accountNumber, initialBalance, branch, customer);
                    break;

                case "CHECKING":
                    account = new CheckingAccount(accountNumber, initialBalance, branch, customer,
                            employerName, employerAddress);
                    break;

                default:
                    System.out.println("Invalid account type: " + accountType);
                    return false;
            }

            // Save the account
            openNewAccount(customer, account);

            System.out.println("Account created successfully: " + accountNumber + " for customer: " + customerId);
            return true;

        } catch (Exception e) {
            System.err.println("Error opening account: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}