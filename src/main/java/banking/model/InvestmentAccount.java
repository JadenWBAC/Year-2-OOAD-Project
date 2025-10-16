package banking.model;

/**
 * Investment account with higher minimum and interest rate
 * Demonstrates: Inheritance, Method Overriding
 */
public class InvestmentAccount extends Account {
    private static final double MINIMUM_BALANCE = 500.0;
    private static final double INTEREST_RATE = 0.05; // 5% monthly
    
    public InvestmentAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer);
        
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Initial balance must be at least " + MINIMUM_BALANCE + " pula");
        }
    }
    
    @Override
    public double calculateInterest() {
        return getBalance() * INTEREST_RATE;
    }
    
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}