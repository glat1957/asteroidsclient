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
        //startButton.setDisable(true);
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
            controller.setCurrentPlayer(gateway.getShipModel(SHIP_1));
            controller.setGateway(gateway);

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Game Screen");
            scene.setOnKeyPressed((evt) -> controller.rotatePlayer(evt));
            
            new Thread(new UpdateOtherPlayer(controller.getPlayerNum(), gateway, controller.getPlayer1Ship(), controller.getPlayer2Ship())).start();
           
            // Show game.
            stage.show();
            
            stage.setOnCloseRequest(closeEvent ->System.exit(0));

            // Close start scene.
            Stage oldStage = (Stage) startButton.getScene().getWindow();
            oldStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
