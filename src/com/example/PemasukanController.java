package com.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PemasukanController implements Initializable {
    @FXML
    private TextField tujuan_field;
    @FXML
    private TextField jumlah_field;
    @FXML
    private DatePicker tanggal_picker;
    @FXML
    private Button simpan_button;
    @FXML
    private Label pemasukan_label;

    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        simpan_button.setOnAction(this::simpanButtonOnAction);
    }

    @FXML
    private void simpanButtonOnAction(ActionEvent event) {
        tambahPemasukan();
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    private void tambahPemasukan() {
        String tujuan = tujuan_field.getText();
        String jumlahText = jumlah_field.getText();
        LocalDate tanggal = tanggal_picker.getValue();

        // Validasi input
        if (tujuan == null || tujuan.trim().isEmpty() || jumlahText == null || jumlahText.trim().isEmpty()
                || tanggal == null) {
            System.out.println("Input tidak valid");
            return;
        }

        double jumlah;
        try {
            jumlah = Double.parseDouble(jumlahText);
        } catch (NumberFormatException e) {
            System.out.println("Jumlah tidak valid");
            return;
        }

        Transaksi pemasukan = new Transaksi(tujuan, "Pemasukan", jumlah, tanggal);

        savePemasukanToDatabase(pemasukan);

        // Clear input fields
        tujuan_field.clear();
        jumlah_field.clear();
        tanggal_picker.setValue(null);

        pemasukan_label.setText(jumlahText);
    }

    private void savePemasukanToDatabase(Transaksi pemasukan) {
        String sql = "INSERT INTO pemasukan (user_id, tujuan, jumlah, tanggal) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int userId = (currentUser != null) ? currentUser.getId() : 1; // Default user_id = 1

            stmt.setInt(1, userId);
            stmt.setString(2, pemasukan.getTujuan());
            stmt.setDouble(3, pemasukan.getJumlah());
            stmt.setDate(4, java.sql.Date.valueOf(pemasukan.getTanggal()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}