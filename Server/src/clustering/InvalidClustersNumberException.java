package Server.src.clustering;

public class InvalidClustersNumberException extends Exception{
	private static final long serialVersionUID = 1L;

	InvalidClustersNumberException(String msg) {
        super(msg);
    }
}
