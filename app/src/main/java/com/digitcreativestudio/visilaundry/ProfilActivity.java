package com.digitcreativestudio.visilaundry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.digitcreativestudio.visilaundry.utils.SessionManager;

import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetails();
        String nama_lengkap = user.get(SessionManager.KEY_NAMA_LENGKAP);
        String username = user.get(SessionManager.KEY_USERNAME);
        String email = user.get(SessionManager.KEY_EMAIL);
        String alamat = user.get(SessionManager.KEY_ALAMAT);
        String no_telp = user.get(SessionManager.KEY_NO_TELP);

        TextView nama_tv = (TextView) findViewById(R.id.nama_lengkap);
        TextView user_tv = (TextView) findViewById(R.id.username);
        TextView email_tv = (TextView) findViewById(R.id.email);
        TextView alamat_tv = (TextView) findViewById(R.id.alamat);
        TextView notelp_tv = (TextView) findViewById(R.id.no_telp);

        nama_tv.setText(nama_lengkap);
        user_tv.setText(username);
        email_tv.setText(email);
        alamat_tv.setText(alamat);
        notelp_tv.setText(no_telp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            int SUCCESS_RESULT=1;
            setResult(SUCCESS_RESULT, new Intent());
            finish();  //return to caller
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int SUCCESS_RESULT=1;
        setResult(SUCCESS_RESULT, new Intent());
        finish();  //return to caller
    }
}
