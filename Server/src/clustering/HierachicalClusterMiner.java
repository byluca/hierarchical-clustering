/**
 * Package che contiene le classi per l'implementazione di algoritmi di clustering.
 */
package Server.src.clustering;

import java.io.*;
import Server.src.data.Data;
import Server.src.data.InvalidSizeException;
import Server.src.distance.ClusterDistance;

/**
 * Implementa un algoritmo di clustering gerarchico agglomerativo.
 * Questa classe crea un dendrogramma che rappresenta il processo di clustering
 * e fornisce metodi per salvare e caricare istanze da file.
 *
 * @serial include
 */
public class HierachicalClusterMiner implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Il dendrogramma che memorizza la gerarchia dei cluster. */
	private Dendrogram dendrogram;

	/**
	 * Crea un'istanza di classe HierachicalClusterMiner con la profondità specificata.
	 *
	 * @param depth la profondità massima del dendrogramma
	 * @throws InvalidDepthException se la profondità specificata non è valida
	 */
	public HierachicalClusterMiner(int depth) throws InvalidDepthException {
		dendrogram = new Dendrogram(depth);
	}

	/**
	 * Restituisce la profondità del dendrogramma.
	 *
	 * @return la profondità del dendrogramma
	 */
	public int getDepth() {
		return dendrogram.getDepth();
	}

	/**
	 * Calcola il clustering del dataset utilizzando la misura di distanza specificata.
	 *
	 * @param data il dataset su cui eseguire il clustering
	 * @param distance l'interfaccia per il calcolo della distanza tra cluster
	 * @throws InvalidDepthException se la profondità del dendrogramma non è compatibile con i dati
	 * @throws InvalidSizeException se si verificano problemi con le dimensioni dei cluster
	 * @throws InvalidClustersNumberException se il numero di cluster non è valido
	 */
	public void mine(Data data, ClusterDistance distance) throws InvalidDepthException, InvalidSizeException, InvalidClustersNumberException {
		if (getDepth() > data.getNumberOfExample()) {
			throw new InvalidDepthException("Numero di Esempi maggiore della profondità del dendrogramma!\n");
		}
		ClusterSet level0 = new ClusterSet(data.getNumberOfExample());
		for (int i = 0; i < data.getNumberOfExample(); i++) {
			Cluster c = new Cluster();
			c.addData(i);
			level0.add(c);
		}
		dendrogram.setClusterSet(level0, 0);
		for (int i = 1; i < getDepth(); i++) {
			ClusterSet nextlevel = null;
			try {
				nextlevel = dendrogram.getClusterSet(i - 1).mergeClosestClusters(distance, data);
				dendrogram.setClusterSet(nextlevel, i);
			} catch (InvalidSizeException | InvalidClustersNumberException e) {
				i = getDepth();
				throw e;
			}
		}
	}

	/**
	 * Restituisce una rappresentazione testuale del dendrogramma.
	 *
	 * @return una stringa che rappresenta il dendrogramma
	 */
	public String toString() {
		return dendrogram.toString();
	}

	/**
	 * Restituisce una rappresentazione testuale del dendrogramma con i dati.
	 *
	 * @param data i dati da utilizzare per la rappresentazione
	 * @return una stringa che rappresenta il dendrogramma con riferimento ai dati
	 * @throws InvalidDepthException se si verificano problemi con la profondità del dendrogramma
	 */
	public String toString(Data data) throws InvalidDepthException {
		return dendrogram.toString(data);
	}

	/**
	 * Metodo statico per caricare un'istanza di HierachicalClusterMiner da un file.
	 *
	 * @param fileName nome del file da cui caricare l'istanza
	 * @return l'istanza caricata di HierachicalClusterMiner
	 * @throws FileNotFoundException se il file non viene trovato
	 * @throws IOException se si verifica un errore di input/output
	 * @throws ClassNotFoundException se la classe dell'oggetto serializzato non viene trovata
	 * @throws IllegalArgumentException se gli argomenti non sono validi
	 */
	public static HierachicalClusterMiner loadHierachicalClusterMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException, IllegalArgumentException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			return (HierachicalClusterMiner) ois.readObject();
		}
	}

	/**
	 * Metodo per salvare l'istanza corrente di HierachicalClusterMiner su un file.
	 *
	 * @param fileName nome del file su cui salvare l'istanza
	 * @throws FileNotFoundException se il file non viene trovato
	 * @throws IOException se si verifica un errore di input/output o con il percorso del file
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException {
		if (fileName.matches(".*[<|?*].*")) {
			throw new IOException("Errore: Il percorso contiene caratteri non validi. Riprova.\n");
		}
		File file = new File(fileName);
		if (file.exists()) {
			throw new IOException("Esiste già un file con questo nome.\n");
		}
		File parentDir = file.getParentFile();
		if (parentDir != null && !parentDir.exists()) {
			if (parentDir.mkdirs()) {
				System.out.println("Directory creata: " + parentDir.getAbsolutePath());
			} else {
				throw new IOException("Impossibile creare la directory: " + parentDir.getAbsolutePath() + "\n");
			}
		}
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
			oos.writeObject(this);
		}
	}
}