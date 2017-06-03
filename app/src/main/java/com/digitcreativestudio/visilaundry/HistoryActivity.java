package com.digitcreativestudio.visilaundry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitcreativestudio.visilaundry.adapter.HistoryAdapter;
import com.digitcreativestudio.visilaundry.entity.Pesanan;
import com.digitcreativestudio.visilaundry.utils.Constant;
import com.digitcreativestudio.visilaundry.utils.SessionManager;
import com.digitcreativestudio.visilaundry.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private ArrayList<Pesanan> pesanans = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        String id_pengguna = sessionManager.getIdPengguna();
        if(id_pengguna!=null){
            History(id_pengguna);
        }

    }

    private void showKuliner(ArrayList<Pesanan> pesanans){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.history);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        HistoryAdapter adapter = new HistoryAdapter(this, pesanans);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void History(final String id_pengguna){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.show();
        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constant.HISTORY_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            parseJSON(response);
                        }catch (JSONException e){
                            Utils.createAlertDialog(HistoryActivity.this,"History Gagal","Penampilan history gagal silahkan coba lagi",false);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.createAlertDialog(HistoryActivity.this,"History Gagal","Penampilan history gagal silahkan periksa koneksi intenet",false);
                        dialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Constant.TAG_ID_PENGGUNA, id_pengguna);

                return params;
            }
        };
        rq.add(postRequest);
    }

    private void parseJSON(String response) throws JSONException{
        JSONObject jObject  = new JSONObject(response);

        int success = jObject.getInt(Constant.TAG_SUCCESS);
        if(success==1) {
            pesanans.clear();
            JSONArray produks = jObject.getJSONArray(Constant.TAG_PESANS);

            for(int i=0;i<produks.length();i++){
                Pesanan pesanan = new Pesanan();
                JSONObject data = (JSONObject) produks.get(i);
                int id_pesan = data.getInt(Constant.TAG_ID_PESAN);
                String nama_paket = data.getString(Constant.TAG_NAMA_PAKET);
                int harga = data.getInt(Constant.TAG_HARGA);
                int jumlah = data.getInt(Constant.TAG_JUMLAH);
                int total_harga = data.getInt(Constant.TAG_TOTAL_HARGA);

                JSONObject created_at = data.getJSONObject(Constant.TAG_CREATED_AT);
                String tgl_pesan = created_at.getString(Constant.TAG_DATE);

                pesanan.setId_pesan(id_pesan);
                pesanan.setNama_paket(nama_paket);
                pesanan.setHarga(harga);
                pesanan.setJumlah(jumlah);
                pesanan.setTotal_harga(total_harga);
                pesanan.setTgl_pesan(tgl_pesan);

                pesanans.add(pesanan);
            }
            if(pesanans.size()>0){
                showKuliner(pesanans);
            }else {
                Utils.createAlertDialog(this,"Tidak Ada","Anda tidak mempunyai history pemesanan",false);
            }
        }else{
            Utils.createAlertDialog(this,"History Gagal","Penampilan history gagal silahkan coba lagi",false);
        }
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
