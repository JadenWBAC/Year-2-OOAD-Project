package banking.controller;

import banking.model.*;
import banking.view.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class TellerDashboardController {
    private Stage stage;
    private List<Customer> customers;

    public TellerDashboardController(Stage stage) {
        this.stage = stage;
        this.customers = new ArrayList<>();
        // Add some sample customers for demonstration
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Individual Customer 1 - John Doe
        IndividualCustomer john = new IndividualCustomer(
                "CUST001", "John", "Doe", "ID123456",
                "Plot 123, Gaborone", "71234567", "john@email.com"
        );
        john.addAccount(new SavingsAccount("ACC001", 1500.0, "Main Branch", john));
        john.addAccount(new InvestmentAccount("ACC002", 5000.0, "Main Branch", john));
        john.addAccount(new CheckingAccount("ACC003", 3000.0, "Main Branch", john,
                "Tech Solutions Ltd", "Plot 789, Gaborone"));

        // Individual Customer 2 - Sarah Wilson (UPDATED with Investment Account)
        IndividualCustomer sarah = new IndividualCustomer(
                "CUST002", "Sarah", "Wilson", "ID789012",
                "Plot 456, Francistown", "71234568", "sarah@email.com"
        );
        sarah.addAccount(new SavingsAccount("ACC004", 2500.0, "Main Branch", sarah));
        sarah.addAccount(new InvestmentAccount("ACC005", 6000.0, "Main Branch", sarah)); // NEW Investment Account
        sarah.addAccount(new CheckingAccount("ACC006", 4000.0, "Main Branch", sarah,
                "Finance Corp", "Plot 321, Francistown"));

        // Company Customer - ACME Corporation (UPDATED with Investment Account)
        CompanyCustomer acmeCorp = new CompanyCustomer(
                "CUST003", "ACME Corporation", "CO123456",
                "Plot 789, Gaborone", "3901234", "info@acme.co.bw"
        );
        acmeCorp.addAccount(new SavingsAccount("ACC007", 15000.0, "Main Branch", acmeCorp));
        acmeCorp.addAccount(new InvestmentAccount("ACC008", 30000.0, "Main Branch", acmeCorp)); // NEW Investment Account
        acmeCorp.addAccount(new CheckingAccount("ACC009", 25000.0, "Main Branch", acmeCorp,
                "ACME Corporation", "Plot 789, Gaborone"));

        customers.add(john);
        customers.add(sarah);
        customers.add(acmeCorp);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers);
    }

    public Customer findCustomerById(String customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    public Customer findCustomerByAccount(String accountNumber) {
        for (Customer customer : customers) {
            if (customer.getAccountByNumber(accountNumber) != null) {
                return customer;
            }
        }
        return null;
    }

    public void createNewCustomer(Customer customer) {
        customers.add(customer);
    }

    public void openNewAccount(Customer customer, Account account) {
        customer.addAccount(account);
    }

    public boolean processDeposit(String accountNumber, double amount) {
        Customer customer = findCustomerByAccount(accountNumber);
        if (customer != null) {
            Account account = customer.getAccountByNumber(accountNumber);
            return account != null && account.deposit(amount);
        }
        return false;
    }

    public boolean processWithdrawal(String accountNumber, double amount) {
        Customer customer = findCustomerByAccount(accountNumber);
        if (customer != null) {
            Account account = customer.getAccountByNumber(accountNumber);
            return account != null && account.withdraw(amount);
        }
        return false;
    }

    public void applyInterestToAllAccounts() {
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                account.applyInterest();
            }
        }
    }
}