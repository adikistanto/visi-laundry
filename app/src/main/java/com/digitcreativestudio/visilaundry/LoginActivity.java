package com.digitcreativestudio.visilaundry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitcreativestudio.visilaundry.utils.Constant;
import com.digitcreativestudio.visilaundry.utils.SessionManager;
import com.digitcreativestudio.visilaundry.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText email,password;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        Button login = (Button) findViewById(R.id.button_login);
        TextView daftar = (TextView) findViewById(R.id.daftar_text);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(this);
        daftar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button_login){
            if((!email.getText().toString().isEmpty()) && (!password.getText().toString().isEmpty())){
                String email_str = email.getText().toString();
                String password_str = password.getText().toString();
                Login(email_str,password_str);
                //sessionManager.createLoginSession("","","",email_str,"","");
            }else {
                Utils.createAlertDialog(this,"Login gagal","Silahkan isi email dan password dengan benar",false);
            }
        }else if(view.getId()==R.id.daftar_text){
            startActivity(new Intent(this,RegistrasiActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }

    private void Login(final String email, final String password){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        RequestQueue rq = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constant.LOGIN_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        Log.v("login respon",response);
                        try {
                            parseJSON(response);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Utils.createAlertDialog(LoginActivity.this,"Login Gagal","Silahkan coba lagi",false);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Utils.createAlertDialog(LoginActivity.this,"Login Gagal","Silahkan periksa koneksi intenet",false);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Constant.TAG_EMAIL, email);
                params.put(Constant.TAG_PASSWORD, password);

                return params;
            }
        };
        rq.add(postRequest);
    }

    private void parseJSON(String response) throws JSONException{
        JSONObject jObject  = new JSONObject(response);

        int success = jObject.getInt(Constant.TAG_SUCCESS);
        if(success==1) {
            JSONObject data = jObject.getJSONObject(Constant.TAG_USER);

            String id_pengguna = data.getString(Constant.TAG_ID_PENGGUNA);
            String nama_lengkap = data.getString(Constant.TAG_NAMA_LENGKAP);
            String username = data.getString(Constant.TAG_USERNAME);
            String email = data.getString(Constant.TAG_EMAIL);
            String no_telp = data.getString(Constant.TAG_NO_TELP);
            String alamat = data.getString(Constant.TAG_ALAMAT);

            sessionManager.createLoginSession(id_pengguna,nama_lengkap,username,email,alamat,no_telp);
            startActivity(new Intent(LoginActivity.this,DrawerActivity.class));
            finish();
        }else{
            Utils.createAlertDialog(this,"Login Gagal","Pasangan email dan password tidak cocok",false);
        }
    }
}
