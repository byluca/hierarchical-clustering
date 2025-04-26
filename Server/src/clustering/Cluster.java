package Server.src.clustering;

import java.io.*;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import Server.src.data.Data;

/**
 * Modella un cluster come una collezione di posizioni degli esempi nel dataset.
 * <p>
 * Un cluster è rappresentato dagli indici delle righe del dataset ({@link Data})
 * che appartengono al cluster. Supporta operazioni di clonazione, fusione e
 * iterazione sugli elementi contenuti.
 * </p>
 */
public class Cluster implements Iterable<Integer>, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	private Set<Integer> clusteredData = new TreeSet<>();

	/**
	 * Aggiunge un elemento al cluster.
	 * @param id Indice della posizione nel dataset da aggiungere al cluster
	 */
	void addData(int id) {
		clusteredData.add(id);
	}

	/**
	 * Restituisce il numero di elementi nel cluster.
	 * @return Dimensione corrente del cluster
	 */
	public int getSize() {
		return clusteredData.size();
	}

	/**
	 * Fornisce un iteratore per scorrere gli elementi del cluster.
	 * @return Iteratore sugli indici degli elementi del cluster
	 */
	public Iterator<Integer> iterator() {
		return clusteredData.iterator();
	}

	/**
	 * Crea una copia profonda del cluster.
	 * @return Un nuovo cluster identico a quello corrente
	 * @throws CloneNotSupportedException Se la clonazione non è supportata
	 */
	@Override
	public Cluster clone() throws CloneNotSupportedException {
		Cluster clone = null;
		try {
			clone = (Cluster) super.clone();
			clone.clusteredData = (Set<Integer>) ((TreeSet<Integer>) this.clusteredData).clone();
		} catch (CloneNotSupportedException e) {
			throw new CloneNotSupportedException("Errore nella clonazione!");
		}
		return clone;
	}

	/**
	 * Fonde il cluster corrente con un altro cluster.
	 * @param c Cluster da unire a quello corrente
	 * @return Nuovo cluster contenente tutti gli elementi di entrambi i cluster
	 */
	Cluster mergeCluster(Cluster c) {
		Cluster newC = new Cluster();
		Iterator<Integer> it1 = this.iterator();
		Iterator<Integer> it2 = c.iterator();

		while (it1.hasNext()) {
			newC.addData(it1.next());
		}
		while (it2.hasNext()) {
			newC.addData(it2.next());
		}
		return newC;
	}

	/**
	 * Rappresentazione testuale degli indici degli elementi nel cluster.
	 * @return Stringa formattata con gli indici separati da virgole
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		Iterator<Integer> it = this.iterator();

		if (it.hasNext()) {
			str.append(" ").append(it.next());
		}

		while (it.hasNext()) {
			str.append(", ").append(it.next());
		}
		return str.toString();
	}

	/**
	 * Rappresentazione dettagliata degli esempi nel cluster.
	 * @param data Dataset di riferimento per recuperare gli esempi
	 * @return Stringa formattata con la rappresentazione degli esempi
	 */
	public String toString(Data data) {
		StringBuilder str = new StringBuilder();
		Iterator<Integer> it = clusteredData.iterator();

		while (it.hasNext()) {
			str.append(" <[").append(data.getExample(it.next())).append("]>");
		}
		return str.toString();
	}
}