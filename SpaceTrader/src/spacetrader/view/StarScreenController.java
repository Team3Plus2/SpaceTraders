/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.view;

import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import spacetrader.cosmos.SparseSpace;
import spacetrader.cosmos.Universe;
import spacetrader.player.Player;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.SunType;
import spacetrader.main.SpaceTrader;

/**
 * FXML Controller class
 *
 * @author Aaron McAnally
 */
public class StarScreenController implements Initializable {

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
    
    private HashMap<String, Image> sunImages;
    
    @FXML
    private Canvas gameCanvas;
    
    @FXML
    private Slider zoomSlider;
    
    private HashMap<Point, SolarSystem> solarSystemLocations;
    
    private SolarSystem selectedSolarSystem;
    
    @FXML
    private Button goToPlanet;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        solarSystemLocations = new HashMap<>();
        sunImages = new HashMap<>();
        
        for(SunType star : (ArrayList<SunType>)SunType.getList(SunType.class)) {
            if(star.usesImage())
                sunImages.put(star.getName(), new Image(star.getImage()));
        }
        
        //adds listener for the zoom slider
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom = (float)zoomSlider.getValue();
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
    
    @FXML
    private void goToSolarSystem() {
        player.setCurrentSolarSystem(selectedSolarSystem);
        SpaceTrader.getInstance().goToSolarSystemView();
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
            if(closest.distance(p.x, p.y) < 10) {
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
            g.fillText(mouseOver.Name(), event.getX(), event.getY());
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
        dragOffsetX = (preDragX - tempX)/10;
        dragOffsetY = (preDragY - tempY)/10;
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
            dragOffsetX = 0;
            dragOffsetY = 0;
            dragging = false;
            dragFinished = true;
            solarSystemLocations.clear();
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
            goToPlanet.setVisible(true);
            goToPlanet.setText(mouseOver.Name());
        } else {
            selectedSolarSystem = null;
            drawUniverse();
            goToPlanet.setVisible(false);
        }
    }
    
