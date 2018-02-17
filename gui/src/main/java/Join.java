import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class Join implements Initializable {
    @FXML
    private Button btn;
    private Stage stage;

    @FXML private URL location;
    @FXML private ResourceBundle resources;

    @FXML
    private TextField srv, nick, port;

    public Join(Stage primaryStage) {
        this.stage = primaryStage;
    }

    private void sendToServer(String server, int port) throws Exception {
     //   Client client = new Client(server, port);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client client = new Client();
        btn.setOnAction((ActionEvent actionEvent) -> {
            try {
                Parent pane = FXMLLoader.load(
                        getClass().getResource("/GUI.fxml"));
                stage.getScene().setRoot(pane);
                stage.setHeight(850);
                stage.setWidth(1120);
                stage.centerOnScreen();
                Task<Void> task = new Task<Void>() {
                    @Override
                    public Void call() throws Exception {
                        client.doIt(srv.getText(), Integer.parseInt(port.getText()));
                        return null;
                    }
                };
                new Thread(task).start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
