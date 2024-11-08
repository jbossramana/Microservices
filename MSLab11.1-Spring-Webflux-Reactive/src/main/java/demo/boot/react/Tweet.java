package demo.boot.react;

import java.io.Serializable;




public class Tweet implements Serializable {
    private String text;
    private String username;
	public String getText() {
		return text;
	}
	
	public Tweet()
	{
		
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Tweet(String text, String username) {
		super();
		this.text = text;
		this.username = username;
	}
    
    
}
