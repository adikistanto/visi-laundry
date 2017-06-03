package com.digitcreativestudio.visilaundry;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitcreativestudio.visilaundry.adapter.PaketAdapter;
import com.digitcreativestudio.visilaundry.adapter.PaketGridAdapter;
import com.digitcreativestudio.visilaundry.entity.Paket;
import com.digitcreativestudio.visilaundry.utils.Constant;
import com.digitcreativestudio.visilaundry.utils.SessionManager;
import com.digitcreativestudio.visilaundry.utils.Utils;
import com.digitcreativestudio.visilaundry.view.ExpandGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SessionManager sessionManager;
    private ArrayList<Paket> pakets = new ArrayList<>();
    private ExpandGridView gridview;
    private PaketGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);
        if(sessionManager.isLoggedIn()==false){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else {
            pakets = getIntent().getParcelableArrayListExtra(Constant.TAG_PAKETS);
            if(pakets!=null){
                showPaket(pakets);
            }else{
                Paket();
            }
        }




    }

    private void showPaket(final ArrayList<Paket> pakets){
        gridview = (ExpandGridView) findViewById(R.id.gridview);
        adapter = new PaketGridAdapter(this,pakets);
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentManager manager = getSupportFragmentManager();
                PesanFragment dialog = PesanFragment.newInstance(pakets.get(i));
                dialog.show(manager, "dialog");
            }
        });
        gridview.setExpanded(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_history) {
            startActivity(new Intent(this,HistoryActivity.class));
            return true;
        }else if (id == R.id.action_logout) {
            sessionManager.logoutUser();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return true;
        }else if (id == R.id.action_about) {
            startActivity(new Intent(this,AboutActivity.class));
            return true;
        }else if (id == R.id.action_profil) {
            startActivity(new Intent(this,ProfilActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                            Utils.createAlertDialog(MainActivity.this,"Load Data Gagal","Load data gagal silahkan persiksa koneksi internet",false);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.createAlertDialog(MainActivity.this,"Load Data Gagal","Load data gagal silahkan persiksa koneksi internet",false);
                    }
                }
        );
        rq.add(postRequest);
    }

    private void parseJSON(String response) throws JSONException{
        JSONObject jObject  = new JSONObject(response);

        int success = jObject.getInt(Constant.TAG_SUCCESS);
        if(success==1) {
            ArrayList<Paket> pakets = new ArrayList<>();
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
                showPaket(pakets);
            }else {
                Utils.createAlertDialog(this,"Tidak Ada Paket","Tidak terdapat data paket silahkan coba lagi nanti",false);
            }
        }else{
            Utils.createAlertDialog(this,"Load Data Gagal","Load data gagal silahkan coba lagi",false);
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }

}
