package com.digitcreativestudio.visilaundry;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.HashMap;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SessionManager sessionManager;
    private ArrayList<Paket> pakets = new ArrayList<>();
    private ExpandGridView gridview;
    private PaketGridAdapter adapter;
    private Toolbar toolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sessionManager = new SessionManager(this);
        if(sessionManager.isLoggedIn()==false){
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }else {
            setDrawer();
            pakets = getIntent().getParcelableArrayListExtra(Constant.TAG_PAKETS);
            if(pakets!=null){
                showPaket(pakets);
            }else{
                Paket();
            }
        }

    }

    private void setDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setProfile();
    }

    private void setProfile(){

        HashMap<String, String> user = sessionManager.getUserDetails();
        String username = user.get(SessionManager.KEY_USERNAME);
        String email = user.get(SessionManager.KEY_EMAIL);

        View header=navigationView.getHeaderView(0);
        TextView username_tv = (TextView) header.findViewById(R.id.username);
        TextView email_tv = (TextView) header.findViewById(R.id.email);

        username_tv.setText(username);
        email_tv.setText(email);
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
                            Utils.createAlertDialog(DrawerActivity.this,"Load Data Gagal","Load data gagal silahkan persiksa koneksi internet",false);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.createAlertDialog(DrawerActivity.this,"Load Data Gagal","Load data gagal silahkan persiksa koneksi internet",false);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
        super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_history) {
            startActivity(new Intent(this,HistoryActivity.class));
        } else if (id == R.id.action_profil) {
            startActivity(new Intent(this,ProfilActivity.class));
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this,AboutActivity.class));
        } else if (id == R.id.action_logout) {
            sessionManager.logoutUser();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
