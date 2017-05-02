package com.digitcreativestudio.visilaundry;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.digitcreativestudio.visilaundry.entity.Paket;
import com.digitcreativestudio.visilaundry.utils.Constant;
import com.digitcreativestudio.visilaundry.utils.SessionManager;
import com.digitcreativestudio.visilaundry.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

/**
 * Created by ADIK on 20/04/2017.
 */

public class PesanFragment extends DialogFragment implements View.OnClickListener{

    private MaterialNumberPicker numberPicker;
    private SessionManager sessionManager;

    public static PesanFragment newInstance(Paket paket) {
        PesanFragment frag = new PesanFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constant.PAKET_LAUNDRY, paket);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pesan, container, false);
        Paket paket = getArguments().getParcelable(Constant.PAKET_LAUNDRY);
        getDialog().setTitle(paket.getNama());

        sessionManager = new SessionManager(getActivity());
        numberPicker = (MaterialNumberPicker) v.findViewById(R.id.number);

        Button button_pesan = (Button) v.findViewById(R.id.button_pesan);
        Button button_batal = (Button) v.findViewById(R.id.button_batal);

        button_batal.setOnClickListener(this);
        button_pesan.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.button_batal){
            getDialog().dismiss();
        }else if(view.getId()==R.id.button_pesan){
            String jumlah = String.valueOf(numberPicker.getValue());
            String id_pengguna = sessionManager.getIdPengguna();
            Paket paket = getArguments().getParcelable(Constant.PAKET_LAUNDRY);
            if((paket!=null)&&(id_pengguna!=null)&&(jumlah!=null)){
                Pesan(id_pengguna,paket.getNama(),paket.getHarga(),jumlah);
            }else{
                Utils.createAlertDialog(getActivity(),"Pemesanan Gagal","Pemesanan gagal silahkan coba lagi",false);
            }
        }
    }

    private void Pesan(final String id_pengguna,
                            final String nama_barang,
                            final String harga,
                            final String jumlah){
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.show();
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest postRequest = new StringRequest(Request.Method.POST, Constant.PESAN_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        try {
                            parseJSON(response);
                        }catch (JSONException e){
                            e.printStackTrace();
                            Utils.createAlertDialog(getActivity(),"Pemesanan Gagal","Pemesanan gagal silahkan coba lagi",false);
                            getDialog().dismiss();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        Utils.createAlertDialog(getActivity(),"Pemesanan Gagal","Pemesanan gagal silahkan periksa koneksi internet",false);
                        getDialog().dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(Constant.TAG_ID_PENGGUNA, id_pengguna);
                params.put(Constant.TAG_NAMA_PAKET, nama_barang);
                params.put(Constant.TAG_HARGA, harga);
                params.put(Constant.TAG_JUMLAH, jumlah);

                return params;
            }
        };
        rq.add(postRequest);
    }

    private void parseJSON(String response) throws JSONException{
        JSONObject jObject  = new JSONObject(response);

        int success = jObject.getInt(Constant.TAG_SUCCESS);
        if(success==1) {
            Toast.makeText(getActivity(),"Pemesanan sukses",Toast.LENGTH_SHORT).show();
            getDialog().dismiss();
        }else{
            Utils.createAlertDialog(getActivity(),"Pemesanan Gagal","Pemesanan gagal silahkan coba lagi",false);
        }
    }
}
