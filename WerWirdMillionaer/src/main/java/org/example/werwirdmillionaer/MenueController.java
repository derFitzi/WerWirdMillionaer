package org.example.werwirdmillionaer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenueController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}