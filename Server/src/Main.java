package Server.src;

import Server.src.server.MultiServer;

/**
 * Classe principale del server.
 * Punto di partenza dell'applicazione lato server.
 */
public class Main {

    /**
     * Metodo main del server.
     * Avvia il server istanziando la classe MultiServer.
     *
     * @param args argomenti passati da terminale (non utilizzati)
     */
    public static void main(String[] args) {
        MultiServer.instanceMultiServer();
    }
}