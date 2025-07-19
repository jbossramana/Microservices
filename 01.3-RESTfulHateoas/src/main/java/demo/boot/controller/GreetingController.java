package demo.boot.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import demo.boot.model.Greeting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class GreetingController {

	private static final String TEMPLATE = "Hello, %s!";

	@GetMapping("/greeting")
	public HttpEntity<Greeting> greeting(
		@RequestParam(value = "name", defaultValue = "World") String name) {

		Greeting greeting = new Greeting(String.format(TEMPLATE, name));
		greeting.add(linkTo(methodOn(GreetingController.class).greeting(name)).withSelfRel());
		greeting.add(linkTo(methodOn(GreetingController.class).greatMsg()).withRel("Today Message"));

		
		return new ResponseEntity<>(greeting, HttpStatus.OK);
	}
	
	@GetMapping("/today")
	public HttpEntity<String> greatMsg()
	{
		return new ResponseEntity<String>("UnshakableFaith",HttpStatus.OK);
	}
}
