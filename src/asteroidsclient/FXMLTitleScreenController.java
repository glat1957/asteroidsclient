package asteroidsclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class FXMLTitleScreenController implements Initializable {
    
    private AsteroidsGateway gateway;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(() -> {
            gateway = new AsteroidsGateway();
        }).start();
    }    
    
}
