package com.digitcreativestudio.visilaundry;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitcreativestudio.visilaundry.entity.Paket;
import com.digitcreativestudio.visilaundry.utils.Constant;
import com.digitcreativestudio.visilaundry.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;

public class Splashcreen extends AppCompatActivity {

    private ArrayList<Paket> pakets = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashcreen);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        ImageView no_internet = (ImageView) findViewById(R.id.no_internet);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        if(isInternetConnected(this)==true){
            Paket();
        }else{
            logo.setVisibility(GONE);
            no_internet.setVisibility(View.VISIBLE);
            progressBar.setVisibility(GONE);
            Utils.createAlertDialog(this,"Koneksi Gagal","Anda tidak sedang terhubung dengan internet",false);
        }
    }

    //cek internet connection
    public static boolean isInternetConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    private void Paket(){

        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.GET, Constant.PAKET_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            parseJSON(response);
                        }catch (JSONException e){
                            Utils.createAlertDialog(Splashcreen.this,"Load Data Gagal","Load data gagal silahkan periksa koneksi internet",false);
                            progressBar.setVisibility(GONE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.createAlertDialog(Splashcreen.this,"Load Data Gagal","Load data gagal silahkan periksa koneksi internet",false);
                        progressBar.setVisibility(GONE);
                    }
                }
        );
        rq.add(postRequest);
    }

    private void parseJSON(String response) throws JSONException{
        JSONObject jObject  = new JSONObject(response);

        int success = jObject.getInt(Constant.TAG_SUCCESS);
        if(success==1) {
            pakets.clear();
            JSONArray produks = jObject.getJSONArray(Constant.TAG_PAKETS);

            for(int i=0;i<produks.length();i++){
                Paket paket = new Paket();
                JSONObject data = (JSONObject) produks.get(i);

                String nama_paket = data.getString(Constant.TAG_NAMA_BARANG);
                String harga = data.getString(Constant.TAG_HARGA_PAKET);
                String warna = data.getString(Constant.TAG_WARNA);

                paket.setNama(nama_paket);
                paket.setHarga(harga);
                paket.setWarna(warna);

                pakets.add(paket);

            }
            if(pakets.size()>0){
                Intent i = new Intent(Splashcreen.this, DrawerActivity.class);
                i.putParcelableArrayListExtra(Constant.TAG_PAKETS,pakets);
                startActivity(i);
                finish();
            }else {
                progressBar.setVisibility(GONE);
                Utils.createAlertDialog(this,"Tidak Ada Paket","Tidak terdapat data paket silahkan coba lagi nanti",false);
            }
        }else{
            progressBar.setVisibility(GONE);
            Utils.createAlertDialog(this,"Load Data Gagal","Load data gagal silahkan coba lagi",false);
        }
    }
}
