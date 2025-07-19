package demo.boot.model;

import org.springframework.hateoas.RepresentationModel;


public class Greeting extends RepresentationModel<Greeting> {

	private final String content;

    
	public Greeting( String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}