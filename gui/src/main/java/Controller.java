import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button endGame;
    private Client client;

    public void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {

        //ustawić listenera na game.gethowmanycards
        endGame.setOnAction((ActionEvent event) -> {
            alert("Pomyślnie dodano klienta");
        });
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                client.run();
                return null;
            }
        };
        new Thread(task).start();
    }
}
