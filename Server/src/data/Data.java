/**
 * Package contenente le classi per la gestione e manipolazione dei dati nell'applicazione.
 */
package Server.src.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.sql.SQLException;

import Server.src.database.*;

/**
 * Classe che rappresenta un dataset composto da esempi.
 * Fornisce funzionalità per caricare dati da database, accedere agli esempi
 * e calcolare distanze tra di essi.
 */
public class Data {
    /** Lista che memorizza gli esempi che compongono il dataset */
    private List<Example> data = new ArrayList<>();

    /**
     * Crea un'istanza di classe Data leggendo i suoi esempi dalla tabella specificata nel database.
     *
     * @param tableName nome della tabella da cui leggere i dati
     * @throws NoDataException se la tabella è vuota o se si verificano errori durante l'accesso ai dati
     */
    public Data(String tableName) throws NoDataException {
        DbAccess dbAccess = new DbAccess();

        try {
            TableData tableData = new TableData(dbAccess);
            List<Example> examples = tableData.getDistinctTransazioni(tableName);
            this.data.addAll(examples);
        } catch (DatabaseConnectionException e) {
            throw new NoDataException("Errore di connessione al database: " + e.getMessage() + "\n");
        } catch (EmptySetException e) {
            throw new NoDataException("La tabella " + tableName + " è vuota: " + e.getMessage() + "\n");
        } catch (MissingNumberException e) {
            throw new NoDataException("Eccezione durante l'elaborazione dei dati: " + e.getMessage() + "\n");
        } catch (SQLException e) {
            throw new NoDataException("Errore SQL durante il recupero dei dati dalla tabella: " + e.getMessage() + "\n");
        }
    }

    /**
     * Restituisce il numero degli esempi memorizzati nel dataset.
     *
     * @return numero di esempi nel dataset
     */
    public int getNumberOfExample() {
        return data.size();
    }

    /**
     * Restituisce l'esempio in posizione specificata.
     *
     * @param exampleIndex indice dell'esempio da recuperare
     * @return l'esempio alla posizione specificata
     */
    public Example getExample(int exampleIndex) {
        return data.get(exampleIndex);
    }

    /**
     * Restituisce un iteratore per scorrere gli esempi del dataset.
     *
     * @return iteratore sugli esempi
     */
    public Iterator<Example> iterator() {
        return data.iterator();
    }

    /**
     * Calcola e restituisce la matrice triangolare superiore delle distanze Euclidee
     * tra gli esempi memorizzati nel dataset.
     *
     * @return matrice delle distanze tra esempi
     * @throws InvalidSizeException se si verificano problemi con le dimensioni durante il calcolo
     */
    public double[][] distance() throws InvalidSizeException {
        double[][] dist = new double[data.size()][data.size()];

        for (int i = 0; i < data.size(); i++) {
            dist[i][i] = 0;
            for (int j = i + 1; j < data.size(); j++) {
                double d = data.get(i).distance(data.get(j));
                dist[i][j] = d;
                dist[j][i] = 0; // riflessione nella metà inferiore
            }
        }
        return dist;
    }

    /**
     * Crea una rappresentazione testuale del dataset con tutti gli esempi enumerati.
     *
     * @return stringa che rappresenta il dataset
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        Iterator<Example> iterator = iterator();
        int count = 0;

        while (iterator.hasNext()) {
            s.append(count++).append(":[").append(iterator.next().toString()).append("]\n");
        }
        return s.toString();
    }
}