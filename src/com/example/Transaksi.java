package com.example;

import java.time.LocalDate;
import javafx.beans.property.*;

public class Transaksi {
    private final StringProperty tujuan;
    private final StringProperty kategori;
    private final DoubleProperty jumlah;
    private final ObjectProperty<LocalDate> tanggal;

    public Transaksi(String tujuan, String kategori, double jumlah, LocalDate tanggal) {
        this.tujuan = new SimpleStringProperty(tujuan);
        this.kategori = new SimpleStringProperty(kategori);
        this.jumlah = new SimpleDoubleProperty(jumlah);
        this.tanggal = new SimpleObjectProperty<>(tanggal);
    }

    public String getTujuan() {
        return tujuan.get();
    }

    public void setTujuan(String tujuan) {
        this.tujuan.set(tujuan);
    }

    public StringProperty tujuanProperty() {
        return tujuan;
    }

    public String getKategori() {
        return kategori.get();
    }

    public void setKategori(String kategori) {
        this.kategori.set(kategori);
    }

    public StringProperty kategoriProperty() {
        return kategori;
    }

    public double getJumlah() {
        return jumlah.get();
    }

    public void setJumlah(double jumlah) {
        this.jumlah.set(jumlah);
    }

    public DoubleProperty jumlahProperty() {
        return jumlah;
    }

    public LocalDate getTanggal() {
        return tanggal.get();
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal.set(tanggal);
    }

    public ObjectProperty<LocalDate> tanggalProperty() {
        return tanggal;
    }
}
