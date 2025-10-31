package banking.dao;

import banking.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    void saveTransaction(Transaction transaction, String accountNumber);
    List<Transaction> findTransactionsByAccount(String accountNumber);
    List<Transaction> findAllTransactions();
}