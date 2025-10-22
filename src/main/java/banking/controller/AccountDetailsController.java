package banking.controller;

import banking.model.Account;
import banking.view.AccountDetailsView;
import javafx.stage.Stage;

public class AccountDetailsController {
    private Account account;
    private Stage stage;
    private CustomerDashboardController parentController;

    public AccountDetailsController(Account account, Stage stage, CustomerDashboardController parentController) {
        this.account = account;
        this.stage = stage;
        this.parentController = parentController;
    }

    public void handleDeposit(double amount) {
        if (account.deposit(amount)) {
            System.out.println("Deposit successful");
        } else {
            System.out.println("Deposit failed");
        }
    }

    public void handleWithdraw(double amount) {
        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful");
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