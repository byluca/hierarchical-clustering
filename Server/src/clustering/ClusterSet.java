package Server.src.clustering;

import java.io.*;
import Server.src.data.Data;
import Server.src.data.InvalidSizeException;
import Server.src.distance.ClusterDistance;

/**
 * Rappresenta una collezione di cluster con operazioni di gestione e manipolazione.
 * <p>
 * La classe gestisce un insieme di cluster fornendo metodi per aggiungere, recuperare
 * e fondere cluster. Supporta la serializzazione degli oggetti.
 * </p>
 */
class ClusterSet implements Serializable {
	private static final long serialVersionUID = 1L;

	private Cluster[] C;
	private int lastClusterIndex = 0;

	/**
	 * Costruttore che inizializza un insieme di cluster con capacità specificata.
	 * @param k Dimensione iniziale dell'insieme di cluster
	 */
	ClusterSet(int k) {
		C = new Cluster[k];
	}

	/**
	 * Aggiunge un cluster all'insieme se non già presente.
	 * <p>
	 * Verifica la presenza tramite confronto di riferimento (identity check)
	 * </p>
	 * @param c Cluster da aggiungere all'insieme
	 */
	void add(Cluster c) {
		for (int j = 0; j < lastClusterIndex; j++) {
			if (c == C[j]) {
				return;
			}
		}
		C[lastClusterIndex] = c;
		lastClusterIndex++;
	}

	/**
	 * Restituisce il cluster nella posizione specificata.
	 * @param i Indice del cluster da recuperare
	 * @return Cluster nella posizione i-esima
	 */
	Cluster get(int i) {
		return C[i];
	}

	/**
	 * Fonde i due cluster più vicini in un nuovo insieme.
	 * <p>
	 * Calcola le distanze tra tutte le coppie di cluster utilizzando la metrica specificata.
	 * Crea un nuovo ClusterSet sostituendo i due cluster più vicini con la loro fusione.
	 * </p>
	 * @param distance Strategia per il calcolo della distanza tra cluster
	 * @param data Dataset di riferimento per il calcolo delle distanze
	 * @return Nuovo ClusterSet con i cluster fusi
	 * @throws InvalidSizeException Se si verifica un errore nelle dimensioni dei dati
	 * @throws InvalidClustersNumberException Se sono presenti meno di due cluster
	 */
	ClusterSet mergeClosestClusters(ClusterDistance distance, Data data)
			throws InvalidSizeException, InvalidClustersNumberException {

		if (lastClusterIndex <= 1) {
			throw new InvalidClustersNumberException("Non ci sono abbastanza cluster da fondere");
		}

		double minD = Double.MAX_VALUE;
		Cluster cluster1 = null;
		Cluster cluster2 = null;

		for (int i = 0; i < this.C.length; i++) {
			Cluster c1 = get(i);
			for (int j = i + 1; j < this.C.length; j++) {
				Cluster c2 = get(j);
				double d = 0;
				try {
					d = distance.distance(c1, c2, data);
					if (d < minD) {
						minD = d;
						cluster1 = c1;
						cluster2 = c2;
					}
				} catch (InvalidSizeException e) {
					j = this.C.length;
					i = this.C.length;
					throw e;
				}
			}
		}

		Cluster mergedCluster = cluster1.mergeCluster(cluster2);
		ClusterSet finalClusterSet = new ClusterSet(this.C.length-1);
		for (int i = 0; i < this.C.length; i++){
			Cluster c = get(i);
			if (c != cluster1) {
				if (c != cluster2) {
					finalClusterSet.add(c);
				}
			} else {
				finalClusterSet.add(mergedCluster);
			}
		}
		return finalClusterSet;
	}

	/**
	 * Restituisce una rappresentazione testuale degli indici degli esempi nei cluster.
	 * @return Stringa formattata con la struttura dei cluster
	 */
	public String toString() {
		String str = "";
		for (int i = 0; i < C.length; i++) {
			if (C[i] != null) {
				str += "cluster" + i + ":" + C[i] + "\n";
			}
		}
		return str;
	}

	/**
	 * Restituisce una rappresentazione dettagliata degli esempi nei cluster.
	 * @param data Dataset per recuperare i valori degli esempi
	 * @return Stringa formattata con i dati effettivi dei cluster
	 */
	public String toString(Data data) {
		String str = "";
		for (int i = 0; i < C.length; i++) {
			if (C[i] != null) {
				str += "cluster" + i + ":" + C[i].toString(data) + "\n";
			}
		}
		return str;
	}
}