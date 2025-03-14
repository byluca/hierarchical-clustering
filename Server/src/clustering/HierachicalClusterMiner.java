package Server.src.clustering;

import java.io.*;

import Server.src.data.Data;
import Server.src.data.InvalidSizeException;

import Server.src.distance.ClusterDistance;

public class HierachicalClusterMiner implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Dendrogram dendrogram;

	// crea un'istanza di classe HierachicalClusterMiner con profondità depth
	public HierachicalClusterMiner(int depth) throws InvalidDepthException {
		dendrogram = new Dendrogram(depth);
	}

	// restituisce la profondità del dendrogramma
	public int getDepth() {
		return dendrogram.getDepth();
	}

	// calcola il clustering del dataset data con interfaccia di calcolo distanza
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

	// restituisce una rappresentazione testuale del dendrogramma
	public String toString() {
		return dendrogram.toString();
	}

	// restituisce una rappresentazione testuale del dendrogramma
	public String toString(Data data) throws InvalidDepthException {
		return dendrogram.toString(data);
	}

	/**
	 * metodo statico per caricare un'istanza di HierachicalClusterMiner da un file
	 * @param fileName nome del file da cui caricare l'istanza
	 * @return l'istanza caricata di HierachicalClusterMiner
	 * @throws FileNotFoundException se il file non viene trovato
	 * @throws IOException se si verifica un errore di input/output
	 * @throws ClassNotFoundException se la classe dell'oggetto serializzato non viene trovata
	 */
	public static HierachicalClusterMiner loadHierachicalClusterMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException, IllegalArgumentException {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
			return (HierachicalClusterMiner) ois.readObject();
		}
	}

	/**
	 * metodo per salvare l'istanza corrente di HierachicalClusterMiner su un file
	 * @param fileName nome del file su cui salvare l'istanza
	 * @throws FileNotFoundException se il file non viene trovato
	 * @throws IOException se si verifica un errore di input/output
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