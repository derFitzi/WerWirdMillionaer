package org.example.werwirdmillionaer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    private Label questionLabel;
    @FXML
    private Button option1Button;
    @FXML
    private Button option2Button;
    @FXML
    private Button option3Button;
    @FXML
    private Button option4Button;

    private int currentQuestionIndex = 0;
    private int prize = 0;
    private int difficultyLevel = 1;
    private Connection connection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Initializing GameController...");
        connectToDatabase();
        if (connection != null) {
            System.out.println("Database connection established.");
            loadNextQuestion();
        } else {
            System.out.println("Database connection failed.");
            setDatabaseConnectionFailed();
        }
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/who_wants_to_be_a_millionaire", "root", "root");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadNextQuestion() {
        if (connection == null) {
            throw new IllegalStateException("Database connection is not established.");
        }

        try {
            String query = "SELECT * FROM Questions WHERE Difficulty = ? LIMIT 1 OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, difficultyLevel);
            statement.setInt(2, currentQuestionIndex);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                setQuestionAndOptions(resultSet);
            } else {
                setNoMoreQuestions();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setQuestionAndOptions(ResultSet resultSet) throws SQLException {
        String question = resultSet.getString("Question");
        String option1 = resultSet.getString("AnswerOption1");
        String option2 = resultSet.getString("AnswerOption2");
        String option3 = resultSet.getString("AnswerOption3");
        String option4 = resultSet.getString("AnswerOption4");

        questionLabel.setText(question);
        option1Button.setText(option1);
        option2Button.setText(option2);
        option3Button.setText(option3);
        option4Button.setText(option4);

        // Print to console for verification
        System.out.println("Question: " + question);
        System.out.println("1: " + option1);
        System.out.println("2: " + option2);
        System.out.println("3: " + option3);
        System.out.println("4: " + option4);
    }

    private void setNoMoreQuestions() {
        questionLabel.setText("No more questions available.");
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
        option4Button.setDisable(true);
    }

    private void setDatabaseConnectionFailed() {
        questionLabel.setText("Database connection failed.");
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
        option4Button.setDisable(true);
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
            String query = "SELECT Solution FROM Questions WHERE Difficulty = ? LIMIT 1 OFFSET ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, difficultyLevel);
            statement.setInt(2, currentQuestionIndex);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int correctAnswer = resultSet.getInt("Solution");
                if (selectedOption == correctAnswer) {
                    handleCorrectAnswer();
                } else {
                    handleIncorrectAnswer();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleCorrectAnswer() {
        prize += 1000; // Increment prize
        currentQuestionIndex++;
        if (currentQuestionIndex % 4 == 0) {
            difficultyLevel++;
        }
        loadNextQuestion();
    }

    private void handleIncorrectAnswer() {
        questionLabel.setText("Incorrect answer. Game over.");
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
        option4Button.setDisable(true);
    }
}
