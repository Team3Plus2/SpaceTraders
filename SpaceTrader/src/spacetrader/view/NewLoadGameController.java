/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.view;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;
import spacetrader.save.LoadGame;

/**
 * FXML Controller class.
 *
 * @author KartikKini
 */
public class NewLoadGameController implements Initializable {
    
    /**
     * Load options for player.
     */
    @FXML
    ListView<Player> loadOptions;
    
    /**
     * Load info for player.
     */
    @FXML
    Label loadInfo;
    
    /**
     * Play button.
     */
    @FXML
    Button playButton;
    
    /**
     * The string "New".
     */
    private String newP = "New";
    
    /**
     * The string ".sav".
     */
    private String save = ".sav";
    
    /**
     * Checks for new player.
     */
    private boolean newPlayer = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Player> options = FXCollections.observableArrayList(new Player[] {new Player(newP, 0, 0, 0, 0, 0)});
        
        //SaveGame.save("happy.sav", new Player("happy", 0, 0, 0, 10, 10), new Universe());
        
        File listOfFiles = new File("../SpaceTrader/");
        for (File f : listOfFiles.listFiles()) {
            if (f.getPath().contains(save)) {
                options.add(LoadGame.loadPlayer(f.getAbsolutePath()));
            }
        }
        
        loadOptions.setItems(options);
        loadOptions.getSelectionModel().getSelectedItems().addListener(new ListChangeListener() {
            @Override
            public void onChanged(ListChangeListener.Change c) {
                selectPlayer();
            }
        });
    }
    
    /**
     * Loads selected player.
     */
    private void selectPlayer() {
        Player p = loadOptions.getSelectionModel().getSelectedItem();
        if (p != null) {
            if (p.getName().equals(newP) 
                    && p.getEngineerSkill() + p.getFighterSkill()
                    + p.getTraderSkill() + p.getInvestorSkill()
                    + p.getPilotSkill() == 0) {
                loadInfo.setText("Click \'Play\' to start a new game!");
                newPlayer = true;
            } else {
                loadInfo.setText(p.getName() + ": $" + p.getMoney() 
                        + "\n\nPilot: " + p.getPilotSkill()  
                        + "\nFighter: " + p.getFighterSkill() 
                        + "\nTrader: " + p.getTraderSkill() 
                        + "\nEngineer: " + p.getEngineerSkill() 
                        + "\n\nShip: " + p.getShip().toString() 
                        + "\n\nAt: " + p.getCurrentSolarSystem().Name());
                newPlayer = false;
            }
        }
    }
    
    /**
     * Initializes a new player.
     */
    @FXML
    private void goToNewPlayer() {
        if (newPlayer) {
            SpaceTrader.getInstance().goToCharacterConfig();
        } else {
            Player p = loadOptions.getSelectionModel().getSelectedItem();
            if (p != null) {
                if (LoadGame.load(p.getName() + save, SpaceTrader.getInstance())) {
                    SpaceTrader.getInstance().goToGame();
                }
            }
        }
    }
    
}
