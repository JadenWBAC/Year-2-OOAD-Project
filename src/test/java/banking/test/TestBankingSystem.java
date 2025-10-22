package banking.test;

import banking.model.*;

/**
 * Test class to demonstrate the core model functionality
 */
public class TestBankingSystem {
    public static void main(String[] args) {
        System.out.println("=== BANKING SYSTEM CORE MODEL TEST ===\n");

        // Create Individual Customer
        IndividualCustomer john = new IndividualCustomer(
                "CUST001",
                "John",
                "Doe",
                "123456789",
                "Plot 123, Gaborone",
                "71234567",
                "john@email.com"
        );

        // Create Company Customer
        CompanyCustomer acmeCorp = new CompanyCustomer(
                "CUST002",
                "ACME Corporation",
                "CO123456",
                "Plot 456, Gaborone",
                "3901234",
                "info@acme.co.bw"
        );

        // Create accounts for John
        System.out.println("--- Creating accounts for " + john.getName() + " ---");
        SavingsAccount johnSavings = new SavingsAccount("ACC001", 1000, "Main Branch", john);
        InvestmentAccount johnInvestment = new InvestmentAccount("ACC002", 5000, "Main Branch", john);
        CheckingAccount johnChecking = new CheckingAccount("ACC003", 0, "Main Branch", john,
                "Tech Company Ltd", "Plot 789, Gaborone");

        john.addAccount(johnSavings);
        john.addAccount(johnInvestment);
        john.addAccount(johnChecking);

        // Create account for ACME
        System.out.println("\n--- Creating account for " + acmeCorp.getName() + " ---");
        SavingsAccount acmeSavings = new SavingsAccount("ACC004", 10000, "Main Branch", acmeCorp);
        CheckingAccount acmeChecking = new CheckingAccount("ACC005", 0, "Main Branch", acmeCorp,
                "ACME Corporation", "Plot 456, Gaborone");

        acmeCorp.addAccount(acmeSavings);
        acmeCorp.addAccount(acmeChecking);

        // Test deposits
        System.out.println("\n--- Testing Deposits ---");
        johnSavings.deposit(500);
        acmeSavings.deposit(2000);
        johnChecking.deposit(3000); // Salary deposit
        acmeChecking.deposit(15000); // Business deposit

        // Test withdrawals
        System.out.println("\n--- Testing Withdrawals ---");
        johnInvestment.withdraw(1000);
        johnSavings.withdraw(2000); // This should fail - below minimum
        johnChecking.withdraw(500); // Should work - no minimum balance

        // Test interest calculation for different account types
        System.out.println("\n--- Testing Interest Calculation ---");
        johnSavings.applyInterest(); // Individual rate: 0.025%
        acmeSavings.applyInterest(); // Company rate: 0.075%
        johnInvestment.applyInterest(); // Investment rate: 5%
        johnChecking.applyInterest(); // Should be 0 (no interest)

        // Print statements
        System.out.println("\n--- Account Statements ---");
        johnSavings.printStatement();
        johnInvestment.printStatement();
        johnChecking.printStatement();
        acmeSavings.printStatement();
        acmeChecking.printStatement();

        System.out.println("\n=== TEST COMPLETE ===");
    }
}