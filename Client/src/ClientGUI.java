package Client.src;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * ClientGUI è la classe che implementa l'interfaccia grafica per il client.
 * Fornisce un'interfaccia utente moderna per connettersi al server, caricare dati,
 * eseguire l'apprendimento e salvare il dendrogramma.
 * <p>
 * Questa versione è stata modernizzata usando la libreria FlatLaf per un aspetto
 * pulito e un layout basato su helper methods per la massima leggibilità.
 */
public class ClientGUI extends JFrame {

    //<editor-fold desc="Constants">
    private static final String FRAME_TITLE = "Modern Dendrogram Client";
    private static final String LABEL_SERVER_IP = "Server IP:";
    private static final String LABEL_PORT = "Porta:";
    private static final String LABEL_DB_TABLE = "Tabella DB:";
    private static final String LABEL_DENDRO_FILE = "File Dendrogramma:";
    private static final String LABEL_PARAMETERS = "Parametri:";
    private static final String BUTTON_CONNECT = "Connetti";
    private static final String BUTTON_DISCONNECT = "Disconnetti";
    private static final String BUTTON_LOAD_FROM_DB = "Carica da DB";
    private static final String BUTTON_LOAD_FROM_FILE = "Carica da File";
    private static final String BUTTON_MINE_DENDROGRAM = "Apprendi Dendrogramma";
    private static final String BUTTON_SAVE_OUTPUT = "Salva Output";
    private static final String PLACEHOLDER_IP = "localhost";
    private static final String PLACEHOLDER_PORT = "2025";
    private static final String PLACEHOLDER_TABLE = "Nome tabella DB (es. play_tennis)";
    private static final String PLACEHOLDER_DENDRO_FILE = "Nome file (es. dendro.dat)";
    private static final String PLACEHOLDER_DEPTH = "Profondità";
    private static final String TITLE_CONNECTION = "1. Connessione al Server";
    private static final String TITLE_OPERATIONS = "2. Operazioni";
    private static final String TITLE_OUTPUT = "3. Output dal Server";
    private static final Insets GBC_INSETS = new Insets(5, 5, 5, 5);
    //</editor-fold>

    //<editor-fold desc="UI Components">
    // --- UI Components ---
    private JTextField serverIPField;
    private JTextField portField;
    private JButton connectButton;
    private JTextField tableNameField;
    private JButton loadDataButton;
    private JTextField fileNameField;
    private JButton loadDendrogramButton;
    private JTextField depthField;
    private JComboBox<String> distanceTypeComboBox;
    private JButton mineDendrogramButton;
    private JTextArea outputArea;
    private JButton saveFileButton;
    private JLabel statusLabel;
    private final Icon connectIcon = UIManager.getIcon("Actions.connect");
    private final Icon disconnectIcon = UIManager.getIcon("Actions.disconnect");
    private final Icon saveIcon = UIManager.getIcon("Actions.menu-saveall");
    //</editor-fold>

    //<editor-fold desc="Client Logic State">
    // --- Client Logic ---
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private boolean isConnected = false;
    private boolean isDataLoaded = false;
    private String dendrogramToSave = null;
    //</editor-fold>

    /**
     * Constructor for the ClientGUI class.
     * Initializes components, organizes the layout, and adds event listeners.
     */
    public ClientGUI() {
        super(FRAME_TITLE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);

        initComponents();
        createMenuBar();
        layoutComponents();
        addListeners();

        setLocationRelativeTo(null);
        updateUIState();
    }

    /**
     * Initializes all GUI components.
     */
    private void initComponents() {
        // Connection
        serverIPField = new JTextField(15);
        portField = new JTextField(5);
        connectButton = new JButton(BUTTON_CONNECT, connectIcon);
        Placeholder.add(serverIPField, PLACEHOLDER_IP);
        Placeholder.add(portField, PLACEHOLDER_PORT);

        // Data Loading from DB
        tableNameField = new JTextField(15);
        loadDataButton = new JButton(BUTTON_LOAD_FROM_DB);
        Placeholder.add(tableNameField, PLACEHOLDER_TABLE);

        // Dendrogram Loading from File
        fileNameField = new JTextField(15);
        loadDendrogramButton = new JButton(BUTTON_LOAD_FROM_FILE);
        Placeholder.add(fileNameField, PLACEHOLDER_DENDRO_FILE);

        // Dendrogram Mining
        depthField = new JTextField(5);
        distanceTypeComboBox = new JComboBox<>(new String[]{"single-link", "average-link"});
        mineDendrogramButton = new JButton(BUTTON_MINE_DENDROGRAM);
        Placeholder.add(depthField, PLACEHOLDER_DEPTH);

        // Output and Save
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        saveFileButton = new JButton(BUTTON_SAVE_OUTPUT, saveIcon);

        // Status Bar
        statusLabel = new JLabel("Non connesso");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
    }

