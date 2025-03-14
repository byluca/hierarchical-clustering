package Server.src.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.sql.SQLException;

import Server.src.database.*;

// avvalora un oggetto data predefinito (fornito dal docente)
// oppure leggendo i suoi esempi dalla tabella con nome tableName nel database
// @throws NoDataException se la tabella è vuota
public class Data {
    private List<Example> data = new ArrayList<>(); // rappresenta il dataset

    // crea un'istanza di classe Data leggendo i suoi esempi dalla tabella con nome tableName nel database
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

    // restituisce il numero degli esempi memorizzati in data
    public int getNumberOfExample() {
        return data.size();
    }

    // restituisce l'elemento dell'istanza data in posizione exampleIndex
    public Example getExample(int exampleIndex) {
        return data.get(exampleIndex);
    }

    // restituisce un iteratore per scorrere gli elementi di data
    public Iterator<Example> iterator() {
        return data.iterator();
    }

    // restituisce la matrice triangolare superiore delle distanze Euclidee calcolate tra gli esempi memorizzati in data
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

    // crea una stringa in cui memorizza gli esempi memorizzati nell’attributo data, opportunamente enumerati
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