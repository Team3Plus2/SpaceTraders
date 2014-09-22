/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import spacetrader.cosmos.Universe;
import spacetrader.cosmos.system.SolarSystem;

/**
 * FXML Controller class
 *
 * @author Aaron McAnally
 */
public class GameController implements Initializable {

    @FXML
    private Label label;
    
    private Player player;
    
    private Universe universe;
    
    private GraphicsContext g;
    
    private float zoom = 1f;
    
    @FXML
    private Canvas gameCanvas;
    
    @FXML
    private Slider zoomSlider;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom = (float)zoomSlider.getValue() + 1;
                drawUniverse();
            }
        
        });
        player = SpaceTrader.getInstance().getPlayer();
        label.setText(player.getName() + "\n"
                + "Pilot Skill: " + player.getPilotSkill() + "\n"
                + "Fighter Skill: " + player.getFighterSkill() + "\n"
                + "Trader Skill: " + player.getTraderSkill() + "\n"
                + "Engineer Skill: " + player.getEngineerSkill() + "\n"
                + "Investor Skill: " + player.getInvestorSkill());
        universe = SpaceTrader.getInstance().getUniverse();
        g = gameCanvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        drawUniverse();
    }
    
    @FXML
    private void mouseClicked(MouseEvent event) {
        SolarSystem clicked = universe.getSolarSystem((int)((event.getX()/10/zoom) - 51), (int)((event.getY()/10/zoom) - 51));
        if(clicked != null) {
            drawUniverse();
            //System.out.println((int)((event.getX()/10/zoom) - 51) + " " + (int)((event.getY()/10/zoom) - 51) + " " + clicked);
            g.fillText(clicked.Name() + ": " + (int)((event.getX()/10/zoom) - 51) + ", " + (int)((event.getY()/10/zoom) - 51), event.getX(), event.getY());
        }     
    }
    
    private void drawUniverse() {
        g.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        int x = -50, y = -50;
        for (SolarSystem a : universe) {
            if(a != null) {
                g.fillOval((x + 50)*10*zoom, (y + 50)*10*zoom, 5*(zoom), 5*(zoom));
            }
            if (x < 50) {
                x++;
            } else {
                x = -50;
                y++;
            }
        }
    }
    
    
}
