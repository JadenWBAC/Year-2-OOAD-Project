package banking.controller;

import banking.dao.AccountDAO;
import banking.dao.impl.TextFileAccountDAO;
import banking.model.Account;
import banking.view.AccountDetailsView;
import javafx.stage.Stage;

public class AccountDetailsController {
    private Account account;
    private Stage stage;
    private CustomerDashboardController parentController;
    private AccountDAO accountDAO;

    public AccountDetailsController(Account account, Stage stage, CustomerDashboardController parentController) {
        this.account = account;
        this.stage = stage;
        this.parentController = parentController;
        this.accountDAO = new TextFileAccountDAO();
    }

    public void handleDeposit(double amount) {
        if (account.deposit(amount)) {
            // Save the updated account to file (this will also save the new transaction)
            accountDAO.updateAccount(account);
            System.out.println("Deposit successful: P" + amount + " to account " + account.getAccountNumber());
        } else {
            System.out.println("Deposit failed");
        }
    }

    public void handleWithdraw(double amount) {
        if (account.withdraw(amount)) {
            // Save the updated account to file (this will also save the new transaction)
            accountDAO.updateAccount(account);
            System.out.println("Withdrawal successful: P" + amount + " from account " + account.getAccountNumber());
        } else {
            System.out.println("Withdrawal failed");
        }
    }

    public void handlePrintStatement() {
        account.printStatement();
    }

    public Account getAccount() {
        return account;
    }

    public CustomerDashboardController getParentController() {
        return parentController;
    }
}