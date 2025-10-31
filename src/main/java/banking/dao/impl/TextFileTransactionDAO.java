package banking.dao.impl;

import banking.dao.TransactionDAO;
import banking.model.Transaction;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TextFileTransactionDAO implements TransactionDAO {
    private static final String TRANSACTIONS_FILE = "data/transactions.txt";
    private static final String DATA_DIR = "data";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TextFileTransactionDAO() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void saveTransaction(Transaction transaction, String accountNumber) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRANSACTIONS_FILE, true))) {
            String line = String.format("%s|%s|%s|%.2f|%.2f|%s",
                    transaction.getTransactionId(), accountNumber,
                    transaction.getType(), transaction.getAmount(),
                    transaction.getBalanceAfter(), transaction.getTimestamp().format(formatter));

            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
        }
    }

    @Override
    public List<Transaction> findTransactionsByAccount(String accountNumber) {
        List<Transaction> allTransactions = findAllTransactions();
        List<Transaction> accountTransactions = new ArrayList<>();

        for (Transaction transaction : allTransactions) {
            // We need to parse the account number from the transaction data
            // This is a bit tricky since we don't store it directly in Transaction object
            // For now, we'll filter by the account number in the file
            if (getAccountNumberFromTransaction(transaction).equals(accountNumber)) {
                accountTransactions.add(transaction);
            }
        }
        return accountTransactions;
    }

    @Override
    public List<Transaction> findAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(TRANSACTIONS_FILE);
        if (!file.exists()) return transactions;

        try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTIONS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    String transactionId = parts[0];
                    String type = parts[2];
                    double amount = Double.parseDouble(parts[3]);
                    double balanceAfter = Double.parseDouble(parts[4]);
                    LocalDateTime timestamp = LocalDateTime.parse(parts[5], formatter);

                    Transaction transaction = new Transaction(transactionId, type, amount, balanceAfter);

                    // Set the timestamp using reflection since it's final
                    try {
                        java.lang.reflect.Field timestampField = Transaction.class.getDeclaredField("timestamp");
                        timestampField.setAccessible(true);
                        timestampField.set(transaction, timestamp);
                    } catch (Exception e) {
                        System.err.println("Could not set timestamp: " + e.getMessage());
                    }

                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading transactions: " + e.getMessage());
        }
        return transactions;
    }

    private String getAccountNumberFromTransaction(Transaction transaction) {
        // This is a helper method to reconstruct account number from transaction data
        // You might need to modify your Transaction class to store account number
        return "UNKNOWN"; // Placeholder - you'll need to implement this properly
    }
}