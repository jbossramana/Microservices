package demo.boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
public class OtherService {



	@Autowired
	 private ReactorLoadBalancerExchangeFilterFunction lbFunction;
	 
    

	@GetMapping("/")
	public Mono<String> doOtherStuff() {
        Mono<String> msg=  WebClient.builder().baseUrl("http://SimpleService")
                .filter(lbFunction)
                .build()
                .get()
                .uri("/")
                .retrieve()
                .bodyToMono(String.class);
        
          return msg.map(s -> "Unshakable".concat(s));
          
    }
	
	
	/*
	 * @GetMapping 
	 * public Flux<User> listUsers()
	 * { return userRepository.findAll(); }
	 */
	
	
    
	
}
