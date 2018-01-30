package Test2;

import java.io.Serializable;

public class Chat implements Serializable{
	protected static final long serialVersionUID = 1L;
	private String message;
	public Chat(String message) {
		super();
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
