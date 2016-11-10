package asteroidsclient;

import asteroids.Ship;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FXMLGameScreenController implements Initializable, asteroids.AsteroidsConstants {

    private Ship playerOne;
    private Ship playerTwo;
    private ImageView playerOneImageView;
    private Image playerOneImage;
    private ImageView playerTwoImageView;
    private AsteroidsGateway gateway;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    
    public void setplayerOne(Ship playerOne){
        this.playerOne = playerOne;
        System.out.println(playerOne.getLives());
    }
    
    public void setPlayerTwo(Ship playerTwo){
        this.playerTwo = playerTwo;
    }
}
