package demo.boot;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class Lab1EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Lab1EurekaServerApplication.class, args);
	}



}
