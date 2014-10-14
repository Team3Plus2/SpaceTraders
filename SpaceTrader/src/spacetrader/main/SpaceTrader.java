/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader.main;

import spacetrader.economy.TradeGood;
import spacetrader.encounter.Encounter;
import spacetrader.economy.MarketPlace;
import spacetrader.player.Player;
import spacetrader.xml.XMLReader;
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
import spacetrader.cosmos.system.Resource;
import spacetrader.xml.DummyXMLObject;
import spacetrader.xml.ObjectLoader;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;
import spacetrader.cosmos.system.TechLevel;
import spacetrader.player.Ship;
import spacetrader.player.ShipType;
import spacetrader.player.Shield;
import spacetrader.player.Weapon;
import spacetrader.player.Gadget;
import spacetrader.view.SolarSystemViewController;
import spacetrader.view.StarScreenController;
import spacetrader.xml.LoadedType;

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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToSolarSystemView() {
        try {
            loadNewScreen("/spacetrader/view/SolarSystemView.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToWelcomeScreen() {
        try {
            loadNewScreen("/spacetrader/view/WelcomeScreen.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToGame(Player newPlayer) {
        try {
            this.newPlayer = newPlayer;
            this.universe = new Universe(100, 0.1f);
            this.newPlayer.setCurrentSolarSystem(this.universe.getClosestSolarSystem(0, 0, 20));
            System.out.println(this.newPlayer.getCurrentSolarSystem());
            loadNewScreen("/spacetrader/view/StarScreen.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public void goToGame() {
        try {
            loadNewScreen("/spacetrader/view/StarScreen.fxml");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadNewScreen(String FXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML));
        Parent root = (Parent)loader.load();
        stackPane.getChildren().add(root);
        if(FXML.equals("/spacetrader/view/SolarSystemView.fxml")) {
            SolarSystemViewController controller = (SolarSystemViewController)loader.getController();
            controller.setScene(stage.getScene());
        } else if(FXML.equals("/spacetrader/view/StarScreen.fxml")) {
            StarScreenController controller = (StarScreenController)loader.getController();
            controller.setScene(stage.getScene());
        }
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
        ObjectLoader.LoadAllObjects();
        
        //commandline encounter!
        /*Player dummy = new Player("lol", 4,4,4,4,4);
        dummy.setShip(new Ship((ShipType)ShipType.get("TERMITE")));
        dummy.getShip().addShield(Shield.Random());
        dummy.getShip().addShield(Shield.Random());
        dummy.getShip().addWeapon(Weapon.Random());
        dummy.getShip().addWeapon(Weapon.Random());
        dummy.getShip().addWeapon(Weapon.Random());
        dummy.getShip().addGadget(Gadget.Random());
        dummy.getShip().addGadget(Gadget.Random());
        TradeGood illegals = new TradeGood((TradeGood)TradeGood.get("narcotics"));
        TradeGood legals = new TradeGood((TradeGood)TradeGood.get("water"));
        illegals.setAmount(10);
        legals.setAmount(8);
        dummy.addTradeGood(illegals);
        dummy.addTradeGood(legals);

        Encounter other = new Encounter(null, dummy);
        System.out.println(other.getGreeting());
        if(other.willAttack()) {
            System.out.println("blah blah blah die!");

        } else if(other.willRequestTrade()) {
            System.out.println("Lol wanna trade?");
        } else if(other.willRequestSearch()) {
            System.out.println("KImma serch u now for bad stuff k?");
            if(other.search(dummy)) {
                System.out.println("Lol, toke ur bad stf, now Imma get hih wit it, :p");
            } else {
                System.out.println("k, u don haf bad stf, so I cannt gt hi, scrw u!");
            }
        } else {
            Scanner in = new Scanner(System.in);
            String reaction = in.nextLine();
            if(reaction.equals("a")) {
                int result = 0;
                while(result == 0) {
                    result = other.roundOfCombat(dummy, null);
                    if(result == -2) {
                        reaction = in.nextLine();
                        if(reaction.equals("c")) {
                            result = 0;
                        }
                    }
                }

                if(result == 1) {
                    System.out.println("ur ded");
                } else if(result == -1) {
                    System.out.println("noooo!!!...bleghghsfdadf");
                } else if(result == -2) {
                    System.out.println("okay... you win");
                }
            } else if(reaction.equals("t")) {
                System.out.println("Other:");
                for(TradeGood good : other.getGoods()) {
                    System.out.println("\t" + good.getName() + " : " + good.getAmount() + " : " + good.getCurrentPriceEach());
                }
            }
        }*/
        
        launch(args);
    }
    
    public Player getPlayer() {
        return newPlayer;
    }
    
    /**
     * Simple example call of xml reader to help demonstrate how to interface with the xml reader.
     * See DummyXMLObject.java and objects/test.xml(look in the files tab) for the complete view of
     * how to construct objects and xml files that will interface and be parsed by the XMLReader class.
     */
    public static void ExampleXMLReaderAPI() {
        XMLReader reader = new XMLReader(DummyXMLObject.class, "objects/test.xml");
        ArrayList<DummyXMLObject> objects = reader.read();
        
        for(DummyXMLObject obj : objects) {
            obj.print();
        }
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
    
    public void setPlayer(Player p) {
        this.newPlayer = p;
    }
    
    public void setUniverse(Universe u) {
        this.universe = u;
    }
    
}
