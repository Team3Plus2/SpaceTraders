/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.view;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import spacetrader.cosmos.Universe;
import spacetrader.cosmos.system.Planet;
import spacetrader.main.SpaceTrader;
import spacetrader.player.Player;
import spacetrader.save.LoadGame;
import spacetrader.save.SaveGame;

/**
 * FXML Controller class
 *
 * @author KartikKini
 */
public class NewLoadGameController implements Initializable {
    
    @FXML
    ListView<Player> loadOptions;
    
    @FXML
    Label loadInfo;
    
    @FXML
    Button playButton;
    
    private boolean newPlayer = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Player> options = FXCollections.observableArrayList(new Player[] {new Player("New", 0, 0, 0, 0, 0)});
        
        //SaveGame.save("happy.sav", new Player("happy", 0, 0, 0, 10, 10), new Universe());
        
        File listOfFiles = new File("../SpaceTrader/");
        for(File f : listOfFiles.listFiles()) {
            if(f.getPath().contains(".sav")) {
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
    
    private void selectPlayer() {
        Player p = loadOptions.getSelectionModel().getSelectedItem();
        if(p != null) {
            if(p.getName().equals("New") &&
                    p.getEngineerSkill() + p.getFighterSkill()
                    + p.getTraderSkill() + p.getInvestorSkill()
                    + p.getPilotSkill() == 0) {
                loadInfo.setText("Click \'Play\' to start a new game!");
                newPlayer = true;
            } else {
                loadInfo.setText(p.getName() + ": $" + p.getMoney() + "\n\nPilot: " + p.getPilotSkill() + 
                        "\nFighter: " + p.getFighterSkill() +
                        "\nTrader: " + p.getTraderSkill() +
                        "\nEngineer: " + p.getEngineerSkill() +
                        "\n\nShip: " + p.getShip().toString() +
                        "\n\nAt: " + p.getCurrentSolarSystem().Name());
                newPlayer = false;
            }
        }
    }
    
    @FXML
    private void goToNewPlayer() {
        if(newPlayer) {
            SpaceTrader.getInstance().goToCharacterConfig();
        } else {
            Player p = loadOptions.getSelectionModel().getSelectedItem();
            if(p != null) {
                if(LoadGame.load(p.getName() + ".sav", SpaceTrader.getInstance())) {
                    SpaceTrader.getInstance().goToGame();
                }
            }
        }
    }
    
}