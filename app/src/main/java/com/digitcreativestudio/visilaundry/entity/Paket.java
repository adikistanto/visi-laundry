package com.digitcreativestudio.visilaundry.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ADIK on 24/04/2017.
 */

public class Paket implements Parcelable {
    private String nama;
    private String harga;
    private String warna;

    public Paket() {
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getWarna() {
        return warna;
    }

    public void setWarna(String warna) {
        this.warna = warna;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nama);
        dest.writeString(this.harga);
        dest.writeString(this.warna);
    }

    protected Paket(Parcel in) {
        this.nama = in.readString();
        this.harga = in.readString();
        this.warna = in.readString();
    }

    public static final Parcelable.Creator<Paket> CREATOR = new Parcelable.Creator<Paket>() {
        @Override
        public Paket createFromParcel(Parcel source) {
            return new Paket(source);
        }

        @Override
        public Paket[] newArray(int size) {
            return new Paket[size];
        }
    };
}
