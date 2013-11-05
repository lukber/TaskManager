package cz.czechGeeks.taskManager.server.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class CantFindEjbException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CantFindEjbException(String message, Throwable cause) {
		super(message, cause);
	}

	public CantFindEjbException(Throwable cause) {
		super(cause);
	}
}
