package asteroidsclient;

import asteroids.Ship;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

public class FXMLGameScreenController implements Initializable, asteroids.AsteroidsConstants {

    private Ship currentPlayer;
    private Ship otherPlayer;

    @FXML
    private ImageView playerImageView;
    private Image playerImage;
    private double playerRotation = 0;
    @FXML
    private Rectangle currentPlayerShip;

    @FXML
    private Rectangle otherPlayerShip;
    
    private AsteroidsGateway gateway;

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
    }
    
    public void setGateway(AsteroidsGateway gateway){
        this.gateway = gateway;
    }

    public void setCurrentPlayer(Ship currentPlayer) {
        this.currentPlayer = currentPlayer;
        System.out.println(currentPlayer.getLives());
    }

    public void setOtherPlayer(Ship otherPlayer) {
        this.otherPlayer = otherPlayer;
    }
    
    public Double setPlayerRotation(Double rotation){
        playerRotation += rotation;
        if(playerRotation > 360)
            playerRotation -= 360;
        else if(playerRotation < 0)
            playerRotation = 360 + playerRotation;
        return playerRotation;
    }

    @FXML
    public void rotatePlayer(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case LEFT: {
                setPlayerRotation(-5.0);
                currentPlayerShip.setRotate(playerRotation);
                break;
            }
            case RIGHT: {
                setPlayerRotation(5.0);
                currentPlayerShip.setRotate(playerRotation);
                break;
            }
        }
    }

}
