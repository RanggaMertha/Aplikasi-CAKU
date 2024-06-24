package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

public class ClientController {

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button pemasukan_btn;

    @FXML
    private Button setting_btn;

    @FXML
    private Button keluar_btn;

    @FXML
    private void dashboardButtonOnAction() {
        loadPage("dashboard.fxml");
    }

    @FXML
    private void pemasukanButtonOnAction() {
        loadPage("pemasukan.fxml");
    }

    @FXML
    private void settingButtonOnAction() {
        loadPage("settings.fxml");
    }

    @FXML
    void keluarButtonOnAction() {
        System.exit(0); // Example action, modify as needed
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            AnchorPane maincontent = MainController.getInstance().getMaincontent();
            if (maincontent != null) {
                maincontent.getChildren().setAll(root);
            } else {
                System.err.println("maincontent is null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}