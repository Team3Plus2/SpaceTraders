/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import java.io.IOException;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author KartikKini
 */
public class SpaceTrader extends Application {
    
    private Stage stage;
    
    private Player newPlayer;
    
    private static SpaceTrader instance;
    
    StackPane stackPane = new StackPane();
    
    public SpaceTrader() {
        instance = this;
    }
    
    public static SpaceTrader getInstance() {
        return instance;
    }
    
    @Override
    public void start(Stage start) {
        try {
            stage = start;
            stage.setTitle("Space Traders");
            Scene scene = new Scene(stackPane);
            Font.loadFont(getClass().getResource("/visuals/LVDCC.TTF").toExternalForm(), 10);
            scene.getStylesheets().add(getClass().getResource("SpaceTraderStylesheet.css").toExternalForm());
            loadNewScreen("WelcomeScreen.fxml");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToCharacterConfig() {
        try {
            loadNewScreen("CharacterConfigurationScreen.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToWelcomeScreen() {
        try {
            loadNewScreen("WelcomeScreen.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToGame(Player newPlayer) {
        try {
            this.newPlayer = newPlayer;
            loadNewScreen("Game.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadNewScreen(String FXML) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(FXML));
        stackPane.getChildren().add(root);        
        EventHandler<ActionEvent> finished = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(stackPane.getChildren().size() > 1) {
                    stackPane.getChildren().remove(0);
                }
            }
        };
        
        final Timeline switchPage;
        if(stackPane.getChildren().size() > 1) {
            switchPage = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(stackPane.getChildren().get(1).opacityProperty(), 0.0), new KeyValue(stackPane.getChildren().get(0).opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(1), finished, new KeyValue(stackPane.getChildren().get(1).opacityProperty(), 1.0), new KeyValue(stackPane.getChildren().get(0).opacityProperty(), 0.0))
            );
        } else {
            switchPage = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(stackPane.getChildren().get(0).opacityProperty(), 0.0)),
                new KeyFrame(Duration.seconds(5), finished, new KeyValue(stackPane.getChildren().get(0).opacityProperty(), 1.0))
            );
        }
        
        switchPage.play();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Player getPlayer() {
        return newPlayer;
    }
    
}
