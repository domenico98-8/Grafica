package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe che comunica con il server attraverso una interfaccia grafica
 * @author Alessia Laquale
 * @author Domenco Cascella
 * @author Patrizia Conte
 */
public class Controller {
    /**
     * Indica lo stato della connessione(Connesso/Sconnesso) con il server
     */
    @FXML
    private Text StateText;
    /**
     * Indica lo stato della connessione(Rosso: Sconnesso/Verde: Connesso)
     */
    @FXML
    private Circle State=new Circle(1000);
    /**
     * TextBox che prende in input il numero della Porta
     */
    @FXML
    private  javafx.scene.control.TextField TextBoxPort=new TextField();
    /**
     * TextBox che prende in input il nome dell'host
     */
    @FXML
    private javafx.scene.control.TextField TextBoxHost;
    /**
     * RadioButton che acquisisce da Archivio
     */
    @FXML
    private RadioButton RadioArchivio;
    /**
     * RadioButton che acquisisce da Data
     */
    @FXML
    private RadioButton RadioData;
    /**
     * Label che riporta il fallimento con la connessione con il server dovuto ad una porta errata
     */
    @FXML
    private Label erroreporta;
    /**
     * Label che riporta il fallimento con la connessione con il server dovuto ad un host errato
     */
    @FXML
    private Label errorehost;
    /**
     * Finestra di accesso al server
     */
    @FXML
    private Pane Accesso;
    /**
     * Finestra di acquisizione da data o archivio
     */
    @FXML
    private Pane Acquisition;
    /**
     * Finestra di acquisizione del nome del file
     */
    @FXML
    private Pane SceltaFile;
    /**
     * Titolo che indica l'inserimento del file o della tabella
     */
    @FXML
    private Text TextDataFile;
    /**
     * Texbox che prende in input il nome del file o tabella
     */
    @FXML
    private TextField TextBoxNomeFile;
    /**
     * Finestra di predizione con l'albero di regressione
     */
    @FXML
    private Pane AlberoDiRegressione;
    /**
     *Label che riporta l'errore di acquisizione di un file non trovata nel server o tabella non trovata nel database
     */
    @FXML
    private Label erroreNome;
    /**
     * Box che contiene le informazioni:
     * 1. Host
     * 2. Porta
     * 3. Tipo Acquisizione
     * 4. Nome file/tabella
     */
    @FXML
    private TextArea info;
    /**
     * Texbox che prende in input la scelta di predizione
     */
    @FXML
    private TextField inserimento;
    /**
     * Box che contiene le predizioni da scegliere
     */
    @FXML
    private TextArea scelte;
    /**
     * Box che contiene il valore predetto
     */
    @FXML
    private TextArea valorepredetto;
    /**
     * Bottone che invia la scelta della predizione al server
     */
    @FXML
    private Button inviaScelta;
    /**
     *Messaggio che appare al momento della fine della predizione che chiederà se vogliamo ripetere o no la predizione
     */
    @FXML
    private Label MessaggioRipeti;
    /**
     * Bottone che riporterà alla finestra di acquisizione di archivio o data
     */
    @FXML
    private Button BottoneNo;
    /**
     * Bottone che ripeterà una nuova acquisizione
     */
    @FXML
 	 private Button BottoneSi;
    /**
     * Label che riporta il fallimento della connessione al server
     */
    @FXML
    private Label erroreServer;
    /**
     * Label che riporta l'errore di acquisizione di una lettera o di una scelta di predizione fuori range
     */
    @FXML
    private Label erroreScelta;
    /**
     * Stringa che continene il nome dell'host
     */
    private final static String localhost="localhost";
    /**
     * Stringa che contiene numero di porta
     */
    private final static String PORT="8080";
    /**
     * Inizializzazione della socket
     */
    private Socket socket=null;
    /**
     * Inizializzazione di stream di output
     */
    private ObjectOutputStream out=null;
    /**
     * Inizializzazione di stream di input
     */
    private ObjectInputStream in=null;
    /**
     * Variabile stringa che conterrà le risposte del server
     */
    private String answer="";

