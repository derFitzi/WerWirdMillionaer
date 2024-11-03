package org.example.werwirdmillionaer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private Button playButton;

    @FXML
    private Label highscoreLabel;

    @FXML
    private Button exitButton;

    @FXML
    public VBox vbox;

    @FXML
    public Pane pane;

    Connection connection;

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


    public void exitGame() {
        System.out.println("Game exited!");
        Platform.exit();
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/who_wants_to_be_a_millionaire", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void getHighscore()
    {
        try{
            String query = "SELECT stage FROM highscore LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next())
            {
                int[] prize  = {0,100,200,300,400,500,1000,2000,4000,8000,16000,32000,64000,125000,500000,1000000};
                int highscoreIndex = resultSet.getInt("stage");
                highscoreLabel.setText("HIGHSCORE: "+prize[highscoreIndex]+ " €");
                highscoreLabel.setVisible(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectToDatabase();
        if(connection != null)
        {
            // Hintergrundbild setzen
            Image hintergrundMainMenue = new Image(getClass().getResourceAsStream("/org/example/werwirdmillionaer/HintergrundMainMenue.jpg"));
            BackgroundImage backgroundImage = new BackgroundImage(hintergrundMainMenue,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
            pane.setBackground(new Background(backgroundImage));

            // Bilder für die Buttons setzen und Größe anpassen
            ImageView playImageView = new ImageView(new Image(getClass().getResourceAsStream("/org/example/werwirdmillionaer/play.png")));
            playImageView.setFitWidth(64);  // Setze die Breite auf 64 Pixel
            playImageView.setFitHeight(64); // Setze die Höhe auf 64 Pixel
            playButton.setGraphic(playImageView);

            /* Settings wurden entfernt
            ImageView settingsImageView = new ImageView(new Image(getClass().getResourceAsStream("/org/example/werwirdmillionaer/settings.png")));
            settingsImageView.setFitWidth(64);
            settingsImageView.setFitHeight(64);
            settingsButton.setGraphic(settingsImageView);
             */

            ImageView exitImageView = new ImageView(new Image(getClass().getResourceAsStream("/org/example/werwirdmillionaer/logout.png")));
            exitImageView.setFitWidth(64);
            exitImageView.setFitHeight(64);
            exitButton.setGraphic(exitImageView);

            getHighscore();
        }
        else
        {
            System.out.println("Database Connection failed!");
        }
    }

}
