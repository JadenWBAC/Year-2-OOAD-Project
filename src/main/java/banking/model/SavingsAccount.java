package banking.model;

/**
 * SavingsAccount - Concrete Model Class
 * Represents a savings account with minimum balance and interest
 *
 * Business Rules (from Assignment):
 * - Minimum balance: P50.00
 * - Monthly interest: 0.05% (0.0005 as decimal)
 * - NO WITHDRAWALS ALLOWED (savings only - money is deposited for future use)
 * - Interest rate depends on customer type (currently same for both)
 *
 * OOP Principles Demonstrated:
 * - Inheritance: Extends Account class
 * - Method Overriding: Overrides abstract methods from parent
 * - Encapsulation: Private constants, public methods
 *
 * @author [Your Name]
 * @version 1.0
 * @since Part B - Week Oct. 06-10
 */
public class SavingsAccount extends Account {
    // ===== CLASS CONSTANTS =====
    // FIXED: Corrected to match assignment requirement (0.05% = 0.0005)
    private static final double MINIMUM_BALANCE = 50.0;  // Minimum P50 balance

    /**
     * Constructor - Create a new Savings Account
     *
     * @param accountNumber Unique account identifier
     * @param initialBalance Starting balance
     * @param branch Bank branch name
     * @param customer Customer who owns this account
     * @throws IllegalArgumentException if initial balance < minimum balance
     */
    public SavingsAccount(String accountNumber, double initialBalance,
                          String branch, Customer customer) {
        // Call parent constructor
        super(accountNumber, initialBalance, branch, customer);

        // BUSINESS RULE: Validate minimum balance requirement
        if (initialBalance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                    "Initial balance must be at least " + MINIMUM_BALANCE + " pula");
        }
    }

    /**
     * Calculate monthly interest for this account
     *
     * Assignment Requirement:
     * - 0.05% monthly interest for savings accounts
     *
     * @return Interest amount to be credited
     */
    @Override
    public double calculateInterest() {
        // Fixed interest rate: 0.05% monthly = 0.0005 as decimal
        // Note: Assignment originally showed different rates for Individual/Company
        // but specifies 0.05% for SavingsAccount in requirements
        return getBalance() * 0.0005;  // 0.05% = 0.0005
    }

    /**
     * Get the minimum balance requirement
     *
     * @return Minimum balance (P50.00)
     */
    @Override
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }

    /**
     * Override withdraw method to PREVENT withdrawals
     *
     * CRITICAL BUSINESS RULE from Assignment:
     * "A Savings account where a customer may deposit funds for future use.
     *  This account does not allow any withdrawals from it."
     *
     * @param amount Amount to withdraw
     * @return false (always fails - withdrawals not allowed)
     */
    @Override
    public boolean withdraw(double amount) {
        // BUSINESS RULE: Savings accounts DO NOT allow withdrawals
        System.out.println("ERROR: Withdrawals are not allowed from Savings accounts.");
        System.out.println("Savings accounts are for depositing funds for future use only.");
        return false;  // Transaction fails
    }
}
