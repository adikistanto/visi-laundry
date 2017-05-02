package com.digitcreativestudio.visilaundry.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digitcreativestudio.visilaundry.entity.Paket;
import com.digitcreativestudio.visilaundry.PesanFragment;
import com.digitcreativestudio.visilaundry.R;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by ADIK on 20/04/2017.
 */

public class PaketAdapter extends RecyclerView.Adapter<PaketAdapter.MyViewHolder> {

    private ArrayList<Paket> arrayList;
    private Context context;
    private FragmentManager manager;
    private int pos;

    public PaketAdapter() {
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nama_paket,harga_paket;
        private RelativeLayout item;

        public MyViewHolder(View view) {
            super(view);
            nama_paket = (TextView) view.findViewById(R.id.nama_paket);
            harga_paket = (TextView) view.findViewById(R.id.harga_paket);
            item = (RelativeLayout) view.findViewById(R.id.item_paket);

        }



        @Override
        public void onClick(View v) {
        }
    }


    public PaketAdapter(Context context, ArrayList<Paket> arrayList,FragmentManager manager) {
        this.context = context;
        this.arrayList = arrayList;
        this.manager = manager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_paket, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Paket data = arrayList.get(position);
        holder.nama_paket.setText(data.getNama());
        holder.harga_paket.setText(""+ NumberFormat.getCurrencyInstance().format(Integer.parseInt(data.getHarga())));
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
