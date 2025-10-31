package banking.dao.impl;

import banking.dao.CustomerDAO;
import banking.dao.AccountDAO;
import banking.dao.TransactionDAO;
import banking.model.*;

public class DataInitializer {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public DataInitializer() {
        this.customerDAO = new TextFileCustomerDAO();
        this.accountDAO = new TextFileAccountDAO();
        this.transactionDAO = new TextFileTransactionDAO();
    }

    public void initializeSampleData() {
        System.out.println("Creating simplified sample data in text files...");

        // INDIVIDUAL CUSTOMER 1 - John Doe (3 accounts)
        IndividualCustomer john = new IndividualCustomer(
                "CUST001", "John", "Doe", "ID123456",
                "Plot 123, Gaborone", "71234567", "john.doe@email.com"
        );
        customerDAO.saveCustomer(john);

        SavingsAccount johnSavings = new SavingsAccount("ACC001", 1500.0, "Main Branch", john);
        InvestmentAccount johnInvestment = new InvestmentAccount("ACC002", 5000.0, "Main Branch", john);
        CheckingAccount johnChecking = new CheckingAccount("ACC003", 3000.0, "Main Branch", john,
                "Tech Solutions Ltd", "Plot 789, Gaborone");

        accountDAO.saveAccount(johnSavings);
        accountDAO.saveAccount(johnInvestment);
        accountDAO.saveAccount(johnChecking);

        // Add sample transactions for John
        johnSavings.deposit(1500.0);
        transactionDAO.saveTransaction(johnSavings.getTransactions().get(0), "ACC001");

        johnInvestment.deposit(5000.0);
        transactionDAO.saveTransaction(johnInvestment.getTransactions().get(0), "ACC002");

        johnChecking.deposit(3000.0);
        transactionDAO.saveTransaction(johnChecking.getTransactions().get(0), "ACC003");

        // INDIVIDUAL CUSTOMER 2 - Sarah Wilson (2 accounts)
        IndividualCustomer sarah = new IndividualCustomer(
                "CUST002", "Sarah", "Wilson", "ID789012",
                "Plot 456, Francistown", "71234568", "sarah.wilson@email.com"
        );
        customerDAO.saveCustomer(sarah);

        SavingsAccount sarahSavings = new SavingsAccount("ACC004", 2500.0, "Main Branch", sarah);
        CheckingAccount sarahChecking = new CheckingAccount("ACC005", 4000.0, "Main Branch", sarah,
                "Finance Corp", "Plot 321, Francistown");

        accountDAO.saveAccount(sarahSavings);
        accountDAO.saveAccount(sarahChecking);

        // Add sample transactions for Sarah
        sarahSavings.deposit(2500.0);
        transactionDAO.saveTransaction(sarahSavings.getTransactions().get(0), "ACC004");

        sarahChecking.deposit(4000.0);
        transactionDAO.saveTransaction(sarahChecking.getTransactions().get(0), "ACC005");

        // COMPANY CUSTOMER 1 - ACME Corporation (3 accounts)
        CompanyCustomer acmeCorp = new CompanyCustomer(
                "CUST003", "ACME Corporation", "CO123456",
                "Plot 789, Gaborone", "3901234", "info@acme.co.bw"
        );
        customerDAO.saveCustomer(acmeCorp);

        SavingsAccount acmeSavings = new SavingsAccount("ACC006", 15000.0, "Main Branch", acmeCorp);
        InvestmentAccount acmeInvestment = new InvestmentAccount("ACC007", 30000.0, "Main Branch", acmeCorp);
        CheckingAccount acmeChecking = new CheckingAccount("ACC008", 25000.0, "Main Branch", acmeCorp,
                "ACME Corporation", "Plot 789, Gaborone");

        accountDAO.saveAccount(acmeSavings);
        accountDAO.saveAccount(acmeInvestment);
        accountDAO.saveAccount(acmeChecking);

        // Add sample transactions for ACME
        acmeSavings.deposit(15000.0);
        transactionDAO.saveTransaction(acmeSavings.getTransactions().get(0), "ACC006");

        acmeInvestment.deposit(30000.0);
        transactionDAO.saveTransaction(acmeInvestment.getTransactions().get(0), "ACC007");

        acmeChecking.deposit(25000.0);
        transactionDAO.saveTransaction(acmeChecking.getTransactions().get(0), "ACC008");

        System.out.println("✅ Simplified sample data created successfully!");
        System.out.println("📊 3 customers, 8 accounts written to text files");
        System.out.println("💳 Account Types: 3 Savings, 2 Investment, 3 Checking");
    }
}