    /**
     * Organizes the components into the main frame using helper methods.
     */
    private void layoutComponents() {
        // Main content panel with padding
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel containing connection and operations
        JPanel topControlsPanel = new JPanel();
        topControlsPanel.setLayout(new BoxLayout(topControlsPanel, BoxLayout.Y_AXIS));
        topControlsPanel.add(createConnectionPanel());
        topControlsPanel.add(createOperationsPanel());

        contentPanel.add(topControlsPanel, BorderLayout.NORTH);
        contentPanel.add(createOutputPanel(), BorderLayout.CENTER);

        // Main window layout
        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    /**
     * Creates the server connection panel.
     * @return A configured JPanel for connection controls.
     */
    private JPanel createConnectionPanel() {
        JPanel panel = createTitledPanel(TITLE_CONNECTION);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = GBC_INSETS;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel(LABEL_SERVER_IP), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; panel.add(serverIPField, gbc);
        gbc.gridx = 2; gbc.weightx = 0; panel.add(new JLabel(LABEL_PORT), gbc);
        gbc.gridx = 3; gbc.weightx = 0.2; panel.add(portField, gbc);
        gbc.gridx = 4; gbc.weightx = 0; panel.add(connectButton, gbc);

        return panel;
    }

    /**
     * Creates the operations panel, including data loading and dendrogram mining.
     * @return A configured JPanel for operations.
     */
    private JPanel createOperationsPanel() {
        JPanel panel = createTitledPanel(TITLE_OPERATIONS);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = GBC_INSETS;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Load data from DB
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0; panel.add(new JLabel(LABEL_DB_TABLE), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = 3; panel.add(tableNameField, gbc);
        gbc.gridx = 4; gbc.weightx = 0; gbc.gridwidth = 1; panel.add(loadDataButton, gbc);

        // Row 2: Load dendrogram from File
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel(LABEL_DENDRO_FILE), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; panel.add(fileNameField, gbc);
        gbc.gridx = 4; gbc.gridwidth = 1; panel.add(loadDendrogramButton, gbc);

        // Row 3: Mine dendrogram
        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel(LABEL_PARAMETERS), gbc);
        gbc.gridx = 1; gbc.gridwidth = 1; panel.add(depthField, gbc);
        gbc.gridx = 2; panel.add(distanceTypeComboBox, gbc);
        gbc.gridx = 3; gbc.gridwidth = 2; panel.add(mineDendrogramButton, gbc);

        return panel;
    }

    /**
     * Creates the output panel with a text area and save button.
     * @return A configured JPanel for output.
     */
    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(TITLE_OUTPUT));
        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        JPanel outputActionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        outputActionsPanel.add(saveFileButton);
        panel.add(outputActionsPanel, BorderLayout.SOUTH);

