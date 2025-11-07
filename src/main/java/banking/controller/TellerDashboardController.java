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
    private List<Customer> customers;

    public TellerDashboardController(Stage stage) {
        this.stage = stage;
        this.customerDAO = new TextFileCustomerDAO();
        this.accountDAO = new TextFileAccountDAO();
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

    public boolean openAccount(String customerId, String accountType, double initialBalance, String employerName, String employerAddress) {
        return false;
    }

}