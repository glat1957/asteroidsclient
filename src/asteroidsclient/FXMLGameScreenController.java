package asteroidsclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

public class FXMLGameScreenController implements Initializable {

    private Ship playerOne;
    private Ship playerTwo;
    private ImageView playerOneImage;
    private ImageView playerTwoImage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void setplayerOne(Ship playerOne){
        this.playerOne = playerOne;
    }
    
    public void setPlayerTwo(Ship playerTwo){
        this.playerTwo = playerTwo;
    }
}
