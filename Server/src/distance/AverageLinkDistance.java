/**
 * Package contenente le classi per il calcolo delle distanze tra cluster.
 */
package Server.src.distance;

import java.util.Iterator;

import Server.src.clustering.Cluster;
import Server.src.data.*;

/**
 * Classe che implementa l'interfaccia ClusterDistance per calcolare la distanza
 * tra cluster utilizzando il metodo Average-Link.
 * La distanza Average-Link è definita come la media delle distanze tra tutte le
 * possibili coppie di elementi appartenenti ai due cluster.
 */
public class AverageLinkDistance implements ClusterDistance {
    /**
     * Calcola la distanza Average-Link tra due cluster.
     * La distanza è calcolata come la somma delle distanze tra ogni coppia di esempi
     * appartenenti ai due cluster, divisa per il prodotto delle dimensioni dei cluster.
     *
     * @param c1 primo cluster
     * @param c2 secondo cluster
     * @param d dataset contenente gli esempi
     * @return la media delle distanze tra tutti gli elementi dei due cluster
     * @throws InvalidSizeException se si verificano problemi con le dimensioni durante il calcolo della distanza
     */
    public double distance(Cluster c1, Cluster c2, Data d) throws InvalidSizeException {
        double sum = 0.0;

        Iterator<Integer> it1 = c1.iterator();
        while (it1.hasNext()) {
            Example e1 = d.getExample(it1.next());
            Iterator<Integer> it2 = c2.iterator();
            while (it2.hasNext()) {
                sum += e1.distance(d.getExample(it2.next()));
            }
        }
        return sum / (c1.getSize() * c2.getSize());
    }
}