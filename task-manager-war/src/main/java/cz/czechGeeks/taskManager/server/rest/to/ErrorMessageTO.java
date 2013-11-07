package cz.czechGeeks.taskManager.server.rest.to;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ErrorMessage")
public class ErrorMessageTO {

	private String message;

	public ErrorMessageTO() {
	}

	public ErrorMessageTO(String message) {
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
