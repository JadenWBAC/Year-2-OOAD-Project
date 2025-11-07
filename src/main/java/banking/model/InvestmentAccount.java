package banking.model;

/**
 * InvestmentAccount - Concrete Model Class
 * Represents an investment account with higher minimum balance and interest
 *
 */
public class InvestmentAccount extends Account {
    // ===== CLASS CONSTANTS =====
    private static final double MINIMUM_BALANCE = 500.0;  // Minimum P500 balance
    private static final double INTEREST_RATE = 0.05;     // 5% monthly interest

    /**
     * Constructor - Create a new Investment Account
     * "The account can only be opened with an initial deposit of BWP500.00"
     */
    public InvestmentAccount(String accountNumber, double initialBalance,
                             String branch, Customer customer) {
        // Call parent constructor
        super(accountNumber, initialBalance, branch, customer);

        // BUSINESS RULE: Validate minimum opening balance
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                    "Initial balance must be at least " + MINIMUM_BALANCE + " pula");
        }
    }

    /**
     * Calculate monthly interest for this account
     * "The Investment account which is an account that pays more interest
     *  than the savings account" - 5% monthly
     */
    @Override
    public double calculateInterest() {
        // 5% monthly interest
        return getBalance() * INTEREST_RATE;
    }

    /**
     * Get the minimum balance requirement
     */
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    // NOTE: This class uses the default withdraw() method from Account class
    // which allows withdrawals as long as balance stays above minimum
}
