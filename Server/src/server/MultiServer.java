package Server.src.server;

import java.io.*;
import java.net.*;

public class MultiServer {
    private final int PORT;
    private static MultiServer singleton = null;

    private MultiServer(int port) {
        this.PORT = port;
        run();
    }

    public static void instanceMultiServer() {
        if (singleton == null) {
            singleton = new MultiServer(2025);
        }
    }

    private void run() {
        try {

            ServerSocket s = new ServerSocket(PORT);
            System.out.println("Server avviato: " + s);

            while (true) {
                Socket socket = s.accept();
                System.out.println("Connessione client: " + socket);

                try {
                    new ServerOneClient(socket);
                } catch (IOException e) {
                    System.out.println("Errore gestione client: " + e.getMessage());
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println("Errore avvio server: " + e.getMessage());
        }
    }
}