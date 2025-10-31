package banking.dao;

import banking.model.Customer;
import java.util.List;

public interface CustomerDAO {
    void saveCustomer(Customer customer);
    Customer findCustomerById(String customerId);
    List<Customer> findAllCustomers();
    void updateCustomer(Customer customer);
}