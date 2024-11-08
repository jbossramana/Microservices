package demo.boot.client;

import demo.boot.model.BankAccount;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AccountService") // Ensure this points to the correct service
public interface AccountClient {

    @GetMapping("/account/{customerId}")
    BankAccount getAccountByCustomerId(@PathVariable("customerId") String customerId);
}