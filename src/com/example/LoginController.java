package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button buttoncancel;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    private Stage stage;
    private Scene scene;

    @FXML
    private void cancelButtonOnAction(ActionEvent e) {
        Stage stage = (Stage) buttoncancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loginButtonOnAction(ActionEvent e) {
        if (!usernameTextField.getText().isBlank() && !passwordPasswordField.getText().isBlank()) {
            validateLogin();
        } else {
            loginMessageLabel.setText("Silakan Masukan Username dan Password Dengan Benar!!");
        }
    }

    private void validateLogin() {
        String username = usernameTextField.getText();
        String password = passwordPasswordField.getText();
        User user = UserDAO.validate(username, password);

        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainlayout.fxml"));
                Parent root = loader.load();

                MainController mainController = loader.getController();
                mainController.setUser(user);

                stage = (Stage) loginMessageLabel.getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            loginMessageLabel.setText("Username atau Password Salah!");
        }
    }
}
