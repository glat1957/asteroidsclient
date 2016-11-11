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

    private Ship playerOne;
    private Ship playerTwo;

    @FXML
    private ImageView playerOneImageView;
    private Image playerOneImage;
    private double playerOneRotation = 0;

    @FXML
    private ImageView playerTwoImageView;
    private Image playerTwoImage;
    private AsteroidsGateway gateway;
    @FXML
    private Rectangle temp;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void setPlayerOne(Ship playerOne) {
        this.playerOne = playerOne;
        System.out.println(playerOne.getLives());
    }

    public void setPlayerTwo(Ship playerTwo) {
        this.playerTwo = playerTwo;
    }

    public void setPlayerOneImage() {
        //playerOneImageView = new ImageView(getClass().getClassLoader().getResource("resources/Spaceship1.png").toString());

        /*try {
            System.out.println(getClass().getClassLoader().getResource("resources/Spaceship1.png").toString());
            playerOneImage = new Image(getClass().getClassLoader().getResource("resources/Spaceship1.png").toString());
            playerOneImageView.setImage(playerOneImage);
            playerOneImageView.setFitWidth(0);
            playerOneImageView.setFitHeight(0);
        } catch (Exception ex) {
            ex.printStackTrace();
         */
    }

    @FXML
    public void rotateShip(KeyEvent keyEvent) {
        System.out.println("Key");
    }
    
}
