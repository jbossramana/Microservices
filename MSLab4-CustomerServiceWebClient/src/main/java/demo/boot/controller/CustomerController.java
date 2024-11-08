package demo.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import demo.boot.model.BankAccount;
import demo.boot.model.Customer;
import reactor.core.publisher.Mono;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {

	@Autowired
    private WebClient.Builder webClientBuilder;

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
    public CustomerController(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    // Circuit Breaker applied here to handle failures when calling AccountService
    @CircuitBreaker(name = "accountService", fallbackMethod = "accountServiceFallbackForCustomer")
    @GetMapping("/customer/{customerId}")
    public Mono<Customer> getCustomer(@PathVariable("customerId") String customerId) {
        // Search for the customer in the list
        Customer customer = customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        // Fetch account details asynchronously from AccountService using WebClient
        return webClientBuilder.baseUrl("http://AccountService")
                .build()
                .get()
                .uri("/account/{customerId}", customerId)
                .retrieve()
                .bodyToMono(BankAccount.class)
                .defaultIfEmpty(new BankAccount()) // Return a default BankAccount if response is empty
                .map(account -> {
                    customer.setAccount(account);
                    return customer;
                })
                .onErrorResume(e -> accountServiceFallbackForCustomer(customerId, e)); // Handle errors
    }

    // Circuit Breaker applied to getAllCustomers as well
    @CircuitBreaker(name = "accountService", fallbackMethod = "accountServiceFallbackForCustomers")
    @GetMapping("/customers")
    public Mono<List<Customer>> getAllCustomers() {
        // Create a list of Mono<Customer> for each customer to fetch account details asynchronously
        List<Mono<Customer>> customerMonos = new ArrayList<>();
        
        for (Customer customer : customers) {
            Mono<Customer> customerMono = webClientBuilder.baseUrl("http://AccountService")
                    .build()
                    .get()
                    .uri("/account/{customerId}", customer.getCustomerId())
                    .retrieve()
                    .bodyToMono(BankAccount.class)
                    .defaultIfEmpty(new BankAccount()) // Return a default BankAccount if response is empty
                    .map(account -> {
                        customer.setAccount(account);
                        return customer;
                    })
                    .onErrorResume(e -> accountServiceFallbackForCustomer(customer.getCustomerId(), e)); // Handle individual errors
            customerMonos.add(customerMono);
        }

        // Combine all customer Monos into a single Mono<List<Customer>>
        return Mono.zip(customerMonos, results -> {
            List<Customer> allCustomers = new ArrayList<>();
            for (Object result : results) {
                allCustomers.add((Customer) result);
            }
            return allCustomers;
        });
    }

    // Fallback method for a single customer (Mono<Customer>)
    public Mono<Customer> accountServiceFallbackForCustomer(String customerId, Throwable t) {
        // Handle the fallback logic for a single customer, e.g., returning a customer with no account details or a default account
        Customer fallbackCustomer = customers.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        fallbackCustomer.setAccount(new BankAccount()); // or set a default value

        return Mono.just(fallbackCustomer);  // Return as Mono
    }

    // Fallback method for multiple customers (Mono<List<Customer>>)
    public Mono<List<Customer>> accountServiceFallbackForCustomers(Throwable t) {
        // Handle the fallback logic for multiple customers (e.g., return empty or default accounts)
        List<Customer> fallbackCustomers = new ArrayList<>();
        for (Customer customer : customers) {
            customer.setAccount(new BankAccount()); // or set a default value
            fallbackCustomers.add(customer);
        }

        return Mono.just(fallbackCustomers);  // Return as Mono
    }
}