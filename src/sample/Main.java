package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 *Classe che:
 * 1. Inizializza il titolo applicazione
 * 2. Richiama il file fxml
 * 3. Mostra a video l'applicazione
 * @author Alessia Laquale
 * @author Domenco Cascella
 * @author Patrizia Conte
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("AccessPage.fxml"));
        primaryStage.setTitle("Albero di Regressione");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Metodo che lancia l'applicazione
     * @param args :argomenti della riga di comando
     */
    public static void main(String[] args) {
        launch(args);
    }
}
