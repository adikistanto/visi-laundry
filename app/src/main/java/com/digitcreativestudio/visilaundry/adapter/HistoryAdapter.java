package com.digitcreativestudio.visilaundry.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitcreativestudio.visilaundry.entity.Pesanan;
import com.digitcreativestudio.visilaundry.R;
import com.digitcreativestudio.visilaundry.utils.Utils;
import com.digitcreativestudio.visilaundry.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADIK on 20/04/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private ArrayList<Pesanan> arrayList;
    private Context context;
    private int pos;

    public HistoryAdapter() {
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nama_paket,harga_paket,jumlah,total_harga,waktu_pesan;
        private ImageView image_batal;

        public MyViewHolder(View view) {
            super(view);
            nama_paket = (TextView) view.findViewById(R.id.nama_paket);
            harga_paket = (TextView) view.findViewById(R.id.harga_paket);
            jumlah = (TextView) view.findViewById(R.id.jumlah);
            total_harga = (TextView) view.findViewById(R.id.total_harga);
            waktu_pesan = (TextView) view.findViewById(R.id.waktu_pesan);
            image_batal = (ImageView) view.findViewById(R.id.image_batal);
        }



        @Override
        public void onClick(View v) {
        }
    }


    public HistoryAdapter(Context context, ArrayList<Pesanan> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Pesanan data = arrayList.get(position);
        holder.nama_paket.setText(data.getNama_paket());
        holder.harga_paket.setText(": "+ NumberFormat.getCurrencyInstance().format(data.getHarga()));
        holder.total_harga.setText(": "+ NumberFormat.getCurrencyInstance().format(data.getTotal_harga()));
        holder.jumlah.setText(": "+data.getJumlah());
        holder.waktu_pesan.setText(": "+data.getTgl_pesan());
        holder.image_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusDialog(data.getId_pesan());
                pos = position;

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void Hapus(final int id_pesan){
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.show();
        RequestQueue rq = Volley.newRequestQueue(context);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constant.HAPUS_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            parseJSON(response);
                        }catch (JSONException e){
                            Utils.createAlertDialog(context,"Pembatalan Gagal","Pembatalan gagal silahkan coba lagi",false);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.createAlertDialog(context,"Pembatalan Gagal","Pembatalan gagal silahkan persiksa koneksi internet",false);
                        dialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Constant.TAG_ID_PESAN, String.valueOf(id_pesan));

                return params;
            }
        };
        rq.add(postRequest);
    }

    private void parseJSON(String response) throws JSONException{
        JSONObject jObject  = new JSONObject(response);

        int success = jObject.getInt(Constant.TAG_SUCCESS);
        if(success==1) {
            arrayList.remove(pos);
            notifyDataSetChanged();
            Toast.makeText(context,"Berhasil membatalakan pesanan", Toast.LENGTH_SHORT).show();
        }else{
            Utils.createAlertDialog(context,"Pembatalan Gagal","Pembatalan gagal silahkan coba lagi",false);
        }
    }

    public void hapusDialog(final int id_pesan) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Batalkan Pesanan");
        alertDialog.setMessage("Apakah Anda yakin akan membatalkan pesanan ini ?");
        alertDialog.setButton2("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Hapus(id_pesan);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
