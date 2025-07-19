package demo.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RefreshScope
@RestController
public class OrderController {

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${msg: Config Server not working... }")
	String orderMsg;
	
	@GetMapping("/")
	@CircuitBreaker(name ="PaymentService", fallbackMethod = "getDefaultInfo" )
	public String getInfo()
	{
		String paymentMsg = restTemplate.getForObject("http://PaymentService/",String.class);
		return orderMsg + paymentMsg;
	}
	
	public String getDefaultInfo(Exception e)
	{
		return "Please do cash payment";
	}
}
