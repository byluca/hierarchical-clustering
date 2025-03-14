package Server.src.database;

public class DatabaseConnectionException extends Exception {
	private static final long serialVersionUID = 1L;

	DatabaseConnectionException(String msg) {
        super(msg);
    }
}
