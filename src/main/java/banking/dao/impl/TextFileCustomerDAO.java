package banking.dao.impl;

import banking.dao.CustomerDAO;
import banking.model.Customer;
import banking.model.IndividualCustomer;
import banking.model.CompanyCustomer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextFileCustomerDAO implements CustomerDAO {
    private static final String CUSTOMERS_FILE = "data/customers.txt";
    private static final String DATA_DIR = "data";

    public TextFileCustomerDAO() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void saveCustomer(Customer customer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE, true))) {
            String line;
            if (customer instanceof IndividualCustomer) {
                IndividualCustomer ind = (IndividualCustomer) customer;
                line = String.format("%s|INDIVIDUAL|%s|%s|%s|%s|%s|%s",
                        ind.getCustomerId(), ind.getFirstName(), ind.getSurname(),
                        ind.getNationalId(), ind.getAddress(), ind.getPhoneNumber(), ind.getEmail());
            } else {
                CompanyCustomer comp = (CompanyCustomer) customer;
                line = String.format("%s|COMPANY|%s|%s|%s|%s|%s",
                        comp.getCustomerId(), comp.getName(), comp.getCompanyNumber(),
                        comp.getAddress(), comp.getPhoneNumber(), comp.getEmail());
            }
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving customer: " + e.getMessage());
        }
    }

    @Override
    public Customer findCustomerById(String customerId) {
        List<Customer> customers = findAllCustomers();
        return customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Customer> findAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return customers;

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    if ("INDIVIDUAL".equals(parts[1])) {
                        IndividualCustomer customer = new IndividualCustomer(
                                parts[0], parts[2], parts[3], parts[4],
                                parts[5], parts[6], parts[7]
                        );
                        customers.add(customer);
                    } else if ("COMPANY".equals(parts[1])) {
                        CompanyCustomer customer = new CompanyCustomer(
                                parts[0], parts[2], parts[3],
                                parts[4], parts[5], parts[6]
                        );
                        customers.add(customer);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading customers: " + e.getMessage());
        }
        return customers;
    }

    @Override
    public void updateCustomer(Customer customer) {
        // For simplicity, we'll rewrite the entire file
        List<Customer> allCustomers = findAllCustomers();
        // Remove the old version of this customer
        allCustomers.removeIf(c -> c.getCustomerId().equals(customer.getCustomerId()));
        // Add the updated customer
        allCustomers.add(customer);

        // Rewrite the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer cust : allCustomers) {
                String line;
                if (cust instanceof IndividualCustomer) {
                    IndividualCustomer ind = (IndividualCustomer) cust;
                    line = String.format("%s|INDIVIDUAL|%s|%s|%s|%s|%s|%s",
                            ind.getCustomerId(), ind.getFirstName(), ind.getSurname(),
                            ind.getNationalId(), ind.getAddress(), ind.getPhoneNumber(), ind.getEmail());
                } else {
                    CompanyCustomer comp = (CompanyCustomer) cust;
                    line = String.format("%s|COMPANY|%s|%s|%s|%s|%s",
                            comp.getCustomerId(), comp.getName(), comp.getCompanyNumber(),
                            comp.getAddress(), comp.getPhoneNumber(), comp.getEmail());
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating customer: " + e.getMessage());
        }
    }
}