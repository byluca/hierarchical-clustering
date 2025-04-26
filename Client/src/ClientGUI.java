package Client.src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * ClientGUI è la classe che implementa l'interfaccia grafica per il client.
 * Fornisce un'interfaccia utente per connettersi al server, caricare dati,
 * eseguire l'apprendimento e salvare il dendrogramma.
 */
public class ClientGUI extends JFrame {
    // Componenti dell'interfaccia grafica
    private JTextField serverIPField;
    private JTextField portField;
    private JTextField tableNameField;
    private JTextField fileNameField;
    private JTextField depthField;
    private JComboBox<String> distanceTypeComboBox;
    private JTextArea outputArea;
    private JButton connectButton;
    private JButton loadDataButton;
    private JButton loadDendrogramButton;
    private JButton mineDendrogramButton;
    private JButton saveFileButton;

    // Connessione al server
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private boolean isConnected = false;
    private boolean isDataLoaded = false;
    private String dendrogramToSave = null;

    /**
     * Costruttore della classe ClientGUI.
     * Inizializza i componenti, organizza il layout e aggiunge i listener.
     */
    public ClientGUI() {
        super("Cliente Dendrogramma");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        initComponents();
        layoutComponents();
        addListeners();
        setLocationRelativeTo(null);
    }

    /**
     * Inizializza i componenti dell'interfaccia grafica.
     */
    private void initComponents() {
        serverIPField = new JTextField("localhost", 15);
        portField = new JTextField("2025", 5);
        tableNameField = new JTextField(15);
        fileNameField = new JTextField(15);
        depthField = new JTextField(5);
        distanceTypeComboBox = new JComboBox<>(new String[]{"single-link", "average-link"});
        outputArea = new JTextArea(15, 50);
        outputArea.setEditable(false);

        connectButton = new JButton("Connetti");
        loadDataButton = new JButton("Carica Dati");
        loadDendrogramButton = new JButton("Carica Dendrogramma");
        mineDendrogramButton = new JButton("Apprendi Dendrogramma");
        saveFileButton = new JButton("Salva Dendrogramma");

        // Disabilita i pulsanti fino alla connessione
        loadDataButton.setEnabled(false);
        loadDendrogramButton.setEnabled(false);
        mineDendrogramButton.setEnabled(false);
        saveFileButton.setEnabled(false);
    }

