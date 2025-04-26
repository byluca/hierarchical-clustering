/**
 * Package contenente le classi per la gestione e manipolazione dei dati nell'applicazione.
 */
package Server.src.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Classe che modella un esempio come vettore di valori reali.
 * Fornisce funzionalità per manipolare il vettore e calcolare distanze tra esempi.
 * Implementa l'interfaccia Iterable per permettere di scorrere i valori contenuti.
 */
public class Example implements Iterable<Double> {
    /** Vettore di valori reali che rappresenta l'esempio */
    private List<Double> example;

    /**
     * Crea una nuova istanza di classe Example con un vettore vuoto.
     */
    public Example() {
        example = new LinkedList<>();
    }

    /**
     * Restituisce un iteratore per scorrere gli elementi dell'esempio.
     *
     * @return iteratore sui valori dell'esempio
     */
    public Iterator<Double> iterator() {
        return example.iterator();
    }

    /**
     * Aggiunge un nuovo valore in coda all'esempio.
     *
     * @param v valore da aggiungere all'esempio
     */
    public void add(Double v) {
        example.add(v);
    }

    /**
     * Restituisce il valore dell'esempio alla posizione specificata.
     *
     * @param index indice del valore da recuperare
     * @return il valore alla posizione specificata
     */
    Double get(int index) {
        return example.get(index);
    }

    /**
     * Calcola la distanza euclidea tra questo esempio e quello passato come parametro.
     * La distanza viene calcolata come somma dei quadrati delle differenze tra i valori
     * corrispondenti nei due esempi.
     *
     * @param newE esempio con cui calcolare la distanza
     * @return la distanza euclidea (somma dei quadrati delle differenze)
     * @throws InvalidSizeException se i due esempi hanno dimensioni diverse
     */
    public double distance(Example newE) throws InvalidSizeException {
        if(example.size() != newE.example.size()) {
            throw new InvalidSizeException("Gli esempi hanno dimensioni diverse!");
        }

        double sum = 0.0;
        Iterator<Double> iterator1 = example.iterator();
        Iterator<Double> iterator2 = newE.iterator();

        while (iterator1.hasNext() && iterator2.hasNext()) {
            double diff = iterator1.next() - iterator2.next();
            sum += Math.pow(diff, 2);
        }
        return sum;
    }

    /**
     * Crea una rappresentazione testuale dell'esempio come sequenza di valori separati da virgole.
     *
     * @return stringa che rappresenta l'esempio
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        Iterator<Double> iterator = iterator();

        if (iterator.hasNext()) {
            s.append(iterator.next());
        }
        while (iterator.hasNext()) {
            s.append(",").append(iterator.next());
        }
        return s.toString();
    }
}