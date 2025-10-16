package banking.model;

/**
 * Company customer with business details
 * Demonstrates: Inheritance, Method Overriding, Polymorphism
 */
public class CompanyCustomer extends Customer {
    private String companyName;
    private String companyNumber;
    
    public CompanyCustomer(String customerId, String companyName, String companyNumber,
                          String address, String phoneNumber, String email) {
        super(customerId, address, phoneNumber, email);
        this.companyName = companyName;
        this.companyNumber = companyNumber;
    }
    
    @Override
    public String getName() {
        return companyName;
    }
    
    public String getCompanyNumber() {
        return companyNumber;
    }
}
