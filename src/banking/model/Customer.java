package banking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for customers
 * Demonstrates: Abstraction, Encapsulation
 */
public abstract class Customer {
    private String customerId;
    private String address;
    private String phoneNumber;
    private String email;
    private List<Account> accounts;
    
    public Customer(String customerId, String address, String phoneNumber, String email) {
        this.customerId = customerId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accounts = new ArrayList<>();
    }
    
    // Abstract method - must be implemented by subclasses
    public abstract String getName();
    
    // Getters
    public String getCustomerId() {
        return customerId;
    }
    
    public String getAddress() {
        return address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public List<Account> getAccounts() {
        return new ArrayList<>(accounts); // Return copy
    }
    
    // Business logic
    public void addAccount(Account account) {
        // Check if customer already has this account type
        for (Account acc : accounts) {
            if (acc.getClass().equals(account.getClass())) {
                System.out.println("Customer already has a " + account.getClass().getSimpleName());
                return;
            }
        }
        accounts.add(account);
        System.out.println("Account added successfully for " + getName());
    }
    
    public Account getAccountByNumber(String accountNumber) {
        for (Account acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber)) {
                return acc;
            }
        }
        return null;
    }
}