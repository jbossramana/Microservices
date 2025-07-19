package demo.boot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class PaymentController {

	
	@Value("${msg: Config Server not working... }")
	String msg;
	

	
	
	@GetMapping("/")
	public String getMsg()
	{
		System.out.println("Payment Server1...");
		return msg;
	}
}
