package banking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transaction record for audit trail
 * Demonstrates: Encapsulation
 */
public class Transaction {
    private String transactionId;
    private String type; // DEPOSIT, WITHDRAWAL, INTEREST
    private double amount;
    private double balanceAfter;
    private LocalDateTime timestamp;
    
    public Transaction(String transactionId, String type, double amount, double balanceAfter) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public String getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("%s | %s | Amount: %.2f | Balance: %.2f | %s",
            transactionId, type, amount, balanceAfter, timestamp.format(formatter));
    }
}