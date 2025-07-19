package demo.boot.controller;

import demo.boot.client.AccountClient;
import demo.boot.model.BankAccount;
import demo.boot.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    // Injecting the Feign client
    private final AccountClient accountClient;

    // Static list to simulate a database of customers
    private static final List<Customer> customers = new ArrayList<>();

    static {
        // Prepopulate some customers in the list
        customers.add(new Customer("1", "Venkat", null));
        customers.add(new Customer("2", "Surya", null));
        customers.add(new Customer("3", "Devi", null));
    }

    @Autowired
    public CustomerController(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    // Get a single customer along with their account
    @GetMapping("/customer/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") String customerId) {
        // Search for the customer in the list
        Customer customer = customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // Fetch account details from AccountService using Feign Client
        BankAccount account = accountClient.getAccountByCustomerId(customerId);
        customer.setAccount(account);

        return customer;
    }

    // Get a list of all customers
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        // Fetch account details for each customer from AccountService using Feign Client
        for (Customer customer : customers) {
            if (customer.getAccount() == null) {
                BankAccount account = accountClient.getAccountByCustomerId(customer.getCustomerId());
                customer.setAccount(account);
            }
        }
        return customers;
    }
}