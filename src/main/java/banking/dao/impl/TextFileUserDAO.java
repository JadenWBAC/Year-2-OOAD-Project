package banking.dao.impl;

import banking.dao.UserDAO;
import banking.dao.CustomerDAO;
import banking.model.User;
import banking.model.Customer;
import banking.util.PasswordUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileUserDAO implements UserDAO {
    private static final String USERS_FILE = "data/users.txt";
    private static final String DATA_DIR = "data";
    private CustomerDAO customerDAO;

    public TextFileUserDAO() {
        createDataDirectory();
        this.customerDAO = new TextFileCustomerDAO();
    }

    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void saveUser(User user) {
        // Check if user already exists
        User existing = findUserByUsername(user.getUsername());
        if (existing != null) {
            updateUser(user); // Update existing user
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            String customerId = user.getCustomer() != null ? user.getCustomer().getCustomerId() : "";
            String line = String.format("%s|%s|%s|%s",
                    user.getUsername(),
                    user.getHashedPassword(), // Store hashed password
                    user.getRole(),
                    customerId);

            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    @Override
    public User findUserByUsername(String username) {
        List<User> users = findAllUsers();
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_FILE);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String username = parts[0];
                    String hashedPassword = parts[1];
                    String role = parts[2];
                    String customerId = parts.length > 3 ? parts[3] : "";

                    Customer customer = null;
                    if (!customerId.isEmpty()) {
                        customer = customerDAO.findCustomerById(customerId);
                        if (customer == null) {
                            System.err.println("Warning: Customer not found for ID: " + customerId);
                        }
                    }

                    // Create user with dummy password (we'll set the real hashed password)
                    User user = new User(username, "dummy", role, customer);

                    // Set the actual hashed password from file
                    setHashedPasswordDirectly(user, hashedPassword);

                    users.add(user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        List<User> allUsers = findAllUsers();
        allUsers.removeIf(u -> u.getUsername().equals(user.getUsername()));
        allUsers.add(user);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User u : allUsers) {
                String customerId = u.getCustomer() != null ? u.getCustomer().getCustomerId() : "";
                String line = String.format("%s|%s|%s|%s",
                        u.getUsername(),
                        u.getHashedPassword(),
                        u.getRole(),
                        customerId);

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        User user = findUserByUsername(username);
        return user != null && user.authenticate(password);
    }

    /**
     * Helper method to set hashed password directly (for loading from file)
     */
    private void setHashedPasswordDirectly(User user, String hashedPassword) {
        try {
            java.lang.reflect.Field passwordField = User.class.getDeclaredField("hashedPassword");
            passwordField.setAccessible(true);
            passwordField.set(user, hashedPassword);
        } catch (Exception e) {
            System.err.println("Could not set hashed password for user: " + user.getUsername());
        }
    }

    /**
     * Link a customer to a user account
     */
    public boolean linkCustomerToUser(String username, String customerId) {
        User user = findUserByUsername(username);
        if (user == null) {
            return false;
        }

        Customer customer = customerDAO.findCustomerById(customerId);
        if (customer == null) {
            return false;
        }

        user.setCustomer(customer);
        updateUser(user);
        return true;
    }

    /**
     * Find user by customer ID (reverse lookup)
     */
    public User findUserByCustomerId(String customerId) {
        List<User> users = findAllUsers();
        return users.stream()
                .filter(u -> u.getCustomer() != null && u.getCustomer().getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }
}