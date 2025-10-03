package banking.model;

/**
 * Savings account with minimum balance and interest
 * Demonstrates: Inheritance, Method Overriding
 */
public class SavingsAccount extends Account {
    private static final double MINIMUM_BALANCE = 50.0;
    
    public SavingsAccount(String accountNumber, double initialBalance, String branch, Customer customer) {
        super(accountNumber, initialBalance, branch, customer);
        
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException("Initial balance must be at least " + MINIMUM_BALANCE + " pula");
        }
    }
    
    @Override
    public double calculateInterest() {
        // Different rates for Individual vs Company customers (Polymorphism)
        if (getCustomer() instanceof IndividualCustomer) {
            return getBalance() * 0.00025; // 0.025% monthly
        } else if (getCustomer() instanceof CompanyCustomer) {
            return getBalance() * 0.00075; // 0.075% monthly
        }
        return 0;
    }
    
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}