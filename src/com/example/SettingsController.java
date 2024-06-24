package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class SettingsController {

    @FXML
    private PasswordField password_baru;

    @FXML
    private PasswordField ulangi_password;

    @FXML 
    private Button kirimpasswordbaru_button;

    @FXML
    private void kirimpasswordbaruButtonOnAction() {
        String newPassword = password_baru.getText();
        String repeatPassword = ulangi_password.getText();

        if (newPassword.equals(repeatPassword)) {
            User user = MainController.getInstance().getUser();
            if (user != null) {
                user.setPassword(newPassword);
                UserDAO.updatePassword(user);
                showAlert(AlertType.INFORMATION, "Password Berhasil Berubah", "Password telah berhasil diperbarui.");
            }
        } else {
            showAlert(AlertType.ERROR, "Password Tidak Sesuai", "Password tidak cocok. Silakan coba lagi.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
