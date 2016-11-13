package asteroidsclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXMLTitleScreenController implements Initializable, asteroids.AsteroidsConstants {

    private AsteroidsGateway gateway;
    @FXML
    private Button startButton;
    @FXML
    private Text status;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //status.setText("Waiting for other player...");
        status.setText("PRESS START TO BEGIN...");

        //startButton.setDisable(true);

        FadeTransition ft = new FadeTransition(Duration.millis(2000), status);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        new Thread(() -> {
            gateway = new AsteroidsGateway();

           
                /*while (gateway.getNumConnected() == 2) {
                    status.setText("Press start to begin...");
                    startButton.setDisable(false);
                }*/

               

           
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

            stage.setOnCloseRequest(closeEvent -> System.exit(0));

            // Close start scene.
            Stage oldStage = (Stage) startButton.getScene().getWindow();
            oldStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
