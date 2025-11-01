package banking.util;

import banking.dao.*;
import banking.dao.impl.*;
import banking.model.*;

import java.io.File;

/**
 * Initializes the banking system with sample data
 * Only runs if data files don't exist
 */
public class DataInitializer {

    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private UserDAO userDAO;

    public DataInitializer() {
        this.customerDAO = new TextFileCustomerDAO();
        this.accountDAO = new TextFileAccountDAO();
        this.userDAO = new TextFileUserDAO();
    }

    /**
     * Initialize sample data if files don't exist
     */
    public void initializeData() {
        // Check if data already exists
        if (new File("data/customers.txt").exists()) {
            System.out.println("Data files already exist. Skipping initialization.");
            return;
        }

        System.out.println("Initializing sample data...");

        // Create sample individual customers
        IndividualCustomer customer1 = createIndividualCustomer1();
        IndividualCustomer customer2 = createIndividualCustomer2();
        IndividualCustomer customer3 = createIndividualCustomer3();

        // Create sample company customer
        CompanyCustomer company1 = createCompanyCustomer1();

        // Save customers
        customerDAO.saveCustomer(customer1);
        customerDAO.saveCustomer(customer2);
        customerDAO.saveCustomer(customer3);
        customerDAO.saveCustomer(company1);

        // Create and save accounts for each customer
        createAccountsForCustomer1(customer1);
        createAccountsForCustomer2(customer2);
        createAccountsForCustomer3(customer3);
        createAccountsForCompany1(company1);

        // Create sample users (linked to customers and standalone tellers)
        createUsers(customer1, customer2, customer3);

        System.out.println("Sample data initialized successfully!");
    }

    private IndividualCustomer createIndividualCustomer1() {
        return new IndividualCustomer(
                "CUST001",
                "Jacob",
                "Smith",
                "ID123456",
                "Plot 123, Gaborone",
                "71234567",
                "jacob@email.com"
        );
    }

    private IndividualCustomer createIndividualCustomer2() {
        return new IndividualCustomer(
                "CUST002",
                "Theo",
                "Johnson",
                "ID789012",
                "Plot 456, Francistown",
                "71234568",
                "theo@email.com"
        );
    }

    private IndividualCustomer createIndividualCustomer3() {
        return new IndividualCustomer(
                "CUST003",
                "Sarah",
                "Williams",
                "ID345678",
                "Plot 789, Maun",
                "71234569",
                "sarah@email.com"
        );
    }

    private CompanyCustomer createCompanyCustomer1() {
        return new CompanyCustomer(
                "CUST004",
                "TechSolutions Ltd",
                "BW000123456",
                "Plot 321, Gaborone CBD",
                "3901234",
                "info@techsolutions.bw"
        );
    }

    private void createAccountsForCustomer1(IndividualCustomer customer) {
        // Savings Account
        SavingsAccount savings = new SavingsAccount("ACC001", 1500.0, "Main Branch", customer);
        savings.deposit(500.0); // Add a transaction
        accountDAO.saveAccount(savings);

        // Investment Account
        InvestmentAccount investment = new InvestmentAccount("ACC002", 5000.0, "Main Branch", customer);
        investment.deposit(1000.0);
        accountDAO.saveAccount(investment);

        // Checking Account
        CheckingAccount checking = new CheckingAccount("ACC003", 3000.0, "Main Branch", customer,
                "Tech Solutions Ltd", "Plot 789, Gaborone");
        checking.deposit(2000.0);
        accountDAO.saveAccount(checking);
    }

    private void createAccountsForCustomer2(IndividualCustomer customer) {
        // Savings Account
        SavingsAccount savings = new SavingsAccount("ACC004", 2500.0, "Main Branch", customer);
        savings.deposit(1000.0);
        accountDAO.saveAccount(savings);

        // Investment Account
        InvestmentAccount investment = new InvestmentAccount("ACC005", 7500.0, "Main Branch", customer);
        investment.deposit(2500.0);
        accountDAO.saveAccount(investment);

        // Checking Account
        CheckingAccount checking = new CheckingAccount("ACC006", 4500.0, "Main Branch", customer,
                "Finance Corp", "Plot 321, Francistown");
        checking.deposit(1500.0);
        accountDAO.saveAccount(checking);
    }

    private void createAccountsForCustomer3(IndividualCustomer customer) {
        // Savings Account
        SavingsAccount savings = new SavingsAccount("ACC007", 1000.0, "Maun Branch", customer);
        accountDAO.saveAccount(savings);

        // Checking Account
        CheckingAccount checking = new CheckingAccount("ACC008", 2000.0, "Maun Branch", customer,
                "Safari Tours Ltd", "Plot 456, Maun");
        accountDAO.saveAccount(checking);
    }

    private void createAccountsForCompany1(CompanyCustomer customer) {
        // Company Savings Account
        SavingsAccount savings = new SavingsAccount("ACC009", 50000.0, "Main Branch", customer);
        accountDAO.saveAccount(savings);

        // Company Checking Account
        CheckingAccount checking = new CheckingAccount("ACC010", 75000.0, "Main Branch", customer,
                "Self Employed", "Plot 321, Gaborone CBD");
        accountDAO.saveAccount(checking);
    }

    private void createUsers(IndividualCustomer customer1, IndividualCustomer customer2,
                             IndividualCustomer customer3) {
        // Customer users (linked to customer accounts)
        User user1 = new User("Jacob", "123445", "CUSTOMER", customer1);
        userDAO.saveUser(user1);

        User user2 = new User("Theo", "Theo2024", "CUSTOMER", customer2);
        userDAO.saveUser(user2);

        User user3 = new User("Sarah", "sarah123", "CUSTOMER", customer3);
        userDAO.saveUser(user3);

        // Bank teller users (no customer association)
        User teller1 = new User("Jaden", "021103", "TELLER");
        userDAO.saveUser(teller1);

        User teller2 = new User("Admin", "admin123", "TELLER");
        userDAO.saveUser(teller2);

        System.out.println("Sample users created:");
        System.out.println("Customers: Jacob/123445, Theo/Theo2024, Sarah/sarah123");
        System.out.println("Tellers: Jaden/021103, Admin/admin123");
    }
}