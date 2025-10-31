package banking.dao;

import banking.model.Account;
import java.util.List;

public interface AccountDAO {
    void saveAccount(Account account);
    Account findAccountByNumber(String accountNumber);
    List<Account> findAccountsByCustomer(String customerId);
    List<Account> findAllAccounts();
    void updateAccount(Account account);
}