/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package spacetrader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("WelcomeScreen.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Space Traders");
        //scene.getStylesheets().add(getClass().getResource("WelcomeScreen.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Prettyish command-prompt interface with the universe to demonstrate how simple it is
     * to interface with the API (just toss it into main to run it, also, it won't exit, you'll need to
     * kill the program, didn't put to much time into exit functionality :) )
     */
    public static void ExampleUniverseAPI() {
        Universe universe = new Universe(50, 0.4f);
        int count = 0;
        for(SolarSystem a : universe) {
            count++;
            if(a == null)
                System.out.print("0");
            else
                System.out.print("(" + a + "," + a.x + "," + a.y +")");
            if(count % 50 == 0)
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
