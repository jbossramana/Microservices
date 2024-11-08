package demo.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import demo.boot.model.BankAccount;
import demo.boot.model.Customer;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

    private final RestTemplate restTemplate;

    // Static list to simulate a database of customers
    private static final List<Customer> customers = new ArrayList<>();

    static {
        // Prepopulate some customers in the list
        customers.add(new Customer("1", "Venkat", null));
        customers.add(new Customer("2", "Surya", null));
        customers.add(new Customer("3", "Devi", null));
        //customers.add(new Customer("4", "John", null));
    }

    @Autowired
    public CustomerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Get a single customer along with their account
    @GetMapping("/customer/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") String customerId) {
        // Search for the customer in the list
        Customer customer = customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // Fetch account details from AccountService using RestTemplate
        BankAccount account = restTemplate.getForObject("http://AccountService/account/" + customerId, BankAccount.class);
        customer.setAccount(account);

        return customer;
    }

    // Get a list of all customers
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        // Fetch account details for each customer from AccountService
        for (Customer customer : customers) {
            if (customer.getAccount() == null) {
                BankAccount account = restTemplate.getForObject("http://AccountService/account/" + customer.getCustomerId(), BankAccount.class);
                customer.setAccount(account);
            }
        }
        return customers;
    }
}