        return panel;
    }
    
    /**
     * Creates the status bar panel.
     * @return A configured JPanel for the status bar.
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
        statusBar.add(statusLabel, BorderLayout.WEST);
        return statusBar;
    }

    /**
     * A utility to create a panel with a titled border and standard padding.
     * @param title The title for the border.
     * @return A configured JPanel.
     */
    private JPanel createTitledPanel(String title) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(title),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return panel;
    }

    /**
     * Creates and adds the menu bar with theme-switching options.
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu viewMenu = new JMenu("Aspetto");
        ButtonGroup themeGroup = new ButtonGroup();

        JRadioButtonMenuItem lightTheme = new JRadioButtonMenuItem("Light Theme", true);
        lightTheme.addActionListener(e -> setTheme(new FlatLightLaf()));

        JRadioButtonMenuItem darkTheme = new JRadioButtonMenuItem("Dark Theme");
        darkTheme.addActionListener(e -> setTheme(new FlatDarkLaf()));

        themeGroup.add(lightTheme);
        themeGroup.add(darkTheme);
        viewMenu.add(lightTheme);
        viewMenu.add(darkTheme);
        menuBar.add(viewMenu);
        setJMenuBar(menuBar);
    }
    
    /**
     * Applies a new Look and Feel to the application.
     * @param laf The LookAndFeel to apply.
     */
    private void setTheme(LookAndFeel laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to set Look and Feel: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    /**
     * Adds event listeners to the interactive components.
     */
    private void addListeners() {
        connectButton.addActionListener(e -> connectToServer());
        loadDataButton.addActionListener(e -> loadDataOnServer());
        loadDendrogramButton.addActionListener(e -> loadDendrogramFromFileOnServer());
        mineDendrogramButton.addActionListener(e -> mineDendrogramOnServer());
        saveFileButton.addActionListener(e -> saveToFile());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeConnection();
            }
        });
    }

    /**
     * Updates the enabled/disabled state of UI components based on the
     * application's current state (connected, data loaded, etc.).
     */
    private void updateUIState() {
        boolean enableConnectionFields = !isConnected;
        boolean enableDataLoading = isConnected;
        boolean enableOperationsAfterLoad = isDataLoaded;

        // Connection fields
        serverIPField.setEnabled(enableConnectionFields);
        portField.setEnabled(enableConnectionFields);

        // Connect/Disconnect Button
        if (isConnected) {
            connectButton.setText(BUTTON_DISCONNECT);
            connectButton.setIcon(disconnectIcon);
            statusLabel.setText("Connesso a: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        } else {
            connectButton.setText(BUTTON_CONNECT);
            connectButton.setIcon(connectIcon);
            statusLabel.setText("Non connesso");
            isDataLoaded = false; // Reset data loaded state on disconnect
        }

        // Data operations
        tableNameField.setEnabled(enableDataLoading);
        loadDataButton.setEnabled(enableDataLoading);
        fileNameField.setEnabled(enableDataLoading); // Can load from file anytime if connected
        loadDendrogramButton.setEnabled(enableDataLoading);
        
        // Mining operations (only enabled if data is loaded from DB)
        depthField.setEnabled(enableOperationsAfterLoad);
        distanceTypeComboBox.setEnabled(enableOperationsAfterLoad);
        mineDendrogramButton.setEnabled(enableOperationsAfterLoad);

        // Save button
        saveFileButton.setEnabled(dendrogramToSave != null && !dendrogramToSave.isEmpty());
    }

    //<editor-fold desc="Client Logic Methods (Unchanged)">
    // --- Metodi di Logica del Client (INVARIATI) ---
    private void connectToServer() {
        if (isConnected) {
            closeConnection();
            isConnected = false;
            outputArea.setText("Disconnesso dal server.");
        } else {
            try {
                String ip = serverIPField.getText().trim();
                int port = Integer.parseInt(portField.getText().trim());

                InetAddress addr = InetAddress.getByName(ip);
                socket = new Socket(addr, port);

                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());

                outputArea.setText("Connessione stabilita con " + socket.toString() + "\n");
                isConnected = true;

            } catch (IOException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Errore di connessione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                outputArea.setText("Errore di connessione: " + ex.getMessage() + "\n");
            }
        }
        updateUIState();
    }

    private void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException ex) {
            appendToOutput("\nErrore durante la chiusura: " + ex.getMessage() + "\n");
        }
    }
    
    private void appendToOutput(String message) {
        outputArea.append(message + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private void loadDataOnServer() {
        if (!isConnected) {
            appendToOutput("Non sei connesso al server.");
            return;
        }

        try {
            String tableName = tableNameField.getText().trim();
            if (tableName.isEmpty()) {
                appendToOutput("Il nome della tabella non può essere vuoto.");
                return;
            }

            out.writeObject(0);
            out.writeObject(tableName);
            out.flush();

            String risposta = (String) (in.readObject());
            if (risposta.equals("OK")) {
                appendToOutput("Dati per la tabella '" + tableName + "' caricati correttamente.");
                isDataLoaded = true;
            } else {
                appendToOutput("Errore dal server: " + risposta);
                isDataLoaded = false;
            }
        } catch (IOException | ClassNotFoundException ex) {
            appendToOutput("Errore I/O durante il caricamento dati: " + ex.getMessage());
        }
        updateUIState();
    }
    
    private void loadDendrogramFromFileOnServer() {
        if (!isConnected) {
            appendToOutput("Non sei connesso al server.");
            return;
        }
        
        try {
            String fileName = fileNameField.getText().trim();
            if (fileName.isEmpty()) {
                appendToOutput("Il nome del file non può essere vuoto.");
                return;
            }

            out.writeObject(2);
            out.writeObject(fileName);
            out.flush();

            String risposta = (String) (in.readObject());
            if (risposta.equals("OK")) {
                dendrogramToSave = (String) in.readObject();
                outputArea.setText("Dendrogramma caricato da '" + fileName + "':\n\n");
                outputArea.append(dendrogramToSave);
            } else {
                appendToOutput("Errore dal server: " + risposta);
                dendrogramToSave = null;
            }
        } catch (IOException | ClassNotFoundException ex) {
            appendToOutput("Errore I/O durante il caricamento del dendrogramma: " + ex.getMessage());
        }
        updateUIState();
    }
    
    private void mineDendrogramOnServer() {
        if (!isDataLoaded) {
            appendToOutput("Caricare prima i dati dal database.");
            return;
        }
        
        try {
            int depth;
            try {
                depth = Integer.parseInt(depthField.getText().trim());
            } catch (NumberFormatException ex) {
                appendToOutput("La profondità deve essere un numero intero.");
                return;
            }

            int dType = distanceTypeComboBox.getSelectedIndex() + 1; // 1 per single, 2 per average

            out.writeObject(1);
            out.writeObject(depth);
            out.writeObject(dType);
            out.flush();

            String risposta = (String) (in.readObject());
            if (risposta.equals("OK")) {
                appendToOutput("Apprendimento completato. Dendrogramma ricevuto.");
                dendrogramToSave = (String) in.readObject();
                outputArea.setText(dendrogramToSave);

                // Chiede il nome del file da salvare sul SERVER
                String fileNameOnServer = JOptionPane.showInputDialog(this,
                        "Inserire il nome con cui salvare il dendrogramma sul server:",
                        "Salva Dendrogramma sul Server", JOptionPane.QUESTION_MESSAGE);
                
                if (fileNameOnServer != null && !fileNameOnServer.trim().isEmpty()) {
                    out.writeObject(fileNameOnServer.trim());
                    out.flush();
                    appendToOutput("Nome file '" + fileNameOnServer + "' inviato al server.");
                } else {
                    out.writeObject("annulla"); // informa il server di annullare
                    appendToOutput("Operazione di salvataggio su server annullata.");
                }

            } else {
                appendToOutput("Errore dal server: " + risposta);
                dendrogramToSave = null;
            }
        } catch (IOException | ClassNotFoundException ex) {
             appendToOutput("Errore I/O durante l'apprendimento: " + ex.getMessage());
        }
        updateUIState();
    }
    
    private void saveToFile() {
        if (dendrogramToSave == null || dendrogramToSave.isEmpty()) {
            appendToOutput("Non c'è alcun dendrogramma da salvare.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salva Dendrogramma su File Locale");
        fileChooser.setSelectedFile(new File("dendrogramma.txt"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(dendrogramToSave);
                appendToOutput("Dendrogramma salvato localmente in: " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Errore durante il salvataggio del file locale: " + ex.getMessage(), "Errore di Salvataggio", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //</editor-fold>

    /**
     * The main method to start the application. Sets the modern Look and Feel.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Use FlatLaf for a modern look and feel
        try {
            FlatDarkLaf.setup(); // Avvia di default in tema scuro
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf. Using default Look and Feel.");
        }

        SwingUtilities.invokeLater(() -> new ClientGUI().setVisible(true));
    }
    
    /**
     * Utility class to add placeholder text to a JTextField.
     * The placeholder disappears when the field gains focus or contains text.
     */
    private static class Placeholder implements FocusListener {
        private final JTextField textField;
        private final String placeholder;
        private boolean isPlaceholderActive;

        private Placeholder(JTextField textField, String placeholder) {
            this.textField = textField;
            this.placeholder = placeholder;
            this.isPlaceholderActive = false;
            textField.addFocusListener(this);
            showPlaceholder(); // Call this to set initial state
        }
        
        public static void add(JTextField textField, String placeholder) {
            new Placeholder(textField, placeholder);
        }

        private void showPlaceholder() {
            if (textField.getText().isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(UIManager.getColor("TextField.placeholderForeground"));
                isPlaceholderActive = true;
            }
        }

        private void hidePlaceholder() {
            if (isPlaceholderActive) {
                textField.setText("");
                textField.setForeground(UIManager.getColor("TextField.foreground"));
                isPlaceholderActive = false;
            }
        }
        
        @Override
        public void focusGained(FocusEvent e) {
            hidePlaceholder();
        }
        
        @Override
        public void focusLost(FocusEvent e) {
            showPlaceholder();
        }
    }
}