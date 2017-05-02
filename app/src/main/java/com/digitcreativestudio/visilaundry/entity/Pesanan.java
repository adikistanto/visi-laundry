package com.digitcreativestudio.visilaundry.entity;

/**
 * Created by ADIK on 20/04/2017.
 */

public class Pesanan {

    private int id_pesan;
    private String nama_paket;
    private int harga;
    private int jumlah;
    private int total_harga;
    private String tgl_pesan;
    private String status;

    public Pesanan() {
    }

    public int getId_pesan() {
        return id_pesan;
    }

    public void setId_pesan(int id_pesan) {
        this.id_pesan = id_pesan;
    }

    public String getNama_paket() {
        return nama_paket;
    }

    public void setNama_paket(String nama_paket) {
        this.nama_paket = nama_paket;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
    }

    public String getTgl_pesan() {
        return tgl_pesan;
    }

    public void setTgl_pesan(String tgl_pesan) {
        this.tgl_pesan = tgl_pesan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
