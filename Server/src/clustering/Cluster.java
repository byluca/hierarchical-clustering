package Server.src.clustering;

import java.io.*;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import Server.src.data.Data;

/**
 * classe Cluster
 * modella un cluster come la collezione delle posizioni occupate
 * dagli esempi raggruppati nel Cluster nel vettore data dell’oggetto
 * che modella il dataset su cui il clustering è calcolato(istanza di Data)
 */

public class Cluster implements Iterable<Integer>, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	
	private Set<Integer> clusteredData = new TreeSet<>();

	/**
	 * aggiunge l'indice di posizione id al cluster
	 * @param id indice da aggiungere al cluster
	 */
	void addData(int id){
        clusteredData.add(id);
	}

	/* restituisce la dimensione del cluster
	 * @return dimensione del cluster
	 */
	public int getSize() {
		return clusteredData.size();
	}

	// restituisce un iteratore per scorrere gli elementi del cluster
	public Iterator<Integer> iterator() {
		return clusteredData.iterator();
	}
	
	// crea una copia del cluster
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

	/* crea un nuovo cluster che è la fusione del cluster corrente e del cluster c
	 * @param c cluster da unire al cluster corrente
	 * @return newC cluster che è la fusione del cluster corrente e del cluster c
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

	// restituisce una stringa contenente gli indici degli esempi raggruppati nel cluster
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

	// restituisce una stringa contenente gli esempi raggruppati nel cluster
	public String toString(Data data) {
		StringBuilder str = new StringBuilder();
		Iterator<Integer> it = clusteredData.iterator();

		while (it.hasNext()) {
			str.append(" <[").append(data.getExample(it.next())).append("]>");
		}
		return str.toString();
	}
}