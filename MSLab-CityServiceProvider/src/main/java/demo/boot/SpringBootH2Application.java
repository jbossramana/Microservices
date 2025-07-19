package demo.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // (scanBasePackages = "demo.boot.*")  @ComponentScan, @Configuration , @EnableAutoConfiguration
public class SpringBootH2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootH2Application.class, args);
	}

}
