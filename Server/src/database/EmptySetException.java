package Server.src.database;

public class EmptySetException extends Exception {
	private static final long serialVersionUID = 1L;

	public EmptySetException(String message) {
        super(message);
    }
}