package banking.model;

import banking.util.PasswordUtil;

public class User {
    private String username;
    private String hashedPassword;
    private String role;
    private Customer customer;

    public User(String username, String password, String role) {
        this.username = username;
        this.hashedPassword = PasswordUtil.hashPassword(password);
        this.role = role;
    }

    public User(String username, String password, String role, Customer customer) {
        this(username, password, role);
        this.customer = customer;
    }

    // Getters
    public String getUsername() { return username; }
    public String getHashedPassword() { return hashedPassword; } // Made public for DAO
    public String getRole() { return role; }
    public Customer getCustomer() { return customer; }

    // Setters
    public void setPassword(String password) {
        this.hashedPassword = PasswordUtil.hashPassword(password);
    }

    public void setCustomer(Customer customer) { this.customer = customer; }

    // Business logic
    public boolean authenticate(String inputPassword) {
        return PasswordUtil.verifyPassword(inputPassword, hashedPassword);
    }

    public boolean isCustomer() {
        return "CUSTOMER".equals(role);
    }

    public boolean isTeller() {
        return "TELLER".equals(role);
    }

    @Override
    public String toString() {
        return String.format("User{username='%s', role='%s', customer=%s}",
                username, role, customer != null ? customer.getName() : "None");
    }
}