package Server.src.distance;

import java.util.Iterator;

import Server.src.data.*;

import Server.src.clustering.Cluster;

// classe SingleLinkDistance implementa il metodo distance dell'interfaccia ClusterDistance per calcolare la distanza tra due cluster
public class SingleLinkDistance implements ClusterDistance {
	/** 
	 * restituisce la minima distanza tra due cluste con la distanza singlelink
	 * @param c1 primo cluster
	 * @param c2 secondo cluster
	 * @param d dataset
	 * @return min (un double)
	 */
	public double distance(Cluster c1, Cluster c2, Data d) throws InvalidSizeException {
		double min = Double.MAX_VALUE;

		Iterator<Integer> it1 = c1.iterator();
		while (it1.hasNext()) {
			Example e1 = d.getExample(it1.next());
			Iterator<Integer> it2 = c2.iterator();
			while (it2.hasNext()) {
                double distance = e1.distance(d.getExample(it2.next()));
                if (distance < min) {
					min=distance;
				}
			}
		}
		return min;
	}
}