package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private Text user_name;
    @FXML
    private Label login_date;
    @FXML
    private Label pemasukan_bal;
    @FXML
    private Label belanja_bal;
    @FXML
    private Label makanan_bal;
    @FXML
    private Label tagihan_bal;
    @FXML
    private Label lainnya_bal;
    @FXML
    private TableView<Transaksi> table_view;
    @FXML
    private TableColumn<Transaksi, String> table_tujuan;
    @FXML
    private TableColumn<Transaksi, String> table_kategori;
    @FXML
    private TableColumn<Transaksi, Double> table_jumlah;
    @FXML
    private TableColumn<Transaksi, LocalDate> table_tanggal;
    @FXML
    private TextField tujuan_field;
    @FXML
    private TextField jumlah_field;
    @FXML
    private DatePicker tanggal_picker;
    @FXML
    private RadioButton belanja_button;
    @FXML
    private RadioButton makanan_button;
    @FXML
    private RadioButton tagihan_button;
    @FXML
    private RadioButton lainnya_button;
    @FXML
    private Button kirim_button;

    private ObservableList<Transaksi> transaksiList = FXCollections.observableArrayList();
    private User currentUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login_date.setText(LocalDate.now().toString());

        // Mengatur cell value factory dengan property yang sesuai
        table_tujuan.setCellValueFactory(new PropertyValueFactory<>("tujuan"));
        table_kategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        table_jumlah.setCellValueFactory(new PropertyValueFactory<>("jumlah"));
        table_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));

        // Menghubungkan ObservableList ke TableView
        table_view.setItems(transaksiList);

        // Setup ToggleGroup untuk RadioButtons agar hanya satu yang dapat dipilih
        ToggleGroup categoryGroup = new ToggleGroup();
        belanja_button.setToggleGroup(categoryGroup);
        makanan_button.setToggleGroup(categoryGroup);
        tagihan_button.setToggleGroup(categoryGroup);
        lainnya_button.setToggleGroup(categoryGroup);

        // Set aksi untuk tombol "kirim"
        kirim_button.setOnAction(this::kirimButtonOnAction);

        // Muat data awal dari database
        loadInitialData();
    }

    @FXML
    private void kirimButtonOnAction(ActionEvent event) {
        tambahTransaksi();
    }

    public void setUser(User user) {
        this.currentUser = user;
        user_name.setText("Selamat Datang, " + user.getName());
    }

    private void tambahTransaksi() {
        String tujuan = tujuan_field.getText();
        String jumlahText = jumlah_field.getText();
        LocalDate tanggal = tanggal_picker.getValue();
        String kategori = getSelectedKategori();

        // Validasi input
        if (tujuan == null || tujuan.trim().isEmpty() || jumlahText == null || jumlahText.trim().isEmpty()
                || tanggal == null || kategori == null) {
            System.out.println("Input tidak valid");
            return;
        }

        double jumlah = Double.parseDouble(jumlahText);

        Transaksi transaksi = new Transaksi(tujuan, kategori, jumlah, tanggal);

        transaksiList.add(transaksi);

        saveTransaksiToDatabase(transaksi);
        updateBalances(kategori, jumlah);

        // Clear input fields
        tujuan_field.clear();
        jumlah_field.clear();
        tanggal_picker.setValue(null);
        belanja_button.setSelected(false);
        makanan_button.setSelected(false);
        tagihan_button.setSelected(false);
        lainnya_button.setSelected(false);
    }

    private void saveTransaksiToDatabase(Transaksi transaksi) {
        String sql;
        if (transaksi.getKategori().equals("Pemasukan")) {
            sql = "INSERT INTO pemasukan (user_id, tujuan, jumlah, tanggal) VALUES (?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO pengeluaran (user_id, tujuan, kategori, jumlah, tanggal) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int userId = (currentUser != null) ? currentUser.getId() : 1; // Default user_id = 1

            stmt.setInt(1, userId);
            stmt.setString(2, transaksi.getTujuan());
            stmt.setDouble(3, transaksi.getJumlah());
            stmt.setDate(4, java.sql.Date.valueOf(transaksi.getTanggal()));

            if (!transaksi.getKategori().equals("Pemasukan")) {
                stmt.setString(3, transaksi.getKategori());
                stmt.setDouble(4, transaksi.getJumlah());
                stmt.setDate(5, java.sql.Date.valueOf(transaksi.getTanggal()));
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSelectedKategori() {
        if (belanja_button.isSelected()) {
            return "Belanja";
        } else if (makanan_button.isSelected()) {
            return "Makanan & Minuman";
        } else if (tagihan_button.isSelected()) {
            return "Tagihan";
        } else if (lainnya_button.isSelected()) {
            return "Lainnya";
        } else {
            return null; // Kategori tidak dipilih
        }
    }

    private void updateBalances(String kategori, double jumlah) {
        double currentAmount;
        switch (kategori) {
            case "Belanja":
                currentAmount = Double.parseDouble(belanja_bal.getText());
                belanja_bal.setText(String.valueOf(currentAmount + jumlah));
                break;
            case "Makanan & Minuman":
                currentAmount = Double.parseDouble(makanan_bal.getText());
                makanan_bal.setText(String.valueOf(currentAmount + jumlah));
                break;
            case "Tagihan":
                currentAmount = Double.parseDouble(tagihan_bal.getText());
                tagihan_bal.setText(String.valueOf(currentAmount + jumlah));
                break;
            case "Lainnya":
                currentAmount = Double.parseDouble(lainnya_bal.getText());
                lainnya_bal.setText(String.valueOf(currentAmount + jumlah));
                break;
            case "Pemasukan":
                currentAmount = Double.parseDouble(pemasukan_bal.getText());
                pemasukan_bal.setText(String.valueOf(currentAmount + jumlah));
                break;
        }
        // Jika itu adalah pengeluaran, kurangi jumlah dari pemasukan_bal
        if (!kategori.equals("Pemasukan")) {
            double currentPemasukan = Double.parseDouble(pemasukan_bal.getText());
            pemasukan_bal.setText(String.valueOf(currentPemasukan - jumlah));
        }
    }

    public void addTransaksi(String tujuan, String jumlah, String tanggal) {
        LocalDate localDate = LocalDate.parse(tanggal);
        Transaksi transaksi = new Transaksi(tujuan, "Pemasukan", Double.parseDouble(jumlah), localDate);
        transaksiList.add(transaksi);
        updatePemasukanLabel();
    }

    private void updatePemasukanLabel() {
        double totalPemasukan = transaksiList.stream()
                .filter(transaksi -> transaksi.getKategori().equals("Pemasukan"))
                .mapToDouble(Transaksi::getJumlah)
                .sum();
        pemasukan_bal.setText(String.valueOf(totalPemasukan));
    }

    private void loadInitialData() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Load pemasukan data
            String sqlPemasukan = "SELECT tujuan, jumlah, tanggal FROM pemasukan WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPemasukan)) {
                int userId = (currentUser != null) ? currentUser.getId() : 1; // Default user_id = 1
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String tujuan = rs.getString("tujuan");
                    double jumlah = rs.getDouble("jumlah");
                    LocalDate tanggal = rs.getDate("tanggal").toLocalDate();
                    Transaksi pemasukan = new Transaksi(tujuan, "Pemasukan", jumlah, tanggal);
                    transaksiList.add(pemasukan);
                }
            }

            // Load pengeluaran data
            String sqlPengeluaran = "SELECT tujuan, kategori, jumlah, tanggal FROM pengeluaran WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPengeluaran)) {
                int userId = (currentUser != null) ? currentUser.getId() : 1; // Default user_id = 1
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String tujuan = rs.getString("tujuan");
                    String kategori = rs.getString("kategori");
                    double jumlah = rs.getDouble("jumlah");
                    LocalDate tanggal = rs.getDate("tanggal").toLocalDate();
                    Transaksi pengeluaran = new Transaksi(tujuan, kategori, jumlah, tanggal);
                    transaksiList.add(pengeluaran);
                }
            }

            // Update balances
            updatePemasukanLabel();
            updateBalancesFromDatabase();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBalancesFromDatabase() {
        double totalBelanja = transaksiList.stream()
                .filter(transaksi -> transaksi.getKategori().equals("Belanja"))
                .mapToDouble(Transaksi::getJumlah)
                .sum();
        belanja_bal.setText(String.valueOf(totalBelanja));

        double totalMakanan = transaksiList.stream()
                .filter(transaksi -> transaksi.getKategori().equals("Makanan & Minuman"))
                .mapToDouble(Transaksi::getJumlah)
                .sum();
        makanan_bal.setText(String.valueOf(totalMakanan));

        double totalTagihan = transaksiList.stream()
                .filter(transaksi -> transaksi.getKategori().equals("Tagihan"))
                .mapToDouble(Transaksi::getJumlah)
                .sum();
        tagihan_bal.setText(String.valueOf(totalTagihan));

        double totalLainnya = transaksiList.stream()
                .filter(transaksi -> transaksi.getKategori().equals("Lainnya"))
                .mapToDouble(Transaksi::getJumlah)
                .sum();
        lainnya_bal.setText(String.valueOf(totalLainnya));

        double totalPemasukan = transaksiList.stream()
                .filter(transaksi -> transaksi.getKategori().equals("Pemasukan"))
                .mapToDouble(Transaksi::getJumlah)
                .sum();
        pemasukan_bal.setText(String.valueOf(totalPemasukan));
    }
}
