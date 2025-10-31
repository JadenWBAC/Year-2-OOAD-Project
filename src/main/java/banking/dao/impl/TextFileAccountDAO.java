package banking.dao.impl;

import banking.dao.AccountDAO;
import banking.dao.CustomerDAO;
import banking.dao.TransactionDAO; // ADD THIS IMPORT
import banking.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileAccountDAO implements AccountDAO {
    private static final String ACCOUNTS_FILE = "data/accounts.txt";
    private static final String DATA_DIR = "data";
    private CustomerDAO customerDAO;
    private TransactionDAO transactionDAO; // ADD THIS FIELD

    public TextFileAccountDAO() {
        createDataDirectory();
        this.customerDAO = new TextFileCustomerDAO();
        this.transactionDAO = new TextFileTransactionDAO(); // INITIALIZE IT
    }

    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void saveAccount(Account account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE, true))) {
            String employerName = "";
            String employerAddress = "";

            if (account instanceof CheckingAccount) {
                employerName = ((CheckingAccount) account).getEmployerName();
                employerAddress = ((CheckingAccount) account).getEmployerAddress();
            }

            String line = String.format("%s|%s|%s|%.2f|%s|%s|%s",
                    account.getAccountNumber(), account.getCustomer().getCustomerId(),
                    account.getClass().getSimpleName(), account.getBalance(),
                    account.getBranch(), employerName, employerAddress);

            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving account: " + e.getMessage());
        }
    }

    @Override
    public Account findAccountByNumber(String accountNumber) {
        List<Account> accounts = findAllAccounts();
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Account> findAccountsByCustomer(String customerId) {
        List<Account> allAccounts = findAllAccounts();
        List<Account> customerAccounts = new ArrayList<>();

        for (Account account : allAccounts) {
            if (account.getCustomer().getCustomerId().equals(customerId)) {
                customerAccounts.add(account);
            }
        }
        return customerAccounts;
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        File file = new File(ACCOUNTS_FILE);
        if (!file.exists()) return accounts;

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 5) {
                    String accountNumber = parts[0];
                    String customerId = parts[1];
                    String accountType = parts[2];
                    double balance = Double.parseDouble(parts[3]);
                    String branch = parts[4];

                    Customer customer = customerDAO.findCustomerById(customerId);
                    if (customer == null) continue;

                    Account account;
                    switch (accountType) {
                        case "SavingsAccount":
                            account = new SavingsAccount(accountNumber, balance, branch, customer);
                            break;
                        case "InvestmentAccount":
                            account = new InvestmentAccount(accountNumber, balance, branch, customer);
                            break;
                        case "CheckingAccount":
                            String employerName = parts.length > 5 ? parts[5] : "";
                            String employerAddress = parts.length > 6 ? parts[6] : "";
                            account = new CheckingAccount(accountNumber, balance, branch, customer, employerName, employerAddress);
                            break;
                        default:
                            continue;
                    }

                    // Load transactions for this account
                    loadTransactionsForAccount(account);
                    accounts.add(account);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading accounts: " + e.getMessage());
        }
        return accounts;
    }

    @Override
    public void updateAccount(Account account) {
        List<Account> allAccounts = findAllAccounts();
        allAccounts.removeIf(a -> a.getAccountNumber().equals(account.getAccountNumber()));
        allAccounts.add(account);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE))) {
            for (Account acc : allAccounts) {
                String employerName = "";
                String employerAddress = "";

                if (acc instanceof CheckingAccount) {
                    employerName = ((CheckingAccount) acc).getEmployerName();
                    employerAddress = ((CheckingAccount) acc).getEmployerAddress();
                }

                String line = String.format("%s|%s|%s|%.2f|%s|%s|%s",
                        acc.getAccountNumber(), acc.getCustomer().getCustomerId(),
                        acc.getClass().getSimpleName(), acc.getBalance(),
                        acc.getBranch(), employerName, employerAddress);

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating account: " + e.getMessage());
        }
    }

    private void loadTransactionsForAccount(Account account) {
        List<Transaction> transactions = transactionDAO.findTransactionsByAccount(account.getAccountNumber());

        // Set transactions using reflection
        try {
            java.lang.reflect.Method setTransactions = Account.class.getDeclaredMethod("setTransactions", List.class);
            setTransactions.setAccessible(true);
            setTransactions.invoke(account, transactions);
        } catch (Exception e) {
            System.err.println("Could not load transactions for account: " + e.getMessage());
        }
    }
}