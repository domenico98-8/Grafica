package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controller {
    @FXML
    Text StateText;
    @FXML
    Circle State=new Circle(1000);
    @FXML
    javafx.scene.control.TextField TextBoxPort=new TextField();
    @FXML
    javafx.scene.control.TextField TextBoxHost;
    @FXML
    RadioButton RadioArchivio;
    @FXML
    RadioButton RadioData;
    @FXML
    Label erroreporta;
    @FXML
    Label errorehost;
    @FXML
    Pane Accesso;
    @FXML
    Pane Acquisition;
    @FXML
    Pane SceltaFile;
    @FXML
    Text TextDataFile;
    @FXML
    TextField TextBoxNomeFile;
    @FXML
    Pane AlberoDiRegressione;
    @FXML
    Label erroreNome;
    @FXML
    TextArea info;
    @FXML
    TextField inserimento;
    @FXML
    TextArea scelte;

    Boolean flag;
    String localhost="localhost";
    String PORT="8080";
    Socket socket=null;
    ObjectOutputStream out=null;
    ObjectInputStream in=null;
    String answer="";
    public void ConnessioneAlServer(ActionEvent actionEvent) throws IOException {
        if(localhost.compareTo(TextBoxHost.getText())==0 && PORT.compareTo(TextBoxPort.getText())==0){
            State.setFill(Paint.valueOf("GREEN"));
            StateText.setText("Connesso");
            InetAddress addr;
            try {
                addr = InetAddress.getByName("127.0.0.1");
            } catch (UnknownHostException e) {
                System.out.println(e.toString());
                return;
            }

            try {
                socket = new Socket(addr, Integer.valueOf(8080).intValue());
                System.out.println(socket);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());	; // stream con richieste del client

            }  catch (IOException e) {
                System.out.println(e.toString());
                return;
            }
            Accesso.setVisible(false);
            Acquisition.setVisible(true);


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
       flag=true;
    }

    public void EstraiData(ActionEvent actionEvent) {
        RadioArchivio.setSelected(false);
        RadioData.setSelected(true);
        flag=false;
    }

    public void Acquisisci(ActionEvent actionEvent) throws IOException {
        SceltaFile.setVisible(true);
        Acquisition.setVisible(false);
        if(flag){
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
                if (!flag) {
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
                if(flag) {
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
        out.writeObject(3);
        out.writeObject(Integer.parseInt(inserimento.getText()));
        answer=in.readObject().toString();
        if(answer.equals("QUERY")){
                out.writeObject(inserimento.getText());
                answer=in.readObject().toString();

                if(answer.equals("OK"))
                { // Reading prediction
                    answer=in.readObject().toString();
                    System.out.println("Predicted class:"+answer);

                }
                else //Printing error message
                    System.out.println(answer);
            }
            scelte.clear();
            answer=in.readObject().toString();
            scelte.setText(answer);


    }
}
