package org.example.werwirdmillionaer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button playButton;
    @FXML
    public VBox vbox;
    public void startGame() {
        System.out.println("Game started!");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/werwirdmillionaer/Game.fxml"));
            Parent gameRoot = loader.load();

            Scene currentScene = playButton.getScene();
            currentScene.setRoot(gameRoot);

            GameController gameController = loader.getController();

            System.out.println("Switched to game screen successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Game.fxml: " + e.getMessage());
        }
    }

    public void showHighscore() {
        System.out.println("Highscore shown!");
    }

    public void showSettings() {
        System.out.println("Settings shown!");
    }

    public void exitGame() {
        System.out.println("Game exited!");
            Platform.exit();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image hintergrundMainMenue = new Image(getClass().getResourceAsStream("HintergrundMainMenue.png"));
        double width = 1920;
        double height = 1090;
        BackgroundImage backgroundImage = new BackgroundImage(hintergrundMainMenue, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(width, height, false, false, true, true));
        vbox.setBackground(new Background(backgroundImage));
    }



}
