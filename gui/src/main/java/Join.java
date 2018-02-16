import com.sun.java.accessibility.util.GUIInitializedListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Join implements Initializable {
    @FXML
    private Button btn;
    private Stage stage;

    @FXML private URL location;
    @FXML private ResourceBundle resources;

    public Join(Stage primaryStage) {
        this.stage = primaryStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn.setOnAction((ActionEvent event) -> {
            try {
                Parent pane = FXMLLoader.load(
                        getClass().getResource("/GUI.fxml"));
                stage.getScene().setRoot(pane);
                stage.setHeight(850);
                stage.setWidth(1120);
                stage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
