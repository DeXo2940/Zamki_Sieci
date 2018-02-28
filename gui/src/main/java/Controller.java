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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.text.Text;

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
    @FXML
    private ArrayList<Rectangle> myCastle;
    private ArrayList<Rectangle> theirCastle;
    @FXML
    private AnchorPane root, castlePane, cardsPane;
    @FXML
    private RadioButton our, their;



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
            rect.setFill(Color.LIGHTSKYBLUE);
            rect.setStroke(Color.BLACK);
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
                rect.setFill(Color.LIGHTSKYBLUE);
                rect.setStroke(Color.BLACK);
                cards.add(rect);
                cardsPane.getChildren().add(cards.get(i));
                cards.get(i).toFront();
                if (i < 15) {
                    rect.setY(60);
                    rect.setX(cards.get(0).getX() + i * 60);
                } else {
                    if (i > 14 & i < 30) {
                        rect.setY(180);
                        rect.setX(cards.get(i - 15).getX());
                    } else {
                        rect.setY(300);
                        rect.setX(cards.get(i - 30).getX());
                    }

                }
            }
        }
    }


    public void initialize(URL url, ResourceBundle resourceBundle) {


        endGame.setOnAction((ActionEvent event) -> {
            alert("Pomyślnie dodano klienta");

        });

        final ToggleGroup group = new ToggleGroup();

        our.setToggleGroup(group);
        their.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (group.getSelectedToggle() != null) {
                    if(new_toggle.equals(our)) {
                        hideCastle(client.getGame().getOpposite().getNumber());
                        showCastle(client.getTeamNumber());
                        System.out.println("Nasz zamek");
                    }
                    else {
                        hideCastle(client.getTeamNumber());
                        showCastle(client.getGame().getOpposite().getNumber());
                        System.out.println("Ich zamek");
                    }


                }
            }
        });


    }

    private void hideCastle(Integer number) {
        castlePane.getChildren().clear();
        castlePane.getChildren().addAll(our, their);
    }

    private void showCastle(Integer teamNumber) {
       Integer size;
        if(teamNumber == client.getTeamNumber()) {
           size = client.getGame().getMine().getCastle().size();
           myCastle = new ArrayList<>();
           Team team = client.getGame().getMine();
           drawCastle(myCastle, castlePane, size, team, Color.YELLOW);

       }
       else {
            size = client.getGame().getOpposite().getCastle().size();
            theirCastle = new ArrayList<>();
            Team team = client.getGame().getOpposite();
            drawCastle(theirCastle, castlePane, size, team, Color.LAWNGREEN);
        }
    }

    public void drawCastle (ArrayList<Rectangle> listRec, AnchorPane pane, Integer size, Team team, Color color) {
        String l;
        for (int i = 0; i < size; i++) {
            Rectangle rect = new Rectangle();
            rect.setHeight(60);
            rect.setWidth(50);
            rect.setStroke(Color.BLACK);

            rect.setFill(color);
            l = String.valueOf(team.getCastle().get(i).getSign());

            Text text = new Text(l);
            listRec.add(rect);
            StackPane stackPane = new StackPane();
            pane.getChildren().add(stackPane);
            stackPane.getChildren().addAll(rect,text);
            if (i < 15) {
                stackPane.setLayoutY(30);
                stackPane.setLayoutX(20 + 55 * i);
            }
            else {
                stackPane.setLayoutX(120);
                stackPane.setLayoutY(20 + 55 * (i-15));
            }
        }
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
                        if (!Objects.equals(number, t1)) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    cards.removeAll(cards);
                                    cardsPane.getChildren().removeAll(cardsPane.getChildren());
                                    drawRectangles();
                                    for (int i = 0; i < cards.size(); i++) {
                                        int finalI = i;
                                        cards.get(i).setOnMouseClicked((MouseEvent event) -> {
                                            if (client.ifMyMove())
                                                client.chooseCard(finalI);
                                            else alert("Nie twój ruch!");
                                        });
                                    }
                                }
                            });
                            System.out.println("Nowa wartość:" + t1);
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
