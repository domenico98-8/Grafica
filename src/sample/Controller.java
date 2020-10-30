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
 * Classe che include gli elementi grafici del client
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
     * TextBox che prende in input il nome della Porta
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
     * Label che riporta il fallimento con la connessione con il serve dovuto ad una porta errata
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
     * finestra di acquisizione di data o archivio
     */
    @FXML
    private Pane Acquisition;
    /**
     * Finestra di acquisizione del nome del file
     */
    @FXML
    private Pane SceltaFile;
    @FXML
    private Text TextDataFile;
    @FXML
    private TextField TextBoxNomeFile;
    @FXML
    private Pane AlberoDiRegressione;
    @FXML
    private Label erroreNome;
    @FXML
    private TextArea info;
    @FXML
    private TextField inserimento;
    @FXML
    private TextArea scelte;
    @FXML
    private TextArea valorepredetto;
    @FXML
    private Button inviaScelta;
    @FXML
    private Label MessaggioRipeti;
    @FXML
    private Button BottoneNo;
    @FXML
 	 private Button BottoneSi;
    @FXML
    private Label erroreServer;
    @FXML
    private Label erroreScelta;

    private final static String localhost="localhost";
    private final static String PORT="8080";
    private Socket socket=null;
    private ObjectOutputStream out=null;
    private ObjectInputStream in=null;
    private String answer="";
    boolean ripeti=true;

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

    public void ConnettiEstraiArchivio(ActionEvent actionEvent) {
       RadioArchivio.setSelected(true);
       RadioData.setSelected(false);
    }

    public void EstraiData(ActionEvent actionEvent) {
        RadioArchivio.setSelected(false);
        RadioData.setSelected(true);
    }

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
