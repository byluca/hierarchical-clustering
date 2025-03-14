package Server.src.clustering;

import java.io.*;

import Server.src.data.Data;

class Dendrogram implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ClusterSet[] tree; // modella il dendrogramma

    Dendrogram(int depth) throws InvalidDepthException {
        if (depth <= 0) {
            throw new InvalidDepthException("Profondità non valida!\n");
        }
        tree = new ClusterSet[depth];
    }

    // inserisce il cluster set c al livello level di tree
    void setClusterSet(ClusterSet c, int level) {
        tree[level] = c;
    }

    // restituisce il cluster set al livello level di tree
    ClusterSet getClusterSet(int level) {
        return tree[level];
    }

    // restituisce la profondità del dendrogramma
    int getDepth() {
        return tree.length;
    }

    // restituisce una rappresentazione testuale del dendrogramma
    public String toString(){
        String v = "";
        for (int i = 0; i < tree.length; i++) {
            v += ("level" + i + ":\n" + tree[i] + "\n");
        }
        return v;
    }

    // restituisce una rappresentazione testuale del dendrogramma
    public String toString(Data data) throws InvalidDepthException {
        String v = "";
        for (int i = 0; i < tree.length; i++) {
            v += ("level" + i + ":\n" + tree[i].toString(data) + "\n");
        }
        return v;
    }
}