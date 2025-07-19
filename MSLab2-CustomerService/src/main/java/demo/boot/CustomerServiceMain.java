package demo.boot;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
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

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);

        return builder
                .requestFactory(() -> factory)
                .build();
	}

	 @Bean
	    @LoadBalanced // enables service name resolution through Eureka
	    public RestClient.Builder loadBalancedRestClientBuilder() {
	        return RestClient.builder();
	    }
	 
	 @Bean
	    public RestClient restClient(RestClient.Builder builder) {
	        return builder.build();
	    }
	 
}
