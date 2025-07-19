package demo.boot;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CustomerServiceMain {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceMain.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(RestTemplateBuilder builder)
	{
		return builder.setConnectTimeout(Duration.ofMillis(5000)).build();
	}
}
