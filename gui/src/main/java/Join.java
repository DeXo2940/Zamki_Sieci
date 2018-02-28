import com.sun.java.accessibility.util.GUIInitializedListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class Join implements Initializable {
    @FXML
    private Button btn;
    private Stage stage;
    private Client client;

    @FXML
    private TextField srv, port;

    public Join(Stage primaryStage) {
        this.stage = primaryStage;
    }
    public void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = new Client();
        btn.setOnAction((ActionEvent actionEvent) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI.fxml"));
                Parent pane = loader.load();
                Controller controller = loader.getController();
                boolean gotit = false;

                    try {
                        gotit = client.doIt(srv.getText(), Integer.parseInt(port.getText()));
                    } catch (Exception e) {
                        alert("Nie można się połączyć!");
                    }


                if(gotit) {
                    controller.setClient(client);
                    stage.getScene().setRoot(pane);
                    stage.setTitle("Zamki");
                    stage.setHeight(700);
                    stage.setWidth(1150);
                    stage.setResizable(false);
                    stage.centerOnScreen();
                }
            } catch (UnknownHostException e) {
                alert("Nieprawidłowy adres IP lub port serwera!");
            } catch (Exception e) {
                alert("Wystąpił błąd!");
            }
        });
    }
    public void exitApplication() {
        try {
            client.getSocket().close();
            System.out.println("Zamknąłem socket");
        } catch (IOException e) {
            alert("Nie udało się rozłączyć");
        }
    }
}
