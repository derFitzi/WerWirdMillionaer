package org.example.werwirdmillionaer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    private Label questionLabel;
    @FXML
    private Label prizeLabel;
    @FXML
    private Button option1Button;
    @FXML
    private Button option2Button;
    @FXML
    private Button option3Button;
    @FXML
    private Button option4Button;
    @FXML
    private Button mainMenuButton;

    private int currentQuestionIndex = 0;
    private int[] prize  = {0,100,200,300,400,500,1000,2000,4000,8000,16000,32000,64000,125000,500000,1000000};
    int prizeIndex = 0;
    private int difficultyLevel = 1;

    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing GameController...");
        mainMenuButton.setVisible(false);
        System.out.println(difficultyLevel);
        connectToDatabase();
        if (connection != null) {
            System.out.println("Database connection established.");
            loadNextQuestion();
        } else {
            System.out.println("Database connection failed.");
            questionLabel.setText("Database connection failed.");
            option1Button.setDisable(true);
            option2Button.setDisable(true);
            option3Button.setDisable(true);
            option4Button.setDisable(true);
        }
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/who_wants_to_be_a_millionaire", "root", "");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNextQuestion() {
        if (connection == null) {
            throw new IllegalStateException("Database connection is not established.");
        }

        try {
            String query = "SELECT * FROM Questions WHERE DifficultyLevel = ? ORDER BY RAND() LIMIT 1";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, difficultyLevel);
            ResultSet resultSet = statement.executeQuery();


            if (resultSet.next()) {
                String question = resultSet.getString("Question");
                String option1 = resultSet.getString("AnswerOption1");
                String option2 = resultSet.getString("AnswerOption2");
                String option3 = resultSet.getString("AnswerOption3");
                String option4 = resultSet.getString("AnswerOption4");
                currentQuestionIndex = resultSet.getInt("Id");

                questionLabel.setText(question);
                prizeLabel.setText("Current Win: "+prize[prizeIndex] +" €");
                option1Button.setText(option1);
                option2Button.setText(option2);
                option3Button.setText(option3);
                option4Button.setText(option4);

                System.out.println("Question: " + question);
                System.out.println("1: " + option1);
                System.out.println("2: " + option2);
                System.out.println("3: " + option3);
                System.out.println("4: " + option4);
            } else {
                questionLabel.setText("No more questions available.");
                option1Button.setDisable(true);
                option2Button.setDisable(true);
                option3Button.setDisable(true);
                option4Button.setDisable(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOption1() {
        checkAnswer(1);
    }

    @FXML
    private void handleOption2() {
        checkAnswer(2);
    }

    @FXML
    private void handleOption3() {
        checkAnswer(3);
    }

    @FXML
    private void handleOption4() {
        checkAnswer(4);
    }

    private void checkAnswer(int selectedOption) {
        if (connection == null) {
            throw new IllegalStateException("Database connection is not established.");
        }

        try {
            String query = "SELECT Solution FROM Questions WHERE Id = ? ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, currentQuestionIndex);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int correctAnswer = resultSet.getInt("Solution");
                if (selectedOption == correctAnswer) {
                    prizeIndex ++; // Increment prize
                    if(prizeIndex==15)
                    {
                        questionLabel.setText("Congratulations! You're a Millionaire! You've conquered the million-dollar question! \uD83C\uDF89");
                        prizeLabel.setText(prize[prizeIndex] +" €");
                        mainMenuButton.setVisible(true);
                        option1Button.setDisable(true);
                        option2Button.setDisable(true);
                        option3Button.setDisable(true);
                        option4Button.setDisable(true);
                        checkHighscore();
                    }
                    else
                    {
                        difficultyLevel++;
                        loadNextQuestion();
                    }
                } else
                {
                    questionLabel.setText("Incorrect answer. Game over.");
                    mainMenuButton.setVisible(true);
                    option1Button.setDisable(true);
                    option2Button.setDisable(true);
                    option3Button.setDisable(true);
                    option4Button.setDisable(true);
                    checkHighscore();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void returnToMainMenu() {
        System.out.println("Returning to main menu...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/werwirdmillionaer/Menue.fxml"));
            Parent menuRoot = loader.load();

            Scene currentScene = option1Button.getScene();
            currentScene.setRoot(menuRoot);

            // Korrekt casten auf MainMenuController
            MainMenuController mainMenuController = loader.getController();
            System.out.println("Switched to menu screen successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading Menu.fxml: " + e.getMessage());
        }
    }

    public void checkHighscore() {
        try {
            // Abfrage, um den aktuellen stage-Wert abzurufen
            String query = "SELECT stage FROM highscore";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Überprüfen, ob es Ergebnisse gibt
                int currentStage = resultSet.getInt("stage");

                // Wenn currentQuestionIndex größer ist, aktualisiere den Eintrag
                if (prizeIndex > currentStage) {
                    String updateQuery = "UPDATE highscore SET stage = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, prizeIndex);
                    updateStatement.executeUpdate();

                    System.out.println("Highscore updated to stage: " + currentQuestionIndex);
                } else {
                    System.out.println("Current stage is not higher than the existing highscore.");
                }
            } else {
                System.out.println("No stage found in highscore.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}