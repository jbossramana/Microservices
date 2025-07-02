package demo.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import demo.boot.model.BankAccount;
import demo.boot.model.Customer;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerControllerNew {

    private final RestClient restClient;

    // Static list to simulate a database of customers
    private static final List<Customer> customers = new ArrayList<>();

    static {
        customers.add(new Customer("1", "Venkat", null));
        customers.add(new Customer("2", "Surya", null));
        customers.add(new Customer("3", "Devi", null));
    }

    @Autowired
    public CustomerController(RestClient restClient) {
        this.restClient = restClient;
    }

    // Get a single customer along with their account
    @GetMapping("/customer/{customerId}")
    public Customer getCustomer(@PathVariable("customerId") String customerId) {
        Customer customer = customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // Fetch account details using RestClient
        BankAccount account = restClient.get()
                .uri("http://AccountService/account/" + customerId)
                .retrieve()
                .body(BankAccount.class);

        customer.setAccount(account);
        return customer;
    }

    // Get a list of all customers
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        for (Customer customer : customers) {
            if (customer.getAccount() == null) {
                BankAccount account = restClient.get()
                        .uri("http://AccountService/account/" + customer.getCustomerId())
                        .retrieve()
                        .body(BankAccount.class);
                customer.setAccount(account);
            }
        }
        return customers;
    }
}
