package com.digitcreativestudio.visilaundry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitcreativestudio.visilaundry.utils.Constant;
import com.digitcreativestudio.visilaundry.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegistrasiActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        Button daftar = (Button) findViewById(R.id.button_daftar);
        daftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button_daftar){
            EditText nama_lengkap = (EditText) findViewById(R.id.nama_lengkap);
            EditText username = (EditText) findViewById(R.id.username);
            EditText email = (EditText) findViewById(R.id.email);
            EditText password = (EditText) findViewById(R.id.password);
            EditText password_konfirmasi = (EditText) findViewById(R.id.password_konfirmasi);
            EditText no_telp = (EditText) findViewById(R.id.no_telp);
            EditText alamat = (EditText) findViewById(R.id.alamat);


            if(TextUtils.isEmpty(nama_lengkap.getText().toString())&&
                    TextUtils.isEmpty(username.getText().toString())&&
                    TextUtils.isEmpty(email.getText().toString())&&
                    TextUtils.isEmpty(password.getText().toString())&&
                    TextUtils.isEmpty(password_konfirmasi.getText().toString())&&
                    TextUtils.isEmpty(no_telp.getText().toString())&&
                    TextUtils.isEmpty(alamat.getText().toString()))
            {
                Utils.createAlertDialog(this,"Registrasi Gagal","Silahkan isi formulir dengan benar",false);
            }else {
                if(password.getText().toString().equals(password_konfirmasi.getText().toString())){
                    Registrasi(nama_lengkap.getText().toString(),
                            username.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString(),
                            no_telp.getText().toString(),
                            alamat.getText().toString());
                }else{
                    Utils.createAlertDialog(this,"Registrasi Gagal","Password yang dimasukan tidak sama",false);
                }
            }
        }
    }

    private void Registrasi(final String nama_lengkap,
                            final String username,
                            final String email,
                            final String password,
                            final String no_telp,
                            final String alamat){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constant.REGISTRASI_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            parseJSON(response);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Utils.createAlertDialog(RegistrasiActivity.this,"Registrasi Gagal","Registrasi gagal silahkan coba lagi",false);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Utils.createAlertDialog(RegistrasiActivity.this,"Registrasi Gagal","Registrasi gagal silahkan periksa koneksi internet",false);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Constant.TAG_NAMA_LENGKAP, nama_lengkap);
                params.put(Constant.TAG_USERNAME, username);
                params.put(Constant.TAG_EMAIL, email);
                params.put(Constant.TAG_PASSWORD, password);
                params.put(Constant.TAG_NO_TELP, no_telp);
                params.put(Constant.TAG_ALAMAT, alamat);

                return params;
            }
        };
        rq.add(postRequest);
    }

    private void parseJSON(String response) throws JSONException{
        JSONObject jObject  = new JSONObject(response);

        int success = jObject.getInt(Constant.TAG_SUCCESS);
        if(success==1) {
            Toast.makeText(this,"Registrasi sukses",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegistrasiActivity.this,LoginActivity.class));
            finish();
        }else{
            Utils.createAlertDialog(this,"Registrasi Gagal","Registrasi gagal silahkan coba lagi",false);
        }
    }
}
