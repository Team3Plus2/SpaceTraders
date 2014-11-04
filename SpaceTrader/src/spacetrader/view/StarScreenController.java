package spacetrader.view;

import java.awt.Point;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.stage.WindowEvent;
import spacetrader.cosmos.SparseSpace;
import spacetrader.cosmos.Universe;
import spacetrader.cosmos.system.Planet;
import spacetrader.player.Player;
import spacetrader.cosmos.system.SolarSystem;
import spacetrader.cosmos.system.SunType;
import spacetrader.main.SpaceTrader;
import spacetrader.save.SaveGame;


/**
 * FXML Controller class. Controller class for the star screen. This class
 * throws an issue with checkstyle saying that Fan Out Complexity is too great,
 * but this class must import all the UI elements which add a lot of
 * class dependency.
 * 
 * @author Aaron McAnally
 */
public class StarScreenController implements Initializable {
    
    /**
     * Local reference of solar system.
     */
    private SolarSystem selectedSolarSystem;
    
    /**
     * Local reference of player.
     */
    private Player player;
    
    /**
     * Local reference of universe.
     */
    private Universe universe;
    
    /**
     * Graphics object that draws items.
     */
    private GraphicsContext g;
    
    /**
     * Zoom variable to scale universe.
     */
    private double zoom = 10;
    
    /**
     * All the dragging related floats.
     */
    private double dragOffsetX = 0;
    /**
     * All the dragging related floats.
     */
    private double dragOffsetY = 0;
    /**
     * All the dragging related floats.
     */
    private double preDragX = 0;
    /**
     * All the dragging related floats.
     */
    private double preDragY = 0;
    
    /**
     * The two variables needed for panning a map.
     */
    private double mapOffsetX = 0;
    /**
     * The two variables needed for panning a map.
     */
    private double mapOffsetY = 0;
    
    /**
     * The two booleans needed for dragging the map.
     */
    private boolean dragging = false;
    /**
     * The two booleans needed for dragging the map.
     */
    private boolean dragFinished = true;
    
    /**
     * The HashMap that holds the sun image URLs.
     */
    private HashMap<String, Image> sunImages;
    
    /**
     * Contains locations of solar systems to aid in drawing.
     */
    private HashMap<Point, SolarSystem> solarSystemLocations;
    
    /**
     * Keeps track of travelable solar systems.
     */
    private ArrayList<SolarSystem> travelable;
    
    /**
     * scales the distance between universes.
     */
    private static final int universeScale = 20;
    
    /**
     * Timer that handles animations.
     */
    private Timer timer;
    

    
    /**
     * FXML reference to the canvas.
     */
    @FXML
    private Canvas gameCanvas;

    /**
     * FXML reference to the zoom slider.
     */
    @FXML
    private Slider zoomSlider;
    
    /**
     * FXML reference to the player info.
     */
    @FXML
    private Label playerInfo;
    
    /**
     * FXML reference to the star backdrop.
     */
    @FXML
    private Pane starBackdrop;
    
    /**
     * FXML reference to the button that goes to the planet.
     */
    @FXML
    private Button goToPlanet;
    
    /**
     * FXML reference to the solar system info pane.
     */
    @FXML
    private BorderPane solarSystemInfo;
    
    /**
     * FXML reference to the label that shows the solar system name.
     */
    @FXML
    private Label solarSystemName;
    
    /**
     * FXML reference to the list of planets.
     */
    @FXML
    private ListView planetList;
    
    /**
     * FXML reference to the fuel info label.
     */
    @FXML
    private Label fuelInfo;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        universe = SpaceTrader.getInstance().getUniverse();
        player = SpaceTrader.getInstance().getPlayer();
        travelable = player.getTravelable(universe);
        solarSystemLocations = new HashMap<>();
        sunImages = new HashMap<>();
        
        for (SunType star : (ArrayList<SunType>) SunType.getList(SunType.class)) {
            if (star.usesImage()) {
                sunImages.put(star.getName(), new Image(star.getImage()));
            }
        }
        
