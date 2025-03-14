package Server.src.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
    private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private final String DBMS = "jdbc:mysql";
    private final String SERVER = "localhost";
    private final String DATABASE = "MapDB";
    private final int PORT = 3306;
    private final String USER_ID = "utente_map"; // MODIFICARE QUI
    private final String PASSWORD = "PasswordSicura123!"; // MODIFICARE QUI

    private Connection conn;

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
            conn = DriverManager.getConnection(connectionString); // LINEA MANCANTE NEL TUO CODICE
        } catch(SQLException e) {
            throw new DatabaseConnectionException("[ERRORE] Connessione fallita: " + e.getMessage());
        }
    }

    public Connection getConnection() throws DatabaseConnectionException {
        if(conn == null) {
            initConnection();
        }
        return conn;
    }

    public void closeConnection() throws SQLException {
        if(conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}