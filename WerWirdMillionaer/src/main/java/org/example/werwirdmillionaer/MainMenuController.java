package org.example.werwirdmillionaer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button playButton;
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


}
