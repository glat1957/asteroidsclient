package asteroidsclient;

import asteroids.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import physics.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.layout.Pane;

public class FXMLGameScreenController implements Initializable, asteroids.AsteroidsConstants {

    private AsteroidsGateway gateway;
    
    @FXML
    public AnchorPane mainPane;
    private ShipModel currentPlayer;
    public Simulation sim = new Simulation(gateway);
    public List<Bullet> bulletsInScene = sim.getBullets();
    public ArrayList<Circle> bulletShapes = sim.getBulletShapes();

    @FXML
    private ImageView BackgroundImageView;
    private double playerRotation = 0.0;

    @FXML
    private ImageView player1Ship;
    @FXML
    private Pane player1Pane;
    @FXML
    private Circle player1Nose;
    private Point player1Loc = new Point(305, 250);

    @FXML
    private ImageView player2Ship;
    @FXML
    private Pane player2Pane;
    @FXML
    private Circle player2Nose;
    private Point player2Loc = new Point(395, 250);

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setGateway(AsteroidsGateway gateway) {
        this.gateway = gateway;
    }

    public void setCurrentPlayer(ShipModel currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getPlayerNum() {
        return currentPlayer.getPlayerNum();
    }

    public Point getPlayerLoc() {
        if (currentPlayer.getPlayerNum() == 1) {
            return player1Loc;
        } else {
            return player2Loc;
        }
    }

    public Point getPlayerNoseLoc() {
        if (currentPlayer.getPlayerNum() == 1) {
            return new Point(player1Nose.localToScene(player1Nose.getLayoutX(), player1Nose.getLayoutX()).getX(),
            player1Nose.localToScene(player1Nose.getLayoutX(), player1Nose.getLayoutX()).getY());
        } else {
            return new Point(player2Nose.localToScene(player2Nose.getLayoutX(), player2Nose.getLayoutX()).getX(),
            player2Nose.localToScene(player2Nose.getLayoutX(), player2Nose.getLayoutX()).getY());
        }
    }

    public Pane getPlayer1Pane() {
        return player1Pane;
    }

    public Pane getPlayer2Pane() {
        return player2Pane;
    }

    private Double setPlayerRotation(Double rotation) {
        playerRotation += rotation;
        if (playerRotation > 360.0) {
            playerRotation -= 360.0;
        } else if (playerRotation < 0.0) {
            playerRotation = 360.0 + playerRotation;
        }
        return playerRotation;
    }

    private void rotateCurrentPlayer(Double rotation) {
        if (currentPlayer.getPlayerNum() == 1) {
            player1Pane.setRotate(rotation);
            gateway.sendPlayer1Rot(playerRotation);
        } else {
            player2Pane.setRotate(rotation);
            gateway.sendPlayer2Rot(playerRotation);
        }
    }

    @FXML
    public void keyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT: {
                setPlayerRotation(-5.0);
                rotateCurrentPlayer(playerRotation);
                break;
            }
            case RIGHT: {
                setPlayerRotation(5.0);
                rotateCurrentPlayer(playerRotation);
                break;
            }
            case SPACE: {
                // Use player location to set spawn point for bullet and use the nose
                // location to set the velocity so that the bullet will be directed in the
                // direction of the nose.
                sim.getBullets().add(new Bullet(getPlayerLoc().x, getPlayerLoc().y,
                        10, 1, 1));
                sim.getBulletShapes().add(new Circle(getPlayerLoc().x, getPlayerLoc().y, 5, Color.RED));
                mainPane.getChildren().add(sim.getBulletShapes().get(sim.getBulletShapes().size() - 1));
            }
        }
    }
    
    /*@FXML
    public void showAsteroid(){
        mainPane.getChildren().add(update.asteroidShapes.get(update.asteroidShapes.size() -1));
}*/
}

// Thread to update players' screens every few milliseconds.
class UpdateOtherPlayer implements Runnable, asteroids.AsteroidsConstants {

    private final Lock lock = new ReentrantLock();
    private final int playerNum;
    private final AsteroidsGateway gateway;
    private final Pane player1Pane;
    private final Pane player2Pane;
    private List<Asteroid> asteroidsInScene = Collections.synchronizedList(new ArrayList<>());
    public ArrayList<Circle> asteroidShapes = new ArrayList<>();

    public UpdateOtherPlayer(int playerNum, AsteroidsGateway gateway, Pane player1Pane, Pane player2Pane) {
        this.playerNum = playerNum;
        this.gateway = gateway;
        this.player1Pane = player1Pane;
        this.player2Pane = player2Pane;
        
        
    }

    @Override
    public void run() {
        while (true) {
            if (playerNum == 1) {
                double tempPlayer2Rot = gateway.getPlayer2Rot();
                Platform.runLater(() -> player2Pane.setRotate(tempPlayer2Rot));
            } else {
                double tempPlayer1Rot = gateway.getPlayer1Rot();
                
                asteroidsInScene = gateway.getAsteroid();
                for(Asteroid a: asteroidsInScene){
                   asteroidShapes.add(new Circle(a.returnX(), a.returnY(), a.returnRadius(), Color.BLUE)); 
                }
                
                Platform.runLater(() -> player1Pane.setRotate(tempPlayer1Rot));
            }
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }
}

class Simulate implements Runnable, asteroids.AsteroidsConstants {

    private final AsteroidsGateway gateway;
    private final Simulation sim;
    private List<Bullet> bulletsInScene;
    private ArrayList<Circle> bulletShapes;
    
    public Simulate(AsteroidsGateway gateway, Simulation sim, 
            List<Bullet> bulletsInScene, ArrayList<Circle> bulletShapes) {
        this.gateway = gateway;
        this.sim = sim;
        this.bulletsInScene = bulletsInScene;
        this.bulletShapes = bulletShapes;
    }

    @Override
    public void run() {
        while (true) {
            
            sim.evolve(1.0);
            sim.updateShapes();
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
            }
        }
    }
}