    /**
     * Helper method that draws universe based on map offset and scale
     */
    private void drawUniverse() {
        int universeScale = 2;
        Point lower = getLower();
        Point upper = getUpper();
        lower = new Point(lower.x / (universeScale), lower.y / (universeScale));
        upper = new Point(upper.x / (universeScale), upper.y / (universeScale));

        universe.generateFrom(lower.x, lower.y, upper.x, upper.y );
        
        int tempSelectedX = 0, tempSelectedY = 0;
        
        //sets scale of star backdrop
        starBackdrop.setScaleX(zoom/5);
        starBackdrop.setScaleY(zoom/5);
        gameCanvas.setScaleX(zoom/10);
        gameCanvas.setScaleY(zoom/10);
        
        //makes sure that solar systems are being drawn depending on it being dragged
        //clears before drawing
        g.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        
        //int x = universe.xMin(), y = universe.yMin();
        starBackdrop.setTranslateX((-mapOffsetX - dragOffsetX)*2);
        starBackdrop.setTranslateY((-mapOffsetY - dragOffsetY)*2);
        
        //draw solar systems
        for (SparseSpace.SparseIterator iter = universe.iterateFrom(lower.x, lower.y, upper.x, upper.y); iter.hasNext();) {
            SolarSystem a = iter.next();
            Image starImage = null;
            if(a != null) {
                int x = a.getX() * universeScale;
                int y = a.getY() * universeScale;
                Random r = new Random();
                r.setSeed(a.Name().hashCode());
                
                //set color of star based on suntype
                if(a.SunType().usesColor()) {
                    g.setFill(a.SunType().getColor());
                } else {
                    starImage = sunImages.get(a.SunType().getName());
                }
                
                if(!dragging) {
                    double size = 20 + a.Planets().length * 2;
                    double xPosition = ((x - mapOffsetX) * 10) + (512 - mapOffsetX) - (size / 2);
                    double yPosition = ((y - mapOffsetY) * 10) + (288 - mapOffsetY) - (size / 2);
                    if(dragFinished) {
                        solarSystemLocations.put(new Point((int) (xPosition + (size/2)), (int) (yPosition + (size/2))), a);
                    }
                    if(starImage == null) {
                        g.fillOval(((x - mapOffsetX) * 10) + (512 - mapOffsetX), ((y - mapOffsetY) * 10) + (288 - mapOffsetY), 5 + a.Planets().length * 2, 5 + a.Planets().length * 2);
                    } else {
                        Affine old = g.getTransform();
                        double angle = r.nextDouble() * 360;
                        rotate(angle, xPosition + size/2, yPosition + size/2);
                        g.drawImage(starImage, xPosition, yPosition, size, size);
                        g.setTransform(old);
                    }
                    g.setFill(Color.WHITE);
                    if(a.equals(selectedSolarSystem)) {
                        tempSelectedX = (int) (((x - mapOffsetX) * 10) + (512 - mapOffsetX));
                        tempSelectedY = (int) (((y - mapOffsetY) * 10) + (288 - mapOffsetY));
                        goToPlanet.setTranslateX((int) (((x - mapOffsetX) * 10) + (512 - mapOffsetX)));
                        goToPlanet.setTranslateY((int) (((y - mapOffsetY) * 10) + (288 - mapOffsetY)) + 10);
                    }
                } else {
                    if(starImage == null) {
                        g.fillOval(((x - mapOffsetX - dragOffsetX) * 10) + (512 - dragOffsetX - mapOffsetX), ((y - mapOffsetY - dragOffsetY) * 10) + (288 - dragOffsetY - mapOffsetY), 5 + a.Planets().length * 2, 5 + a.Planets().length * 2);
                    } else {
                        Affine old = g.getTransform();
                        double angle = r.nextDouble() * 360;
                        double size = 20 + a.Planets().length * 2;
                        double xPosition = ((x - mapOffsetX - dragOffsetX) * 10) + (512 - mapOffsetX - dragOffsetX) - (size/2);
                        double yPosition = ((y - mapOffsetY - dragOffsetY) * 10) + (288 - mapOffsetY - dragOffsetY) - (size/2);
                        rotate(angle, xPosition + size/2, yPosition + size/2);
                        g.drawImage(starImage, xPosition, yPosition, size, size);
                        g.setTransform(old);
                    }
                    g.setFill(Color.WHITE);
                    if(a.equals(selectedSolarSystem)) {
                        tempSelectedX = (int) (((x - dragOffsetX - mapOffsetX) * 10) + (512 - dragOffsetX - mapOffsetX));
                        tempSelectedY = (int) (((y - dragOffsetY - mapOffsetY) * 10) + (288 - dragOffsetY - mapOffsetY));
                        goToPlanet.setTranslateX((int) (((x - dragOffsetX - mapOffsetX) * 10) + (512 - dragOffsetX - mapOffsetX)));
                        goToPlanet.setTranslateY((int) (((y - dragOffsetY - mapOffsetY) * 10) + (288 - dragOffsetY - mapOffsetY)) + 10);
                    }
                }
            }
        }
//        if (dragging) {
//            g.strokeOval((player.getCurrentSolarSystem().getX() * universeScale) - dragOffsetX - mapOffsetX, (player.getCurrentSolarSystem().getY() * universeScale) - dragOffsetY - mapOffsetY, 10, 10);
//        } else {
//            g.strokeOval(player.getCurrentSolarSystem().getX() * universeScale - mapOffsetX, (player.getCurrentSolarSystem().getY() * universeScale) - dragOffsetY - mapOffsetY, 10, 10);
//        }
        if(selectedSolarSystem != null) {
            g.setFill(Color.BLACK);
            g.fillRect(tempSelectedX + 4, tempSelectedY - 10, longestLine(selectedSolarSystem.toString()) * 8, selectedSolarSystem.toString().split("\n").length * 15);
            g.setFill(Color.WHITE);
            g.fillText(selectedSolarSystem.toString(), tempSelectedX + 5, tempSelectedY);
        }
    }
    
    private void rotate(double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
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
            lowerY = (int)(((mapOffsetY - gameCanvas.getHeight()/2))/10 + mapOffsetY);
            lowerX = (int)(((mapOffsetX - gameCanvas.getWidth()/2))/10 + mapOffsetX);
        } else {
            lowerY = (int)(((mapOffsetY + dragOffsetY - gameCanvas.getHeight()/2))/10 + mapOffsetY + dragOffsetY);
            lowerX = (int)(((mapOffsetX + dragOffsetX - gameCanvas.getWidth()/2))/10 + mapOffsetX + dragOffsetX);
        }

        return new Point(lowerX, lowerY);
    }
    
    private Point getUpper() {
        int upperX;
        int upperY;
        if(!dragging) {//using Kartik's method
            upperY = (int)(((mapOffsetY + gameCanvas.getHeight()/2))/10 + mapOffsetY);
            upperX = (int)(((mapOffsetX + gameCanvas.getWidth()/2))/10 + mapOffsetX);
        } else {
            upperY = (int)(((mapOffsetY + dragOffsetY + gameCanvas.getHeight()/2))/10 + mapOffsetY + dragOffsetY);
            upperX = (int)(((mapOffsetX + dragOffsetX + gameCanvas.getWidth()/2))/10 + mapOffsetX + dragOffsetX);
        }
        
        return new Point(upperX, upperY);
    }
}
