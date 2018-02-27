import com.sun.java.accessibility.util.GUIInitializedListener;
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

    @FXML
    private TextField srv, nick, port;

    public Join(Stage primaryStage) {
        this.stage = primaryStage;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Client client = new Client();
        btn.setOnAction((ActionEvent actionEvent) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI.fxml"));
                Parent pane = loader.load();
                Controller controller = loader.getController();
                client.doIt(srv.getText(), Integer.parseInt(port.getText()));

                        //wyjeb wyjątek jak nie ma serwera
                controller.setClient(client);
                stage.getScene().setRoot(pane);
                stage.setHeight(850);
                stage.setWidth(1150);
                stage.centerOnScreen();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
