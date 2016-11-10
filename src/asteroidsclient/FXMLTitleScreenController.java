package asteroidsclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class FXMLTitleScreenController implements Initializable, asteroids.AsteroidsConstants {

    private AsteroidsGateway gateway;
    @FXML
    private Button startButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new Thread(() -> {
            gateway = new AsteroidsGateway();
        }).start();
    }

    @FXML
    public void startGame(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLGameScreen.fxml"));
            Parent root = (Parent) loader.load();

            FXMLGameScreenController controller = (FXMLGameScreenController) loader.getController();
            controller.setPlayerOne(gateway.getShip(SHIP_1));
            controller.setPlayerOneImage();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Game Screen");
            stage.show();
            Stage oldStage = (Stage) startButton.getScene().getWindow();
            oldStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
