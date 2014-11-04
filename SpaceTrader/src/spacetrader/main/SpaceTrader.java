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
    
    /**
     * The current stage of the game.
     */
    private Stage stage;
    
    /**
     * The current player in the game.
     */
    private Player newPlayer;
    
    /**
     * The current universe of the game.
     */
    private Universe universe;

    /**
     * Location of the solar system view FXML file.
     */
    private static final String SOLAR_SYSTEM_VIEW_FXML_LOCATION = "/spacetrader/view/SolarSystemView.fxml";
    
    /**
     * Location of the star screen FXML file.
     */
    private static final String STAR_SCREEN_FXML_LOCATION = "/spacetrader/view/StarScreen.fxml";
    
    /**
     * @return the universe
     */
    public Universe getUniverse() {
        return universe;
    }
    
    /**
     * This instance of SpaceTrader.
     */
    private static SpaceTrader instance;
    
    /**
     * The StackPane object used for views.
     */
    StackPane stackPane = new StackPane();
    
    /**
     * instantiates the SpaceTrader.
     * 
     * "instance = this" is needed in order to implement singleton
     */
    public SpaceTrader() {
        instance = this;
        stage = new Stage();
    }
    
    /**
     * @return the instance of the game
     */
    public static SpaceTrader getInstance() {
        return instance;
    }
    
    @Override
    public void start(Stage start) {
        stage = start;
        stage.setTitle("Space Traders");
        Scene scene = new Scene(stackPane);
        Font.loadFont(getClass().getResource("/visuals/LVDCC.TTF").toExternalForm(), 10);
        scene.getStylesheets().add(getClass().getResource("/spacetrader/view/SpaceTraderStylesheet.css").toExternalForm());
        goToWelcomeScreen();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Goes to the character configuration screen.
     */
    public void goToCharacterConfig() {
        try {
            loadNewScreen("/spacetrader/view/CharacterConfigurationScreen.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Goes to the load game screen.
     */
    public void goToLoadGame() {
        try {
            loadNewScreen("/spacetrader/view/NewLoadGame.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Goes to the planet view screen.
     */
    public void goToPlanetView() {
        try {
            loadNewScreen("/spacetrader/view/PlanetView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Goes to the solar system view screen.
     */
    public void goToSolarSystemView() {
        try {
            loadNewScreen(SOLAR_SYSTEM_VIEW_FXML_LOCATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Goes to the welcome screen.
     */
    public void goToWelcomeScreen() {
        try {
            loadNewScreen("/spacetrader/view/WelcomeScreen.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Goes to the game screen.
     * 
     * @param newPlayer2 the player in the game
     */
    public void goToGame(Player newPlayer2) {
        this.newPlayer = newPlayer2;
        this.universe = new Universe(100, 0.1f);
        this.newPlayer.setCurrentSolarSystem(this.universe.getClosestSolarSystem(0, 0, 20));
        System.out.println(this.newPlayer.getCurrentSolarSystem());
        goToGame();
    }
    
    /**
     * Goes to the game screen.
     */
    public void goToGame() {
        try {
            loadNewScreen(STAR_SCREEN_FXML_LOCATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Goes to the encounter screen.
     */
    public void goToEncounter() {
        try {
            System.out.println("I'm about to go to encounter!");
            loadNewScreen("/spacetrader/view/Encounter.fxml");
        } catch (IOException e) {
            System.out.println("EHERJELRE");
            e.printStackTrace();
        }
    }
    
    /**
     * Used to load the new screen.
     * 
     * @param fXML the FXML file
     * @throws IOException 
     */
    private void loadNewScreen(String fXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fXML));
        Parent root = (Parent) loader.load();
        stackPane.getChildren().add(root);
        if (fXML.equals(SOLAR_SYSTEM_VIEW_FXML_LOCATION)) {
            SolarSystemViewController controller = (SolarSystemViewController) loader.getController();
            controller.setScene(stage.getScene());
        } else if (fXML.equals(STAR_SCREEN_FXML_LOCATION)) {
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
    
    /**
     * @return the player in the game
     */
    public Player getPlayer() {
        return newPlayer;
    }
    
    /**
     * @param p the new player in the game
     */
    public void setPlayer(Player p) {
        this.newPlayer = p;
    }
    
    /**
     * @param u sets the universe of the game
     */
    public void setUniverse(Universe u) {
        this.universe = u;
    }
    
}