    /**
     * Metodo che permette di connettersi con il Server
     * @param actionEvent : click del bottone buttonConnetti
     * @throws IOException :Errore generato quando si verifica un errore I/O
     */
    public void ConnessioneAlServer(ActionEvent actionEvent) throws IOException {
        if(localhost.compareTo(TextBoxHost.getText())==0 && PORT.compareTo(TextBoxPort.getText())==0){
            InetAddress addr;
            try {
                addr = InetAddress.getByName("127.0.0.1");
            } catch (UnknownHostException e) {
                System.out.println(e.toString());
                return;
            }
            try {
                erroreServer.setVisible(true);
                erroreporta.setVisible(false);
                errorehost.setVisible(false);
                socket = new Socket(addr, Integer.valueOf(8080).intValue());
                System.out.println(socket);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());	; // stream con richieste del client
                Accesso.setVisible(false);
                Acquisition.setVisible(true);
                RadioArchivio.setSelected(true);
            }  catch (IOException e) {
                System.out.println(e.toString());
                return;
            } finally {
                if(socket!=null){
                    State.setFill(Paint.valueOf("GREEN"));
                    StateText.setText("Connesso");
                    erroreServer.setVisible(false);
                }
            }

        }else{
            if(localhost.compareTo(TextBoxHost.getText())!=0) {
                errorehost.setVisible(true);
            }else{
                errorehost.setVisible(false);
            }
            if(PORT.compareTo(TextBoxPort.getText())!=0){
                erroreporta.setVisible(true);
            }else{
                erroreporta.setVisible(false);

            }
        }


    }

    /**
     *Setta RadioArchivio a vero e radioData a falso
     * @param actionEvent :click su RadioArchivio
     */
    public void EstraiArchivio(ActionEvent actionEvent) {
       RadioArchivio.setSelected(true);
       RadioData.setSelected(false);
    }

    /**
     * Setta RadioData a vero e radioArchivio a falso
     * @param actionEvent :click su RadioData
     */
    public void EstraiData(ActionEvent actionEvent) {
        RadioArchivio.setSelected(false);
        RadioData.setSelected(true);
    }

    /**
     * Invia il tipo di acquisizione al server
     * @param actionEvent :click sul bottone ButtonAcquisizione
     * @throws IOException :Errore generato quando si verifica un errore I/O
     */
    public void Acquisisci(ActionEvent actionEvent) throws IOException {
        SceltaFile.setVisible(true);
        Acquisition.setVisible(false);
        if(RadioArchivio.isSelected()){
            out.writeObject(2);
            TextDataFile.setText("Inserisci Nome File:");
        }else{
            out.writeObject(0);
            TextDataFile.setText("Inserisci Nome Tabella:");
        }

    }

    /**
     * Invia il nome del file/tabella acquisita al server
     * @param actionEvent :click sul bottone InviaFile
     * @throws IOException :Errore generato quando si verifica un errore I/O
     * @throws ClassNotFoundException :Eccezione lanciata quando un'applicazione non trova una classe con quel nome
     */
    public void InviaIlFile(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

            out.writeObject(TextBoxNomeFile.getText());
            answer = in.readObject().toString();
            if(answer.equals("Ripeti")){
                erroreNome.setVisible(true);
                TextBoxNomeFile.clear();
            }else {
                if (!RadioArchivio.isSelected()) {
                    answer = in.readObject().toString();
                    if (!answer.equals("OK")) {
                        System.out.println(answer);
                        return;
                    }
                    out.writeObject(1);
                }
                answer = in.readObject().toString();
                if (!answer.equals("OK")) {
                    System.out.println(answer);
                    return;
                }
                SceltaFile.setVisible(false);
                AlberoDiRegressione.setVisible(true);
                if(RadioArchivio.isSelected()) {
                    info.setText("Host: " + localhost+ "\nPORT: " + PORT + "\nAcquisizione da: Archivio"+"\nNome tabella: "+TextBoxNomeFile.getText());
                }else{
                    info.setText("Host: " + localhost+ "\nPORT: " + PORT + "\nAcquisizione da: Data"+"\nNome tabella: "+TextBoxNomeFile.getText());
                }

                out.writeObject(3);

                answer=in.readObject().toString();
                // Formualting query, reading answer
                answer=in.readObject().toString();
                scelte.setText(answer);



            }

    }

    /**
     *Invia la scelta della predizione al server
     * @param actionEvent :click sul bottone InviaScelta
     * @throws IOException :Errore generato quando si verifica un errore I/O
     * @throws ClassNotFoundException :Eccezione lanciata quando un'applicazione non trova una classe con quel nome
     */
    public void InviaSceltaPredizione(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
            try {
                out.writeObject(Integer.parseInt(inserimento.getText()));
                answer = in.readObject().toString();
            if (answer.equals("OK")) {
                answer = in.readObject().toString();
                valorepredetto.setText(answer);
                inviaScelta.setDisable(true);
                inserimento.setDisable(true);
                MessaggioRipeti.setVisible(true);
                BottoneSi.setVisible(true);
                BottoneNo.setVisible(true);
                scelte.setText("Fine Predizione!");
                erroreScelta.setVisible(false);


            } else if(!answer.equals("ERRORE")){
                answer = in.readObject().toString();
                scelte.setText(answer);
                erroreScelta.setVisible(false);
            }else{
                out.writeObject(3);
                answer=in.readObject().toString();
                answer=in.readObject().toString();
                scelte.setText(answer);
                throw new EOFException();
            }
            inserimento.clear();
            }catch (EOFException|NumberFormatException e){
                erroreScelta.setText("Fuori Range! Ripeti la predizione");
                erroreScelta.setVisible(true);
            }
    }

    /**
     * Ripete la predizione
     * @param actionEvent :click sul bottone BottoneSi
     * @throws IOException :Errore generato quando si verifica un errore I/O
     * @throws ClassNotFoundException :Eccezione lanciata quando un'applicazione non trova una classe con quel nome
     */
    public void Ripeti(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        inviaScelta.setDisable(false);
        inserimento.setDisable(false);
        MessaggioRipeti.setVisible(false);
        BottoneSi.setVisible(false);
        BottoneNo.setVisible(false);
        valorepredetto.clear();
        out.writeObject(3);
        answer=in.readObject().toString();
        answer=in.readObject().toString();
        scelte.setText(answer);
    }

    /**
     * Riporta alla finestra della scelta del tipo di acquisizione
     * @param actionEvent :click sul bottone BottoneNo
     * @throws IOException :Errore generato quando si verifica un errore I/O
     */
    public void NonRipetere(ActionEvent actionEvent) throws IOException {
        Acquisition.setVisible(true);
        AlberoDiRegressione.setVisible(false);
        out.writeObject(4);
        inviaScelta.setDisable(false);
        inserimento.setDisable(false);
        MessaggioRipeti.setVisible(false);
        BottoneSi.setVisible(false);
        BottoneNo.setVisible(false);
        valorepredetto.clear();

    }

    /**
     *Verifica se è stata inserita una lettera e rende visibile un messaggio di errore
     * @param keyEvent : pressione tasto
     */
    public void SoloNumeri(KeyEvent keyEvent) {
       String input=inserimento.getText();
       if(input.matches("[A-Z*]") || input.matches("[a-z*]")){
           erroreScelta.setText("Impossibile Inserire Lettere!");
          erroreScelta.setVisible(true);
           inserimento.clear();
       }else{
           erroreScelta.setVisible(false);
       }

    }
}
