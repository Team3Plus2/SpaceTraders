/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.main;

import spacetrader.player.Player;
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
import spacetrader.cosmos.Universe;
import spacetrader.xml.ObjectLoader;

import spacetrader.view.SolarSystemViewController;
import spacetrader.view.StarScreenController;

/**
 *
 * @author KartikKini
 */
public class SpaceTrader extends Application {
    
    private Stage stage;
    
    private Player newPlayer;
    
    private Universe universe;

    public Universe getUniverse() {
        return universe;
    }
    
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
            scene.getStylesheets().add(getClass().getResource("/spacetrader/view/SpaceTraderStylesheet.css").toExternalForm());
            loadNewScreen("/spacetrader/view/WelcomeScreen.fxml");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToCharacterConfig() {
        try {
            loadNewScreen("/spacetrader/view/CharacterConfigurationScreen.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToLoadGame() {
        try {
            loadNewScreen("/spacetrader/view/NewLoadGame.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToPlanetView() {
        try {
            loadNewScreen("/spacetrader/view/PlanetView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToSolarSystemView() {
        try {
            loadNewScreen("/spacetrader/view/SolarSystemView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToWelcomeScreen() {
        try {
            loadNewScreen("/spacetrader/view/WelcomeScreen.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToGame(Player newPlayer2) {
        try {
            this.newPlayer = newPlayer2;
            this.universe = new Universe(100, 0.1f);
            this.newPlayer.setCurrentSolarSystem(this.universe.getClosestSolarSystem(0, 0, 20));
            System.out.println(this.newPlayer.getCurrentSolarSystem());
            loadNewScreen("/spacetrader/view/StarScreen.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToGame() {
        try {
            loadNewScreen("/spacetrader/view/StarScreen.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToEncounter() {
        try {
            System.out.println("I'm about to go to encounter!");
            loadNewScreen("/spacetrader/view/Encounter.fxml");
        } catch (IOException e) {
            System.out.println("EHERJELRE");
            e.printStackTrace();
        }
    }
    
    private void loadNewScreen(String fXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fXML));
        Parent root = (Parent) loader.load();
        stackPane.getChildren().add(root);
        if (fXML.equals("/spacetrader/view/SolarSystemView.fxml")) {
            SolarSystemViewController controller = (SolarSystemViewController) loader.getController();
            controller.setScene(stage.getScene());
        } else if (fXML.equals("/spacetrader/view/StarScreen.fxml")) {
            StarScreenController controller = (StarScreenController) loader.getController();
            controller.setScene(stage.getScene());
        }
        EventHandler<ActionEvent> finished = (ActionEvent event) -> {
            if (stackPane.getChildren().size() > 1) {
                stackPane.getChildren().remove(0);
            }
        };
        
        final Timeline switchPage;
        if (stackPane.getChildren().size() > 1) {
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
        ObjectLoader.loadAllObjects();
        launch(args);
    }
    
    public Player getPlayer() {
        return newPlayer;
    }
    
    public void setPlayer(Player p) {
        this.newPlayer = p;
    }
    
    public void setUniverse(Universe u) {
        this.universe = u;
    }
    
}
