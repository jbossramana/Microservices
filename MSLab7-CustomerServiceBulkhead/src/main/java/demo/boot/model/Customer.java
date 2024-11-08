package demo.boot.model;

public class Customer {
    private String customerId;
    private String name;
    private BankAccount account;

    // Constructor, Getters, Setters
    public Customer(String customerId, String name, BankAccount account) {
        this.customerId = customerId;
        this.name = name;
        this.account = account;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BankAccount getAccount() {
        return account;
    }

    public void setAccount(BankAccount account) {
        this.account = account;
    }
}