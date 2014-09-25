/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader;

import java.awt.Point;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;
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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import spacetrader.cosmos.Universe;
import spacetrader.cosmos.player.Player;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.SunType;
import spacetrader.cosmos.SparseSpace;

/**
 * FXML Controller class
 *
 * @author Aaron McAnally
 */
public class GameController implements Initializable {

    private static final int GENERATION_BUFFER = 10;
    
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
    
    private boolean dragging = false, dragFinished = true;
    
    @FXML
    private Canvas gameCanvas;
    
    @FXML
    private Slider zoomSlider;
    
    private HashMap<Point, SolarSystem> solarSystemLocations;
    
    private SolarSystem selectedSolarSystem;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        solarSystemLocations = new HashMap<>();
        
        //adds listener for the zoom slider
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom = (float)zoomSlider.getValue() + 1;
                dragFinished = true;
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
    }
    
    private SolarSystem findClosestSolarSystem(Point p) {
        SolarSystem forreturn = solarSystemLocations.get(p);
        if(forreturn == null) {
            Point[] points = new Point[solarSystemLocations.keySet().size()];
            solarSystemLocations.keySet().toArray(points);
            Point closest = points[0];
            for(Point test : points) {
                if(closest.distance(p.x, p.y) > test.distance(p.x, p.y)) {
                    closest = test;
                }
            }
            if(closest.distance(p.x, p.y) < 5) {
                forreturn = solarSystemLocations.get(closest);
            }
        }
        return forreturn;
    }
    
    @FXML
    private void mouseMove(MouseEvent event) {
        SolarSystem mouseOver = findClosestSolarSystem(new Point((int) event.getX() - 5, (int) event.getY() - 5));
        if(mouseOver != null) {
            drawUniverse();
            //Point p = getSolarSystemCoords(mouseOver);
            g.setStroke(Color.WHITE);
            g.strokeOval(event.getX() - 15, event.getY() - 15, 30, 30);
        } else {
            drawUniverse();
        }
    }
    
    /**
     * update drag offsets when dragging
     * @param event info about the mouse
     */
    @FXML
    private void mouseDrag(MouseEvent event) {
        dragging = true;
        //System.out.println("preDragOffset: " + preDragX + ", " + preDragY);
        double tempX = event.getX();
        double tempY = event.getY();
        //System.out.println("tempX: " + tempX + ", " + tempY);
        dragOffsetX = (preDragX - tempX)/zoom;
        dragOffsetY = (preDragY - tempY)/zoom;
        //System.out.println("Drag Offset: " + dragOffsetX + ", " + dragOffsetY);
        drawUniverse();
    }
    
    private Point getSolarSystemCoords(SolarSystem s) {
        for (Point p : solarSystemLocations.keySet()) {
            if(solarSystemLocations.get(p).equals(s)) {
                return p;
            }
        }
        return null;
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
            dragFinished = true;
            
            drawUniverse();
        }
    }
    
    /**
     * helper method that checks if we clicked on a solar system
     * @param event contains info about where the mouse was clicked
     */
    private void checkClick(MouseEvent event) {
        SolarSystem mouseOver = findClosestSolarSystem(new Point((int) event.getX() - 5, (int) event.getY() - 5));
        if(mouseOver != null) {
            selectedSolarSystem = mouseOver;
            drawUniverse();
        }
    }
    
    /**
     * Helper method that draws universe based on map offset and scale
     */
    private void drawUniverse() {
        
        Point lower = getLower();
        Point upper = getUpper();

        universe.generateFrom(lower.x, lower.y, upper.x, upper.y );
        
        int tempSelectedX = 0, tempSelectedY = 0;
        //sets scale of star backdrop
        starBackdrop.setScaleX(zoom/5);
        starBackdrop.setScaleY(zoom/5);
        //makes sure that solar systems are being drawn depending on it being dragged
        //clears before drawing
        g.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        if(!dragging) {
            starBackdrop.setTranslateX(-mapOffsetX * 2);
            starBackdrop.setTranslateY(-mapOffsetY * 2);
            //int x = universe.xMin(), y = universe.yMin();
            for (SparseSpace.SparseIterator iter = universe.iterateFrom(lower.x, lower.y, upper.x, upper.y); iter.hasNext();) {
                SolarSystem a = iter.next();
                int x = iter.getX();
                int y = iter.getY();
                if(a != null) {
                    switch(a.SunType()) {
                        case BINARY:
                            g.setFill(Color.BLUE);
                            break;
                        case BLACK_HOLE:
                            g.setFill(Color.VIOLET);
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
                    if(dragFinished) {
                        solarSystemLocations.put(new Point((int) (((x - mapOffsetX) * zoom) + (512 - mapOffsetX)), (int) (((y - mapOffsetY) * zoom) + (288 - mapOffsetY))), a);
                    }
                    g.fillOval(((x - mapOffsetX) * zoom) + (512 - mapOffsetX), ((y - mapOffsetY) * zoom) + (288 - mapOffsetY), (zoom / 2) + a.Planets().length, (zoom / 2) + a.Planets().length);
                    g.setFill(Color.WHITE);
                    if(a.equals(selectedSolarSystem)) {
                        tempSelectedX = (int) (((x - mapOffsetX) * zoom) + (512 - mapOffsetX));
                        tempSelectedY = (int) (((y - mapOffsetY) * zoom) + (288 - mapOffsetY));
                    }
                }
            }
            dragFinished = false;
        } else {
            starBackdrop.setTranslateX((- dragOffsetX - mapOffsetX) * 2);
            starBackdrop.setTranslateY((- dragOffsetY - mapOffsetY) * 2);
            for (SparseSpace.SparseIterator iter = universe.iterateFrom(lower.x, lower.y, upper.x, upper.y); iter.hasNext();) {
                SolarSystem a = iter.next();
                int x = iter.getX();
                int y = iter.getY();
                if(a != null) {
                    switch(a.SunType()) {
                        case BINARY:
                            g.setFill(Color.BLUE);
                            break;
                        case BLACK_HOLE:
                            g.setFill(Color.VIOLET);
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
                    g.fillOval(((x - dragOffsetX - mapOffsetX) * zoom) + (512 - mapOffsetX - dragOffsetX), ((y - dragOffsetY - mapOffsetY) * zoom) + (288 - mapOffsetY - dragOffsetY), (zoom / 2) + a.Planets().length, (zoom / 2) + a.Planets().length);
                    g.setFill(Color.WHITE);
                    if(a.equals(selectedSolarSystem)) {
                        tempSelectedX = (int) (((x - dragOffsetX - mapOffsetX) * zoom) + (512 - dragOffsetX - mapOffsetX));
                        tempSelectedY = (int) (((y - dragOffsetY - mapOffsetY) * zoom) + (288 - dragOffsetY - mapOffsetY));
                    }
                }
            }
        }
        if(selectedSolarSystem != null) {
            g.setFill(Color.BLACK);
            g.fillRect(tempSelectedX + 4, tempSelectedY - 10, longestLine(selectedSolarSystem.toString()) * 8, selectedSolarSystem.toString().split("\n").length * 15);
            g.setFill(Color.WHITE);
            g.fillText(selectedSolarSystem.toString(), tempSelectedX + 5, tempSelectedY);
        }
    }
    
    private int longestLine(String s) {
        String[] strings = s.split("\n");
        int longest = strings[0].length();
        for (int i = 0; i < strings.length; i++) {
            if(strings[i].length() > longest) {
                longest = strings[i].length();
            }
        }
        return longest;
    }
    
    private Point getLower() {
        int lowerX;
        int lowerY;
        if(!dragging) {//using Kartik's method
            lowerY = (int)((mapOffsetY - gameCanvas.getHeight()/2))/(int)zoom + (int)mapOffsetY;
            lowerX = (int)((mapOffsetX - gameCanvas.getWidth()/2))/(int)zoom + (int)mapOffsetX;
        } else {
            lowerY = (int)((mapOffsetY + dragOffsetX - gameCanvas.getHeight()/2))/(int)zoom + (int)mapOffsetY + (int)dragOffsetY;
            lowerX = (int)((mapOffsetX + dragOffsetX - gameCanvas.getWidth()/2))/(int)zoom + (int)mapOffsetX + (int)dragOffsetX;
        }

        return new Point(lowerX, lowerY);
    }
    
    private Point getUpper() {
        int upperX;
        int upperY;
        if(!dragging) {//using Kartik's method
            upperY = (int)((mapOffsetY + gameCanvas.getHeight()/2))/(int)zoom + (int)mapOffsetY;
            upperX = (int)((mapOffsetX + gameCanvas.getWidth()/2))/(int)zoom + (int)mapOffsetX;
        } else {
            upperY = (int)((mapOffsetY + dragOffsetX + gameCanvas.getHeight()/2))/(int)zoom + (int)mapOffsetY + (int)dragOffsetY;
            upperX = (int)((mapOffsetX + dragOffsetX + gameCanvas.getWidth()/2))/(int)zoom + (int)mapOffsetX + (int)dragOffsetX;
        }
        
        return new Point(upperX, upperY);
    }
}
