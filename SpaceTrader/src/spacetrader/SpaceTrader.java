/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import spacetrader.turns.TurnEvent;
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
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.Planet;

import java.util.Scanner;
import java.util.Random;

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
            this.universe = new Universe(100, 0.1f);
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
        //ExampleUniverseAPI();
        Universe universe = new Universe();
        TurnEvent.RegisterListener(universe);
        for(int i = 0; i < 200; i++) {
            TurnEvent.NextTurn();
        }
        
        launch(args);
    }
    
    public Player getPlayer() {
        return newPlayer;
    }
    
    
    /**
     * Prettyish command-prompt interface with the universe to demonstrate how simple it is
     * to interface with the API (just toss it into main to run it, also, it won't exit, you'll need to
     * kill the program, didn't put to much time into exit functionality :) )
     */
    public static void ExampleUniverseAPI() {
        Universe universe = new Universe(100, 0.4f);
        System.out.println(universe.canGenerateAround(130, 50, 10));
        //universe.generateAround(130, 50, 10);
        //System.out.println(universe.canGenerateAround(130, 50, 10));
        int count = 0;
        for(SolarSystem a : universe) {
            count++;
            if(a == null)
                System.out.print("");
            else
                System.out.print("(" + a + ",'" + a.Name() + "')");
            if(count % 101 == 0)
                System.out.println();
        }
        
        Scanner in = new Scanner(System.in);
        String finalCommand = "";
        
        do {
            System.out.println("\nWhat solar system would you like to query?");
            int x = in.nextInt();
            int y = in.nextInt();

            SolarSystem system = universe.getSolarSystem(x, y);
            
            if(system != null) {
                System.out.println("System Information");        
                System.out.println("--------------------------------------");        
                System.out.println("X: " + x + ", Y: " + y);        
                System.out.println("Name: " + system.Name());            
                System.out.println("Government: " + system.Government());            
                System.out.println("Sun Type: " + system.SunType());            
                System.out.println("Technology Era: " + system.TechLevel());
                System.out.println("Wealth: " + system.Wealth());                
                System.out.println("Planets: " + system.Planets().length);


                System.out.println("Planetary Information:");  

                Planet[] planets = system.Planets();
                for(Planet planet : planets) {
                    System.out.println("Name: " + planet.Name());                
                    System.out.println("\tResource: " + planet.Resources());
                    System.out.println("\tWealth: " + planet.Wealth());
                }
            }
            
            System.out.println("Query again?");  
            finalCommand = in.nextLine();
        } while(finalCommand != "stop");
    }
    
}
