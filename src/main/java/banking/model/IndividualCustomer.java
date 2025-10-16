package banking.model;

/**
 * Individual customer with personal details
 * Demonstrates: Inheritance, Method Overriding
 */
public class IndividualCustomer extends Customer {
    private String firstName;
    private String surname;
    private String nationalId;
    
    public IndividualCustomer(String customerId, String firstName, String surname, 
                             String nationalId, String address, String phoneNumber, String email) {
        super(customerId, address, phoneNumber, email);
        this.firstName = firstName;
        this.surname = surname;
        this.nationalId = nationalId;
    }
    
    @Override
    public String getName() {
        return firstName + " " + surname;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public String getNationalId() {
        return nationalId;
    }
}