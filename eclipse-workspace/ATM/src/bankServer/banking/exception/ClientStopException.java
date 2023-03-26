package bankServer.banking.exception;

public class ClientStopException extends Exception {
	private static final long serialVersionUID = 1L;
	private static String message = " stop service exceptionally.";
		
	public ClientStopException() {
		super(message);
	}
}
