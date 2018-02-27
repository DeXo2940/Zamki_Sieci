import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button endGame;
    private Client client;
    private ArrayList<Rectangle> cards;
    private ArrayList<Rectangle> myCastle;
    private ArrayList<Rectangle> theirCastle;
    @FXML
    private AnchorPane root, castlePane, cardsPane;

    public void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void drawRectangles() {
        cards = new ArrayList<>();
        int c = client.getGame().getHowManyCards();
        Rectangle rect = new Rectangle();
        if (c > 0) {

            rect.setHeight(100);
            rect.setWidth(50);
            rect.setFill(Color.BLUE);
            cards.add(rect);
            cardsPane.getChildren().add(cards.get(0));
            cards.get(0).toFront();
            rect.setX(20);
            rect.setY(60);

        for (int i = 1; i < 50; i++) {
            if (i > c - 1) break;
            rect = new Rectangle();
            rect.setHeight(100);
            rect.setWidth(50);
            rect.setFill(Color.BLUE);
            cards.add(rect);
            cardsPane.getChildren().add(cards.get(i));
            cards.get(i).toFront();
            if (i < 15) {
                rect.setY(60);
                rect.setX(cards.get(0).getX() + i * 60);
            } else {
                if (i > 14 & i < 30) {
                    rect.setY(180);
                    rect.setX(cards.get(i-15).getX());
                } else {
                    rect.setY(300);
                    rect.setX(cards.get(i-30).getX());
                }

            }
        }
        }
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
                client.getGame().getHowManyCardsProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if(!Objects.equals(number, t1)) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    cards.removeAll(cards);
                                    cardsPane.getChildren().removeAll(cardsPane.getChildren());
                                    drawRectangles();
                                    for (int i = 0; i < cards.size(); i++) {
                                        int finalI = i;
                                        cards.get(i).setOnMouseClicked((MouseEvent event) -> {
                                            if(client.ifMyMove())
                                            client.chooseCard(finalI);
                                            else alert("Nie twój ruch!");
                                        });
                                    }
                                }
                            });
                            System.out.println("Nowa wartość:" +t1);
                        }
                    }
                });

                client.run();

                return null;
            }
        };
        new Thread(task).start();
        drawRectangles();
    }
}
