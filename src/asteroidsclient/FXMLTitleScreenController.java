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

        startButton.setDisable(true);
        status.setText("Waiting for other player...");

        FadeTransition ft = new FadeTransition(Duration.millis(2000), status);
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        new Thread(() -> {
            gateway = new AsteroidsGateway();

            while (gateway.getNumConnected() < 2) {
                status.setText("WAITING FOR OTHER PLAYER...");
            }

            status.setText("PRESS START TO BEGIN...");
            startButton.setDisable(false);

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
            controller.setScore();

            Scene scene = new Scene(root);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Game Screen: Player " + controller.getCurrentPlayer().getPlayerNum());
            scene.setOnKeyPressed((evt) -> controller.keyEvent(evt));

            new Thread(new UpdatePlayer(
                    controller.getPlayerNum(), gateway, controller.getPlayer1Pane(),
                    controller.getPlayer2Pane(), controller.player1Lives, controller.player2Lives,
                    controller.getCurrentPlayer(), controller.score, controller.mainPane)).start();

            // Show game.
            stage.show();

            // Close all application processes upon exit.
            stage.setOnCloseRequest(closeEvent -> {
                gateway.disconnectPlayer();
                System.exit(0);
            });

            // Close start scene.
            Stage oldStage = (Stage) startButton.getScene().getWindow();
            oldStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
