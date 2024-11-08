package demo.boot.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import demo.boot.model.BankAccount;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountController {

    // Simulating a database of BankAccount objects
    private static final List<BankAccount> accounts = new ArrayList<>();

    static {
        // Adding 3 sample BankAccount objects to the list
        accounts.add(new BankAccount("1", "Savings", 1000.50));
        accounts.add(new BankAccount("2", "Checking", 500.75));
        accounts.add(new BankAccount("3", "Savings", 2500.00));
    }

    // Endpoint to retrieve a BankAccount by accountId
    @GetMapping("/account/{accountId}")
    public BankAccount getAccount(@PathVariable("accountId") String accountId) {
        // Find the account by accountId
    	System.out.println("Instance2...");
        return accounts.stream()
                .filter(account -> account.getAccountId().equals(accountId))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}