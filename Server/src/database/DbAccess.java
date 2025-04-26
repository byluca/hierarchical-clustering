/**
 * Package contenente le classi per la gestione dell'accesso al database.
 */
package Server.src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che gestisce la connessione al database MySQL.
 * Fornisce metodi per inizializzare, ottenere e chiudere la connessione al database.
 */
public class DbAccess {
    /** Nome della classe driver JDBC */
    private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    /** Tipo di DBMS utilizzato */
    private final String DBMS = "jdbc:mysql";

    /** Indirizzo del server database */
    private final String SERVER = "localhost";

    /** Nome del database */
    private final String DATABASE = "MapDB";

    /** Porta per la connessione al server database */
    private final int PORT = 3306;

    /** ID utente per l'accesso al database */
    private final String USER_ID = "root"; // MODIFICARE QUI

    /** Password per l'accesso al database */
    private final String PASSWORD = ""; // MODIFICARE QUI

    /** Oggetto Connection per la connessione al database */
    private Connection conn;

    /**
     * Inizializza la connessione al database utilizzando i parametri definiti.
     * Carica il driver JDBC e stabilisce la connessione.
     *
     * @throws DatabaseConnectionException se si verificano errori durante il caricamento del driver
     *                                    o durante la connessione al database
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch(ClassNotFoundException e) {
            throw new DatabaseConnectionException("[ERRORE] Driver non trovato: " + e.getMessage());
        }

        String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
                + "?user=" + USER_ID
                + "&password=" + PASSWORD
                + "&useSSL=false"
                + "&serverTimezone=UTC"
                + "&allowPublicKeyRetrieval=true";

        try {
            conn = DriverManager.getConnection(connectionString);
        } catch(SQLException e) {
            throw new DatabaseConnectionException("[ERRORE] Connessione fallita: " + e.getMessage());
        }
    }

    /**
     * Restituisce l'oggetto Connection per interagire con il database.
     * Se la connessione non è ancora stata inizializzata, la inizializza.
     *
     * @return oggetto Connection per l'interazione con il database
     * @throws DatabaseConnectionException se si verificano errori durante l'inizializzazione della connessione
     */
    public Connection getConnection() throws DatabaseConnectionException {
        if(conn == null) {
            initConnection();
        }
        return conn;
    }

    /**
     * Chiude la connessione al database se è aperta.
     *
     * @throws SQLException se si verificano errori durante la chiusura della connessione
     */
    public void closeConnection() throws SQLException {
        if(conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}