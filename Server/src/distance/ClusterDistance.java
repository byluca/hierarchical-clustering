package Server.src.distance;

import Server.src.clustering.Cluster;

import Server.src.data.Data;
import Server.src.data.InvalidSizeException;

// interfaccia ClusterDistance contiene metodo per calcolare la distanza tra due cluster
public interface ClusterDistance {
	/**
	 * @param c1 primo cluster
	 * @param c2 secondo cluster
	 * @param d dataset
	 * @return double
	 */
	double distance(Cluster c1, Cluster c2, Data d) throws InvalidSizeException;
}