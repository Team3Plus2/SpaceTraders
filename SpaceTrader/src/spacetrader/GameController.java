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
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import spacetrader.cosmos.Universe;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.SunType;

/**
 * FXML Controller class
 *
 * @author Aaron McAnally
 */
public class GameController implements Initializable {

    @FXML
    private Label label;
    
    @FXML
    private Pane starBackdrop;
    
    private Player player;
    
    private Universe universe;
    
    private GraphicsContext g;
    
    private double zoom = 10;
    
    private double dragOffsetX = 0, dragOffsetY = 0, preDragX = 0, preDragY = 0;
    
    private double mapOffsetX = 0, mapOffsetY = 0;
    
    private boolean dragging = false;
    
    @FXML
    private Canvas gameCanvas;
    
    @FXML
    private Slider zoomSlider;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //adds listener for the zoom slider
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom = (float)zoomSlider.getValue() + 1;
                drawUniverse();
            }
        
        });
        // gets player and prints label
        player = SpaceTrader.getInstance().getPlayer();
        label.setText(player.getName() + "\n"
                + "Pilot Skill: " + player.getPilotSkill() + "\n"
                + "Fighter Skill: " + player.getFighterSkill() + "\n"
                + "Trader Skill: " + player.getTraderSkill() + "\n"
                + "Engineer Skill: " + player.getEngineerSkill() + "\n"
                + "Investor Skill: " + player.getInvestorSkill());
        universe = SpaceTrader.getInstance().getUniverse();
        // gets graphic object and draws universe to begin
        g = gameCanvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        drawUniverse();
    }
    
    /**
     * 
     * @param event 
     */
    @FXML
    private void mouseClicked(MouseEvent event) {
        checkClick(event);
    }
    
    /**
     * 
     * @param event 
     */
    @FXML
    private void mouseDown(MouseEvent event) {
        preDragX = event.getX();
        preDragY = event.getY();
        checkClick(event);
    }
    
    /**
     * update drag offsets when dragging
     * @param event info about the mouse
     */
    @FXML
    private void mouseDrag(MouseEvent event) {
        dragging = true;
        System.out.println("preDragOffset: " + preDragX + ", " + preDragY);
        double tempX = event.getX();
        double tempY = event.getY();
        System.out.println("tempX: " + tempX + ", " + tempY);
        dragOffsetX = (preDragX - tempX)/zoom;
        dragOffsetY = (preDragY - tempY)/zoom;
        System.out.println("Drag Offset: " + dragOffsetX + ", " + dragOffsetY);
        drawUniverse();
    }
    
    /**
     * when mouse is released, update mapoffset
     * @param event contains info about the mouse
     */
    @FXML
    private void mouseReleased(MouseEvent event) {
        if (dragging) {
            mapOffsetX += dragOffsetX;
            mapOffsetY += dragOffsetY;
            dragging = false;
            drawUniverse();
        }
        if(mapOffsetX > 100) {
            
        }
        checkClick(event);
    }
    
    /**
     * helper method that checks if we clicked on a solar system
     * @param event contains info about where the mouse was clicked
     */
    private void checkClick(MouseEvent event) {
        SolarSystem clicked = universe.getSolarSystem((int)(((event.getX())/zoom) + mapOffsetX - 50), (int)(((event.getY())/zoom) + mapOffsetY - 50));
        System.out.println((int)(((event.getX())/zoom) + mapOffsetX - 50) + ", " + (int)(((event.getY())/zoom) + mapOffsetY - 50) + " " + clicked);
        if(clicked != null) {
            drawUniverse();
            g.fillText(clicked.Name() + ": " + (int)(((event.getX())/zoom) + mapOffsetX - 50) + ", " + (int)(((event.getY())/zoom) + mapOffsetY - 50), event.getX(), event.getY());
        }
    }
    
    /**
     * Helper method that draws universe based on map offset and scale
     */
    private void drawUniverse() {
        //sets scale of star backdrop
        starBackdrop.setScaleX(zoom/5);
        starBackdrop.setScaleY(zoom/5);
        //makes sure that solar systems are being drawn depending on it being dragged
        //clears before drawing
        g.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        if(!dragging) {
            starBackdrop.setTranslateX(-mapOffsetX * 2);
            starBackdrop.setTranslateY(-mapOffsetY * 2);
            int x = -50, y = -50;
            for (SolarSystem a : universe) {
                if(a != null) {
                    switch(a.SunType()) {
                        case BINARY:
                            g.setFill(Color.BLUE);
                            break;
                        case BLACK_HOLE:
                            g.setFill(Color.BLACK);
                            break;
                        case PROTO:
                            g.setFill(Color.PINK);
                            break;
                        case RED_GIANT:
                            g.setFill(Color.RED);
                            break;
                        case SOL:
                            g.setFill(Color.YELLOW);
                            break;
                        case WHITE_DWARF:
                            g.setFill(Color.WHITE);
                            break;
                    }
                    g.fillOval(((x - mapOffsetX) * zoom) + 512, ((y - mapOffsetY) * zoom) + 288, zoom, zoom);
                    g.setFill(Color.WHITE);
                                    
                }
                if (x < 50) {
                    x++;
                } else {
                    x = -50;
                    y++;
                }
            }
        } else {
            starBackdrop.setTranslateX((- dragOffsetX - mapOffsetX) * 2);
            starBackdrop.setTranslateY((- dragOffsetY - mapOffsetY) * 2);
            int x = -50, y = -50;
            for (SolarSystem a : universe) {
                if(a != null) {
                    switch(a.SunType()) {
                        case BINARY:
                            g.setFill(Color.BLUE);
                            break;
                        case BLACK_HOLE:
                            g.setFill(Color.BLACK);
                            break;
                        case PROTO:
                            g.setFill(Color.PINK);
                            break;
                        case RED_GIANT:
                            g.setFill(Color.RED);
                            break;
                        case SOL:
                            g.setFill(Color.YELLOW);
                            break;
                        case WHITE_DWARF:
                            g.setFill(Color.WHITE);
                            break;
                    }
                    g.fillOval(((x - dragOffsetX - mapOffsetX) * zoom) + 512, ((y - dragOffsetY - mapOffsetY) * zoom) + 288, zoom, zoom);
                    g.setFill(Color.WHITE);
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
    
    
}
