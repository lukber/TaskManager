package cz.czechGeeks.taskManager.client.android.model;

/**
 * Reprezentace chybovych zprav
 * 
 * @author lukasb
 * 
 */
public class ErrorMessage {

	// text zpravy
	private String message;

	public ErrorMessage() {
	}

	public ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
