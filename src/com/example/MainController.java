package com.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private AnchorPane maincontent;

    @FXML
    private Label usernameLabel;

    private User user;

    private static MainController instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
    }

    public void setUser(User user) {
        this.user = user;
        updateUIWithUserDetails();
    }

    private void updateUIWithUserDetails() {
        if (user != null && usernameLabel != null) {
            usernameLabel.setText("Welcome, " + user.getUsername());
        }
    }

    public static MainController getInstance() {
        return instance;
    }

    public AnchorPane getMaincontent() {
        return maincontent;
    }

    public User getUser() {
        return user;
    }
}
