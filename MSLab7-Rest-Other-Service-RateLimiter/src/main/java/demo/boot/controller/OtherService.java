package demo.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class OtherService {

	@Autowired
	RestTemplate restTemplate;
	
	@GetMapping("/")
	 @RateLimiter(name="SimpleService", fallbackMethod = "getDefaultInfo")
	public String getInfo()
	{
		String msg = restTemplate.getForObject("http://SimpleService",String.class);
		return "Unshakable"+msg;
	}
	
	public String getDefaultInfo(Throwable  t)
	{
		return "Prayer has comeback power...Fallback message";
	}
}
