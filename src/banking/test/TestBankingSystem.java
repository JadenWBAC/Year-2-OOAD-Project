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
        
        john.addAccount(johnSavings);
        john.addAccount(johnInvestment);
        
        // Create account for ACME
        System.out.println("\n--- Creating account for " + acmeCorp.getName() + " ---");
        SavingsAccount acmeSavings = new SavingsAccount("ACC003", 10000, "Main Branch", acmeCorp);
        acmeCorp.addAccount(acmeSavings);
        
        // Test deposits
        System.out.println("\n--- Testing Deposits ---");
        johnSavings.deposit(500);
        acmeSavings.deposit(2000);
        
        // Test withdrawals
        System.out.println("\n--- Testing Withdrawals ---");
        johnInvestment.withdraw(1000);
        johnSavings.withdraw(2000); // This should fail - below minimum
        
        // Test interest calculation (Polymorphism in action!)
        System.out.println("\n--- Testing Interest Calculation ---");
        johnSavings.applyInterest(); // Individual rate: 0.025%
        acmeSavings.applyInterest(); // Company rate: 0.075%
        johnInvestment.applyInterest(); // Investment rate: 5%
        
        // Print statements
        System.out.println("\n--- Account Statements ---");
        johnSavings.printStatement();
        johnInvestment.printStatement();
        acmeSavings.printStatement();
        
        // Test checking account
        System.out.println("\n--- Creating Checking Account ---");
        CheckingAccount johnChecking = new CheckingAccount(
            "ACC004", 0, "Main Branch", john, 
            "Tech Company Ltd", "Plot 789, Gaborone"
        );
        john.addAccount(johnChecking);
        johnChecking.deposit(3000);
        johnChecking.withdraw(500);
        johnChecking.applyInterest(); // Should be 0
        johnChecking.printStatement();
        
        System.out.println("\n=== TEST COMPLETE ===");
    }
}