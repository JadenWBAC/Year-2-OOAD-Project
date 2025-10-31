package banking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Savings account with minimum balance and interest
 * Extends Account class with specific interest rules
 */
public abstract class Account {
    // Encapsulated attributes
    private String accountNumber;
    private double balance;
    private String branch;
    private Customer customer;
    private List<Transaction> transactions;
    
    // Constructor
    public Account(String accountNumber, double initialBalance, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.branch = branch;
        this.customer = customer;
        this.transactions = new ArrayList<>();
    }

    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public double getBalance() {
        return balance;
    }
    
    protected void setBalance(double balance) {
        this.balance = balance;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Return copy for safety
    }

    // Abstract methods - must be implemented by subclasses
    public abstract double calculateInterest();
    public abstract double getMinimumBalance();
    
    // Concrete methods with business logic
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive");
            return false;
        }
        
        balance += amount;
        Transaction transaction = new Transaction(
            "TXN" + System.currentTimeMillis(),
            "DEPOSIT",
            amount,
            balance
        );
        transactions.add(transaction);
        System.out.println("Deposit successful. New balance: " + balance);
        return true;
    }
    
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive");
            return false;
        }
        
        if (balance - amount < getMinimumBalance()) {
            System.out.println("Insufficient funds. Minimum balance requirement not met.");
            return false;
        }
        
        balance -= amount;
        Transaction transaction = new Transaction(
            "TXN" + System.currentTimeMillis(),
            "WITHDRAWAL",
            amount,
            balance
        );
        transactions.add(transaction);
        System.out.println("Withdrawal successful. New balance: " + balance);
        return true;
    }
    
    public void applyInterest() {
        double interest = calculateInterest();
        if (interest > 0) {
            balance += interest;
            Transaction transaction = new Transaction(
                "TXN" + System.currentTimeMillis(),
                "INTEREST",
                interest,
                balance
            );
            transactions.add(transaction);
            System.out.println("Interest applied: " + interest + ". New balance: " + balance);
        }
    }
    
    public void printStatement() {
        System.out.println("\n=== ACCOUNT STATEMENT ===");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Branch: " + branch);
        System.out.println("Current Balance: " + balance);
        System.out.println("\nTransaction History:");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        System.out.println("========================\n");
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }
}