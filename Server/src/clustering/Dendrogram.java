package Server.src.clustering;

import java.io.*;
import Server.src.data.Data;

/**
 * Rappresenta una struttura gerarchica di cluster (dendrogramma).
 * <p>
 * Il dendrogramma organizza i cluster in una sequenza di livelli, dove ogni livello
 * rappresenta una fase del processo di clustering gerarchico. Il livello 0 corrisponde
 * alla situazione iniziale con tutti gli elementi separati, mentre l'ultimo livello
 * rappresenta un singolo cluster contenente tutti gli elementi.
 * </p>
 */
class Dendrogram implements Serializable {
    private static final long serialVersionUID = 1L;

    private ClusterSet[] tree;

    /**
     * Costruisce un dendrogramma con la profondità specificata.
     * @param depth Numero di livelli del dendrogramma
     * @throws InvalidDepthException Se la profondità è minore o uguale a zero
     */
    Dendrogram(int depth) throws InvalidDepthException {
        if (depth <= 0) {
            throw new InvalidDepthException("Profondità non valida!\n");
        }
        tree = new ClusterSet[depth];
    }

    /**
     * Imposta l'insieme di cluster a un determinato livello del dendrogramma.
     * @param c Insieme di cluster da inserire
     * @param level Livello del dendrogramma in cui inserire (0-based)
     */
    void setClusterSet(ClusterSet c, int level) {
        tree[level] = c;
    }

    /**
     * Recupera l'insieme di cluster a un determinato livello.
     * @param level Livello del dendrogramma da cui recuperare (0-based)
     * @return Insieme di cluster al livello specificato
     */
    ClusterSet getClusterSet(int level) {
        return tree[level];
    }

    /**
     * Restituisce la profondità massima del dendrogramma.
     * @return Numero totale di livelli del dendrogramma
     */
    int getDepth() {
        return tree.length;
    }

    /**
     * Restituisce una rappresentazione testuale degli indici degli elementi nei cluster.
     * @return Stringa con la struttura completa del dendrogramma
     */
    public String toString() {
        String v = "";
        for (int i = 0; i < tree.length; i++) {
            v += ("level" + i + ":\n" + tree[i] + "\n");
        }
        return v;
    }

    /**
     * Restituisce una rappresentazione dettagliata degli esempi nei cluster.
     * @param data Dataset di riferimento per recuperare i valori degli esempi
     * @return Stringa con i dati effettivi dei cluster
     * @throws InvalidDepthException Se si verifica un'incongruenza nella struttura del dendrogramma
     */
    public String toString(Data data) throws InvalidDepthException {
        String v = "";
        for (int i = 0; i < tree.length; i++) {
            v += ("level" + i + ":\n" + tree[i].toString(data) + "\n");
        }
        return v;
    }
}