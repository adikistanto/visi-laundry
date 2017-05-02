package com.digitcreativestudio.visilaundry.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.digitcreativestudio.visilaundry.entity.Paket;
import com.digitcreativestudio.visilaundry.PesanFragment;
import com.digitcreativestudio.visilaundry.R;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by ADIK on 24/04/2017.
 */

public class PaketGridAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<Paket> pakets =new ArrayList<>();
    private static LayoutInflater inflater = null;


    public PaketGridAdapter(Activity a, ArrayList<Paket> d) {
        activity = a; pakets = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return pakets.size();
    }
    public Object getItem(int position) {
        return pakets.get(position);
    }
    public long getItemId(int position) {
        return position;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(activity);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.list_item_paket_grid, null);

            // set value into textview
            TextView nama = (TextView) gridView.findViewById(R.id.nama_paket);
            TextView harga = (TextView) gridView.findViewById(R.id.harga_paket);
            final Paket paket = pakets.get(position);
            nama.setText(paket.getNama());
            harga.setText(""+ NumberFormat.getCurrencyInstance().format(Integer.parseInt(paket.getHarga())));
        } else {
            gridView = (View) convertView;
        }

        return gridView;


    }
}
