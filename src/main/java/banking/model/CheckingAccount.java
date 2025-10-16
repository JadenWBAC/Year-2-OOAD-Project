package banking.model;

/**
 * Checking account with no minimum balance or interest
 * Demonstrates: Inheritance, Method Overriding
 */
public class CheckingAccount extends Account {
    private String employerName;
    private String employerAddress;
    
    public CheckingAccount(String accountNumber, double initialBalance, String branch, 
                          Customer customer, String employerName, String employerAddress) {
        super(accountNumber, initialBalance, branch, customer);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }
    
    @Override
    public double calculateInterest() {
        return 0; // Checking accounts don't earn interest
    }
    
    @Override
    public double getMinimumBalance() {
        return 0; // No minimum balance
    }
    
    public String getEmployerName() {
        return employerName;
    }
    
    public String getEmployerAddress() {
        return employerAddress;
    }
}