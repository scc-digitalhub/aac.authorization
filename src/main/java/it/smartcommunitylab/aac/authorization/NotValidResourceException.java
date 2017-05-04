package it.smartcommunitylab.aac.authorization;

public class NotValidResourceException extends Exception {

	private static final long serialVersionUID = -7343191432587106922L;

	public NotValidResourceException() {
		super();
	}

	public NotValidResourceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public NotValidResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotValidResourceException(String message) {
		super(message);
	}

	public NotValidResourceException(Throwable cause) {
		super(cause);
	}

}