    /**
     * Organizza i componenti nell'interfaccia grafica.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout());

        // Pannello superiore per la connessione
        JPanel connectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        connectionPanel.setBorder(BorderFactory.createTitledBorder("Connessione"));
        connectionPanel.add(new JLabel("Server IP:"));
        connectionPanel.add(serverIPField);
        connectionPanel.add(new JLabel("Porta:"));
        connectionPanel.add(portField);
        connectionPanel.add(connectButton);

        // Pannello per il caricamento dei dati
        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dataPanel.setBorder(BorderFactory.createTitledBorder("Caricamento Dati"));
        dataPanel.add(new JLabel("Nome Tabella:"));
        dataPanel.add(tableNameField);
        dataPanel.add(loadDataButton);

        // Pannello per le operazioni
        JPanel operationsPanel = new JPanel(new GridLayout(2, 1));
        operationsPanel.setBorder(BorderFactory.createTitledBorder("Operazioni"));

        // Sottopannello per il caricamento del dendrogramma
        JPanel loadDendrogramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        loadDendrogramPanel.add(new JLabel("Nome File:"));
        loadDendrogramPanel.add(fileNameField);
        loadDendrogramPanel.add(loadDendrogramButton);

        // Sottopannello per l'apprendimento del dendrogramma
        JPanel mineDendrogramPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mineDendrogramPanel.add(new JLabel("Profondità:"));
        mineDendrogramPanel.add(depthField);
        mineDendrogramPanel.add(new JLabel("Distanza:"));
        mineDendrogramPanel.add(distanceTypeComboBox);
        mineDendrogramPanel.add(mineDendrogramButton);

        operationsPanel.add(loadDendrogramPanel);
        operationsPanel.add(mineDendrogramPanel);

        // Pannello per l'output
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Output"));
        outputPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        outputPanel.add(saveFileButton, BorderLayout.SOUTH);

        // Pannello principale
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new GridLayout(3, 1));
        topPanel.add(connectionPanel);
        topPanel.add(dataPanel);
        topPanel.add(operationsPanel);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(outputPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    /**
     * Aggiunge i listener agli eventi dei componenti.
     */
    private void addListeners() {
        connectButton.addActionListener(e -> connectToServer());
        loadDataButton.addActionListener(e -> loadDataOnServer());
        loadDendrogramButton.addActionListener(e -> loadDendrogramFromFileOnServer());
        mineDendrogramButton.addActionListener(e -> mineDendrogramOnServer());
        saveFileButton.addActionListener(e -> saveToFile());

        // Aggiungi un listener per la chiusura della finestra
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeConnection();
            }
        });
    }

    /**
     * Connette il client al server.
     * Se già connesso, chiude la connessione.
     */
    private void connectToServer() {
        if (isConnected) {
            closeConnection();
            connectButton.setText("Connetti");
            isConnected = false;
            loadDataButton.setEnabled(false);
            loadDendrogramButton.setEnabled(false);
            mineDendrogramButton.setEnabled(false);
            saveFileButton.setEnabled(false);
            outputArea.setText("");
            return;
        }

        try {
            String ip = serverIPField.getText().trim();
            int port = Integer.parseInt(portField.getText().trim());

            InetAddress addr = InetAddress.getByName(ip);
            socket = new Socket(addr, port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            outputArea.setText("Connessione stabilita con " + socket.toString() + "\n");
            connectButton.setText("Disconnetti");
            isConnected = true;
            loadDataButton.setEnabled(true);

        } catch (IOException | NumberFormatException ex) {
            outputArea.setText("Errore di connessione: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Chiude la connessione con il server.
     */
    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            outputArea.append("Errore durante la chiusura della connessione: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Carica i dati dal database sul server.
     * Invia il nome della tabella al server e attende la conferma.
     */
    private void loadDataOnServer() {
        if (!isConnected) {
            outputArea.append("Non sei connesso al server\n");
            return;
        }

        try {
            String tableName = tableNameField.getText().trim();
            if (tableName.isEmpty()) {
                outputArea.append("Il nome della tabella non può essere vuoto\n");
                return;
            }

            out.writeObject(0);
            out.writeObject(tableName);

            String risposta = (String) (in.readObject());
            if (risposta.equals("OK")) {
                outputArea.append("Dati caricati con successo\n");
                isDataLoaded = true;
                loadDendrogramButton.setEnabled(true);
                mineDendrogramButton.setEnabled(true);
            } else {
                outputArea.append("Errore: " + risposta + "\n");
            }
        } catch (IOException | ClassNotFoundException ex) {
            outputArea.append("Errore durante il caricamento dei dati: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Carica il dendrogramma da file sul server.
     * Invia il nome del file al server e visualizza il dendrogramma in output se il caricamento va a buon fine.
     */
    private void loadDendrogramFromFileOnServer() {
        if (!isConnected || !isDataLoaded) {
            outputArea.append("Non sei connesso al server o i dati non sono stati caricati\n");
            return;
        }

        try {
            String fileName = fileNameField.getText().trim();
            if (fileName.isEmpty()) {
                outputArea.append("Il nome del file non può essere vuoto\n");
                return;
            }

            out.writeObject(2);
            out.writeObject(fileName);

            String risposta = (String) (in.readObject());
            if (risposta.equals("OK")) {
                dendrogramToSave = (String) in.readObject();
                outputArea.setText(dendrogramToSave);
                saveFileButton.setEnabled(true);
            } else {
                outputArea.append("Errore: " + risposta + "\n");
            }
        } catch (IOException | ClassNotFoundException ex) {
            outputArea.append("Errore durante il caricamento del dendrogramma: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Apprende il dendrogramma sul server.
     * Invia i parametri di profondità e tipo di distanza, e se l'operazione ha successo,
     * richiede all'utente il nome per salvare il dendrogramma.
     */
    private void mineDendrogramOnServer() {
        if (!isConnected || !isDataLoaded) {
            outputArea.append("Non sei connesso al server o i dati non sono stati caricati\n");
            return;
        }

        try {
            String depthStr = depthField.getText().trim();
            if (depthStr.isEmpty()) {
                outputArea.append("La profondità non può essere vuota\n");
                return;
            }

            int depth;
            try {
                depth = Integer.parseInt(depthStr);
            } catch (NumberFormatException ex) {
                outputArea.append("La profondità deve essere un numero intero\n");
                return;
            }

            int dType = distanceTypeComboBox.getSelectedIndex() + 1;

            out.writeObject(1);
            out.writeObject(depth);
            out.writeObject(dType);

            String risposta = (String) (in.readObject());
            if (risposta.equals("OK")) {
                dendrogramToSave = (String) in.readObject();
                outputArea.setText(dendrogramToSave);

                // Richiedi il nome del file per salvare il dendrogramma
                String fileName = JOptionPane.showInputDialog(this,
                        "Inserire il nome dell'archivio (comprensivo di estensione):",
                        "Salva Dendrogramma", JOptionPane.QUESTION_MESSAGE);

                if (fileName != null && !fileName.isEmpty()) {
                    out.writeObject(fileName);
                    saveFileButton.setEnabled(true);
                } else {
                    outputArea.append("\nOperazione di salvataggio annullata\n");
                }
            } else {
                outputArea.append("Errore: " + risposta + "\n");
            }
        } catch (IOException | ClassNotFoundException ex) {
            outputArea.append("Errore durante l'apprendimento del dendrogramma: " + ex.getMessage() + "\n");
        }
    }

    /**
     * Salva il dendrogramma su un file locale.
     * Apre una finestra di dialogo per selezionare il percorso di salvataggio.
     */
    private void saveToFile() {
        if (dendrogramToSave == null) {
            outputArea.append("Non c'è alcun dendrogramma da salvare\n");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salva Dendrogramma");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(dendrogramToSave);
                outputArea.append("\nDendrogramma salvato con successo in " + fileToSave.getAbsolutePath() + "\n");
            } catch (IOException ex) {
                outputArea.append("\nErrore durante il salvataggio del file: " + ex.getMessage() + "\n");
            }
        }
    }

    /**
     * Metodo main per avviare l'applicazione ClientGUI.
     * @param args argomenti da linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientGUI().setVisible(true);
        });
    }
}
