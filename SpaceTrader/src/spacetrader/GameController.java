/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Aaron McAnally
 */
public class GameController implements Initializable {

    @FXML
    private Label label;
    
    private Player player;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player = SpaceTrader.getInstance().getPlayer();
        label.setText(player.getName() + "\n"
                + "Pilot Skill: " + player.getPilotSkill() + "\n"
                + "Fighter Skill: " + player.getFighterSkill() + "\n"
                + "Trader Skill: " + player.getFighterSkill() + "\n"
                + "Engineer Skill: " + player.getFighterSkill() + "\n"
                + "Investor Skill: " + player.getFighterSkill());
    }    
    
}
