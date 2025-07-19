package demo.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;




@SpringBootApplication	// This annotation boostraps and auto-configure the application.
//@EnableHypermediaSupport(type= {HypermediaType.HAL})  // Hypermedia Appliation Language(HAL)
//@EnableHypermediaSupport(type = HypermediaType.HAL_FORMS)
public class Hateoasapplication {

	public static void main(String[] args) {
		SpringApplication.run(Hateoasapplication.class, args);
	}
	

	
}