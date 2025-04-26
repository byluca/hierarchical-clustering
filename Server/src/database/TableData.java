/**
 * Package contenente le classi per la gestione dell'accesso al database.
 */
package Server.src.database;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Server.src.data.*;

/**
 * Classe che gestisce l'accesso ai dati di una tabella del database.
 * Fornisce metodi per recuperare i dati dalle tabelle e convertirli in oggetti Example.
 */
public class TableData {
    /** Riferimento all'oggetto di accesso al database */
    private DbAccess db;

    /**
     * Inizializza un nuovo oggetto TableData con il riferimento all'accesso al database.
     *
     * @param db oggetto che gestisce la connessione al database
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * Recupera le transazioni distinte dalla tabella specificata.
     * Ogni riga della tabella viene convertita in un oggetto Example,
     * verificando che tutti gli attributi siano numerici.
     *
     * @param table nome della tabella da cui recuperare i dati
     * @return lista di Example memorizzata nella tabella
     * @throws SQLException in caso di errore nell'interrogazione del database
     * @throws EmptySetException in caso di tabella vuota
     * @throws MissingNumberException in presenza di attributi non numerici
     * @throws DatabaseConnectionException in caso di errore nella connessione al database
     */
    public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException, MissingNumberException, DatabaseConnectionException {
        List<Example> transazioni = new ArrayList<>();
        Connection con = db.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        TableSchema schema = new TableSchema(db, table);

        stmt = con.createStatement();
        String query = "select distinct * from " + table;
        rs = stmt.executeQuery(query);

        if (!rs.isBeforeFirst()) { // verifica se il ResultSet è vuoto
            throw new EmptySetException("La tabella " + table + " è vuota.\n");
        }

        while (rs.next()) {
            Example example = new Example();

            for (int i = 0; i < schema.getNumberOfAttributes(); i++) {
                TableSchema.Column column = schema.getColumn(i);
                if (!column.isNumber()) {
                    throw new MissingNumberException("Attributo non numerico trovato: " + column.getColumnName() + "\n");
                }
                example.add(rs.getDouble(column.getColumnName()));
            }
            transazioni.add(example);
        }
        rs.close();
        stmt.close();
        db.closeConnection();
        return transazioni;
    }
}