        //adds listener for the zoom slider
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                zoom = (float) zoomSlider.getValue();
                dragFinished = true;
                drawUniverse();
            }
        
        });
        
        playerInfo.setText(player.getName()
                + "\nPilot Skill: " + player.getPilotSkill()
                + "\nFighter Skill: " + player.getFighterSkill()
                + "\nTrader Skill: " + player.getTraderSkill()
                + "\nEngineer Skill: " + player.getEngineerSkill()
                + "\nInvestor Skill: " + player.getInvestorSkill());
        // gets graphic object and draws universe to begin
        g = gameCanvas.getGraphicsContext2D();
        g.setFill(Color.WHITE);
        
        SaveGame.save(player.getName() + ".sav", player, universe);
        
        drawUniverse();
    }
    
    /**
     * Happens any time mouse is clicked.
     * @param event 
     */
    @FXML
    private void mouseClicked(MouseEvent event) {
        checkClick(event);
    }
    
    /**
     * Occurs any time the mouse is clicked.
     * @param event 
     */
    @FXML
    private void mouseDown(MouseEvent event) {
        preDragX = event.getX();
        preDragY = event.getY();
    }
    
    /**
     * Moves player to selected solar system.
     */
    @FXML
    private void goToSolarSystem() {
        Planet selected = (Planet) planetList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            player.move(selectedSolarSystem);
        } else {
            player.move(selectedSolarSystem, selected);
        }
        if (timer != null) {
            timer.cancel();
        }
        Random rand = new Random();
        if (rand.nextFloat() < 0.3) {
            SpaceTrader.getInstance().goToEncounter();
            return;
        }
        if (player.isDead()) {
            SpaceTrader.getInstance().goToWelcomeScreen();
            return;
        }
        SpaceTrader.getInstance().goToSolarSystemView();
    }
    
    /**
     * Helper method that finds solar system closest to a point.
     * @param p Point of Solar System
     * @return SolarSystem closest
     */
    private SolarSystem findClosestSolarSystem(Point p) {
        SolarSystem forreturn = solarSystemLocations.get(p);
        if (forreturn == null) {
            Point[] points = new Point[solarSystemLocations.keySet().size()];
            solarSystemLocations.keySet().toArray(points);
            Point closest = points[0];
            for (Point test : points) {
                if (closest.distance(p.x, p.y) > test.distance(p.x, p.y)) {
                    closest = test;
                }
            }
            if (closest.distance(p.x, p.y) < 10) {
                forreturn = solarSystemLocations.get(closest);
            }
        }
        return forreturn;
    }
    
    /**
     * Handles mouse moving event.
     * @param event 
     */
    @FXML
    private void mouseMove(MouseEvent event) {
        SolarSystem mouseOver = findClosestSolarSystem(new Point((int) event.getX() - 5, (int) event.getY() - 5));
        if (mouseOver != null) {
            drawUniverse();
            g.setStroke(Color.WHITE);
            g.setFill(Color.WHITE);
            g.strokeOval(event.getX() - 15, event.getY() - 15, 30, 30);
            g.fillText(mouseOver.name(), event.getX(), event.getY());
            if (travelable.contains(mouseOver)) {
                double radx = ((player.getCurrentSolarSystem().getX() * universeScale) - mapOffsetX - dragOffsetX) + (512 - mapOffsetX - dragOffsetX);
                double rady = ((player.getCurrentSolarSystem().getY() * universeScale) - mapOffsetY - dragOffsetY) + (288 - mapOffsetY - dragOffsetY);
                g.strokeLine(radx, rady, event.getX(), event.getY());
            }
        } else {
            drawUniverse();
        }
    }
    
    /**
     * update drag offsets when dragging.
     * @param event info about the mouse
     */
    @FXML
    private void mouseDrag(MouseEvent event) {
        dragging = true;
        double tempX = event.getX();
        double tempY = event.getY();
        dragOffsetX = (preDragX - tempX) / 2;
        dragOffsetY = (preDragY - tempY) / 2;
        drawUniverse();
    }
    
    /**
     * when mouse is rele ased, update mapoffset.
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
     * helper method that checks if we clicked on a solar system.
     * @param event contains info about where the mouse was clicked
     */
    private void checkClick(MouseEvent event) {
        SolarSystem mouseOver = findClosestSolarSystem(new Point((int) event.getX() - 5, (int) event.getY() - 5));
        if (mouseOver != null && travelable.contains(mouseOver)) {
            selectedSolarSystem = mouseOver;
            animateInfoScreen(true);
            solarSystemName.setText(mouseOver.name());
            ObservableList<Planet> planets = FXCollections.observableArrayList(mouseOver.planets());
            planetList.setItems(planets);
            fuelInfo.setText("Current:\n" + player.getShip().getFuel() + "\n\nCost:\n" + player.distanceToSolarSystem(mouseOver) + "\n\nSum:\n" + (player.getShip().getFuel() - player.distanceToSolarSystem(mouseOver)));
            drawUniverse();
        } else {
            drawUniverse();
        }
    }
    
    /**
     * Helper method that draws universe based on map offset and scale.
     */
    private void drawUniverse() {
        Random r = new Random();
        Point lower = getLower();
        Point upper = getUpper();
        lower = new Point(lower.x / (universeScale), lower.y / (universeScale));
        upper = new Point(upper.x / (universeScale), upper.y / (universeScale));

        universe.generateFrom(lower.x, lower.y, upper.x, upper.y );
        
        //sets scale of star backdrop
        starBackdrop.setScaleX(zoom / 5);
        starBackdrop.setScaleY(zoom / 5);
        gameCanvas.setScaleX(zoom / 10);
        gameCanvas.setScaleY(zoom / 10);
        
        //clears before drawing
        g.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        
        //int x = universe.xMin(), y = universe.yMin();
        starBackdrop.setTranslateX((-mapOffsetX - dragOffsetX) * 2);
        starBackdrop.setTranslateY((-mapOffsetY - dragOffsetY) * 2);
        
        //draw non travelable solar systems
        for (SparseSpace.SparseIterator iter = universe.iterateFrom(lower.x, lower.y, upper.x, upper.y); iter.hasNext();) {
            SolarSystem a = iter.next();
            Image starImage = null;
            if (a != null && !travelable.contains(a)) {
                int x = a.getX() * universeScale;
                int y = a.getY() * universeScale;
                r.setSeed(a.name().hashCode());
                
                //set color of star based on suntype
                if (a.sunType().usesColor()) {
                    g.setFill(new Color(a.sunType().getR(), a.sunType().getG(), a.sunType().getB(), 1.0));
                } else {
                    starImage = sunImages.get(a.sunType().getName());
                }
                
                //initializes size and position variables
                double size = 20 + a.planets().length * 2;
                double xPosition = (x - mapOffsetX - dragOffsetX) + (512 - mapOffsetX - dragOffsetX) - (size / 2);
                double yPosition = (y - mapOffsetY - dragOffsetY) + (288 - mapOffsetY - dragOffsetY) - (size / 2);
                if (dragFinished) {
                    solarSystemLocations.put(new Point((int) (xPosition + (size / 2)), (int) (yPosition + (size / 2))), a);
                }
                if (starImage == null) {
                    g.fillOval(xPosition, yPosition, size, size);
                } else {
                    Affine old = g.getTransform();
                    double angle = r.nextDouble() * 360;
                    rotate(angle, xPosition + size / 2, yPosition + size / 2);
                    g.drawImage(starImage, xPosition, yPosition, size, size);
                    g.setTransform(old);
                }
                g.setFill(Color.WHITE);
            }
        }
        
        //darken non-travelable planets
        g.setFill(new Color(0, 0, 0, 0.7));
        g.fillRect(0, 0, 1024, 512);
        
        //draw travel radius
        double radsize = player.getTravelRadius() * universeScale * 2;
        double radx = (((player.getCurrentSolarSystem().getX() * universeScale) - mapOffsetX - dragOffsetX)) + (512 - mapOffsetX - dragOffsetX) - (radsize / 2f);
        double rady = (((player.getCurrentSolarSystem().getY() * universeScale) - mapOffsetY - dragOffsetY)) + (288 - mapOffsetY - dragOffsetY) - (radsize / 2f);
        g.strokeOval(radx, rady, radsize, radsize);
        g.setFill(new Color(0, 0, 1, 0.1));
        g.fillOval(radx, rady, radsize, radsize);
        
        drawTravelable();
    }
    
    /**
     * Draws travelable planets.
     */
    private void drawTravelable() {
        //draw travelable planets
        g.setStroke(Color.WHITE);
        g.setFill(Color.WHITE);
        Random r = new Random();
        for (SolarSystem a : travelable) {
            r.setSeed(a.name().hashCode());
            int x = a.getX() * universeScale;
            int y = a.getY() * universeScale;
            Image starImage = null;
            if (a.sunType().usesColor()) {
                g.setFill(new Color(a.sunType().getR(), a.sunType().getG(), a.sunType().getB(), 1.0));
            } else {
                starImage = sunImages.get(a.sunType().getName());
            }
            double size = 20 + a.planets().length * 2;
            double xPosition = (x - mapOffsetX - dragOffsetX) + (512 - mapOffsetX - dragOffsetX) - (size / 2);
            double yPosition = (y - mapOffsetY - dragOffsetY) + (288 - mapOffsetY - dragOffsetY) - (size / 2);

            if (dragFinished) {
                solarSystemLocations.put(new Point((int) (xPosition + (size / 2)), (int) (yPosition + (size / 2))), a);
            } //blah
            if (starImage == null) {
                g.fillOval(xPosition, yPosition, size, size);
            } else {
                Affine old = g.getTransform();
                double angle = r.nextDouble() * 360;
                rotate(angle, xPosition + size / 2, yPosition + size / 2);
                g.drawImage(starImage, xPosition, yPosition, size, size);
                g.setTransform(old);
            }
        }
    }
    
    /**
     * private helper method to rotate star textures.
     * @param angle double the angle to rotate
     * @param px double x position of the point
     * @param py double y position of the point
     */
    private void rotate(double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
    
    /**
     * helper method for getting the lower point of the universe.
     * @return Point of the lower
     */
    private Point getLower() {
        int lowerX;
        int lowerY;
        lowerY = (int) (-gameCanvas.getHeight() + mapOffsetY * 2);
        lowerX = (int) (-gameCanvas.getWidth() + mapOffsetX * 2);
        return new Point(lowerX, lowerY);
    }
    
    /**
     * helper method for getting the upper point of the universe.
     * @return Point of the upper
     */
    private Point getUpper() {
        int upperX;
        int upperY;
        upperY = (int) (gameCanvas.getHeight() + mapOffsetY * 2);
        upperX = (int) (gameCanvas.getWidth() + mapOffsetX * 2);
        return new Point(upperX, upperY);
    }
    
    /**
     * helper method to animate the info screen.
     * @param appear 
     */
    private void animateInfoScreen(boolean appear) {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                        if (appear) {
                            dragging = true;
                            if (solarSystemInfo.getScaleY() < 1) {
                                solarSystemInfo.setScaleY(solarSystemInfo.getScaleY() + 0.1);
                            } else {
                                solarSystemInfo.setScaleY(1);
                                this.cancel();
                            }
                        } else {
                            if (solarSystemInfo.getScaleY() > 0) {
                                solarSystemInfo.setScaleY(solarSystemInfo.getScaleY() - 0.1);
                            } else {
                                solarSystemInfo.setScaleY(0);
                                this.cancel();
                            }
                        }
                        drawUniverse();
                    });
            }
        }, 0, 10);
    }
    
    /**
     * Used to set the scene so that timer thread can exit.
     * @param scene the scene to set
     */
    public void setScene(Scene scene) {
        scene.getWindow().setOnCloseRequest((WindowEvent event) -> {
                if (timer != null) {
                    timer.cancel();
                }
            });
    }
    
    /**
     * Javafx helper method to close info pane.
     */
    @FXML
    private void closeInfoPane() {
        animateInfoScreen(false);
    }
    
}
