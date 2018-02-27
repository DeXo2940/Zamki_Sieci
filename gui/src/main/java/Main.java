import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private FXMLLoader loader;
    private int sceneNr = 0;

    @Override
    public void init() throws Exception
    {
        loader = new FXMLLoader(getClass().getResource("/join.fxml"));
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        Join controller = new Join(primaryStage);
        loader.setController(controller);

        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void stop(){

    }

    public static void main(String[] args) {
        launch(args);
    }

}
