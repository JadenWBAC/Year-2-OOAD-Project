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

        // Load ALL data from text files
        loadAllDataFromFiles();

        // If no data exists, initialize sample data directly to files
        if (customers.isEmpty()) {
            initializeSampleDataInFiles();
            // Reload after creating sample data
            loadAllDataFromFiles();
        }
    }

    private void loadAllDataFromFiles() {
        System.out.println("Loading data from text files...");

        // Load customers
        this.customers = customerDAO.findAllCustomers();
        System.out.println("Loaded " + customers.size() + " customers");

        // Load accounts for each customer
        for (Customer customer : customers) {
            List<Account> customerAccounts = accountDAO.findAccountsByCustomer(customer.getCustomerId());
            for (Account account : customerAccounts) {
                customer.addAccount(account);
            }
            System.out.println("Customer " + customer.getName() + " has " + customerAccounts.size() + " accounts");
        }
    }

    private void initializeSampleDataInFiles() {
        System.out.println("No data found. Creating sample data in text files...");
        DataInitializer initializer = new DataInitializer();
        initializer.initializeSampleData();
    }

    // REST OF YOUR EXISTING METHODS STAY EXACTLY THE SAME
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
    }

    public boolean processDeposit(String accountNumber, double amount) {
        Customer customer = findCustomerByAccount(accountNumber);
        if (customer != null) {
            Account account = customer.getAccountByNumber(accountNumber);
            if (account != null && account.deposit(amount)) {
                accountDAO.updateAccount(account);
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
                return true;
            }
        }
        return false;
    }

    public void applyInterestToAllAccounts() {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                account.applyInterest();
                accountDAO.updateAccount(account);
            }
        }
    }
}