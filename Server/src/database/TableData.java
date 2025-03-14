package Server.src.database;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Server.src.data.*;

// gestisce l'accesso ai dati di una tabella
public class TableData {
    private DbAccess db;

    // inizializza l’attributo db
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * recupera le transazioni distinte dalla tabella specificata
     *
     * @param table nome della tabella
     * @return lista di Example memorizzata nella tabella
     * @throws SQLException in caso di errore nella interrogazione
     * @throws EmptySetException in caso di tabella vuota
     * @throws MissingNumberException in presenza di attributi non numerici
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