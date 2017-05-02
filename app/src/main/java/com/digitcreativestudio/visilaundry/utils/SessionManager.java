package com.digitcreativestudio.visilaundry.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.digitcreativestudio.visilaundry.LoginActivity;

import java.util.HashMap;

/**
 * Created by ADIK on 20/04/2017.
 */

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "VisiLaundryPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_NAMA_LENGKAP = "nama_lengkap";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ALAMAT = "alamat";
    public static final String KEY_NO_TELP = "id_pengguna";
    public static final String KEY_HARGA_PAKET_JAKET = "Jaket";
    public static final String KEY_HARGA_PAKET_KEMEJA_PANJANG = "Kemeja Panjang";
    public static final String KEY_HARGA_PAKET_KEMEJA_PENDEK = "Kemeja Pendek";
    public static final String KEY_HARGA_PAKET_CELANA_PANJANG = "Celana Panjang";


    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createLoginSession( String id, String nama_lengkap, String username, String email, String alamat, String no_telp){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_NAMA_LENGKAP, nama_lengkap);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ALAMAT, alamat);
        editor.putString(KEY_NO_TELP, no_telp);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));
        user.put(KEY_NAMA_LENGKAP, pref.getString(KEY_NAMA_LENGKAP, null));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_ALAMAT, pref.getString(KEY_ALAMAT, null));
        user.put(KEY_NO_TELP, pref.getString(KEY_NO_TELP, null));
        return user;
    }


    public String getIdPengguna(){
        return pref.getString(KEY_USER_ID,null);
    }


    public void logoutUser(){
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}