package asteroidsclient;

import asteroids.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import physics.Point;

public class FXMLGameScreenController implements Initializable, asteroids.AsteroidsConstants {

    @FXML
    private AnchorPane mainPane;
    private ShipModel currentPlayer;
    private List<Bullet> bulletsInScene = Collections.synchronizedList(new ArrayList<>());
    private ArrayList<Circle> bulletShapes = new ArrayList<>();
    
    @FXML
    private ImageView BackgroundImageView;
    private Image playerImage;
    private double playerRotation = 0;

    @FXML
    private ImageView player1Ship;
    private Point player1Loc = new Point(295, 253);
    @FXML
    private ImageView player2Ship;
    private Point player2Loc = new Point(405, 253);

    private AsteroidsGateway gateway;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setGateway(AsteroidsGateway gateway) {
        this.gateway = gateway;
    }

    public void setCurrentPlayer(ShipModel currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    public int getPlayerNum(){
        return currentPlayer.getPlayerNum();
    }
    
    private Point getPlayerLoc(){
        if(currentPlayer.getPlayerNum() == 1){
            return player1Loc;
        }
        else {
            return player2Loc;
        }
    }
    
    public ImageView getPlayer1Ship(){
        return player1Ship;
    }
    
    public ImageView getPlayer2Ship(){
        return player2Ship;
    }

    private Double setPlayerRotation(Double rotation) {
        playerRotation += rotation;
        if (playerRotation > 360) {
            playerRotation -= 360;
        } else if (playerRotation < 0) {
            playerRotation = 360 + playerRotation;
        }
        return playerRotation;
    }

    private void rotateCurrentPlayer(Double rotation) {
        if (currentPlayer.getPlayerNum() == 1) {
            player1Ship.setRotate(rotation);
            gateway.sendPlayer1Rot(playerRotation);
        } else {
            player2Ship.setRotate(rotation);
            gateway.sendPlayer2Rot(playerRotation);
        }
    }

    @FXML
    public void keyEvent(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT: {
                setPlayerRotation(-1.0);
                rotateCurrentPlayer(playerRotation);
                break;
            }
            case RIGHT: {
                setPlayerRotation(1.0);
                rotateCurrentPlayer(playerRotation);
                break;
            }
            case SPACE: {
                 bulletsInScene.add(new Bullet(getPlayerLoc().x, getPlayerLoc().y, 1, 1, 1));
                 bulletShapes.add(new Circle(getPlayerLoc().x, getPlayerLoc().y, 10, Color.RED));
                 mainPane.getChildren().add(bulletShapes.get(bulletShapes.size() - 1));
            }
        }
    }
}

// Thread to update players' screens every few milliseconds.
class UpdateOtherPlayer implements Runnable, asteroids.AsteroidsConstants {

    private final Lock lock = new ReentrantLock();
    private final int playerNum;
    private final AsteroidsGateway gateway;
    private final ImageView player1Ship;
    private final ImageView player2Ship;

    public UpdateOtherPlayer(int playerNum, AsteroidsGateway gateway, ImageView player1Ship, ImageView player2Ship) {
        this.playerNum = playerNum;
        this.gateway = gateway;
        this.player1Ship = player1Ship;
        this.player2Ship = player2Ship;
    }

    @Override
    public void run() {
        while (true) {
            if (playerNum == 1) {
                double tempPlayer2Rot = gateway.getPlayer2Rot();
                Platform.runLater(() -> player2Ship.setRotate(tempPlayer2Rot));
            } else {
                double tempPlayer1Rot = gateway.getPlayer1Rot();
                Platform.runLater(() -> player1Ship.setRotate(tempPlayer1Rot));
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }
}
