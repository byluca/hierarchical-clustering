package Server.src.distance;

import java.util.Iterator;

import Server.src.data.*;
import Server.src.clustering.Cluster;

/**
 * La classe SingleLinkDistance implementa il metodo distance
 * dell'interfaccia ClusterDistance per calcolare la distanza tra due cluster
 * utilizzando il criterio del single-linkage.
 */
public class SingleLinkDistance implements ClusterDistance {

	/**
	 * Calcola la minima distanza tra due cluster utilizzando il criterio single-linkage.
	 * La distanza tra due cluster viene determinata come la minima distanza
	 * tra tutti i punti appartenenti ai due cluster.
	 *
	 * @param c1 primo cluster
	 * @param c2 secondo cluster
	 * @param d dataset contenente gli esempi
	 * @return la minima distanza tra due punti appartenenti ai cluster c1 e c2
	 * @throws InvalidSizeException se si verifica un errore relativo alle dimensioni dei dati
	 */
	@Override
	public double distance(Cluster c1, Cluster c2, Data d) throws InvalidSizeException {
		double min = Double.MAX_VALUE;

		// Itera sugli elementi del primo cluster
		Iterator<Integer> it1 = c1.iterator();
		while (it1.hasNext()) {
			Example e1 = d.getExample(it1.next());

			// Itera sugli elementi del secondo cluster
			Iterator<Integer> it2 = c2.iterator();
			while (it2.hasNext()) {
				double distance = e1.distance(d.getExample(it2.next()));

				// Aggiorna il valore minimo se la distanza calcolata è inferiore
				if (distance < min) {
					min = distance;
				}
			}
		}
		return min;
	}
}
