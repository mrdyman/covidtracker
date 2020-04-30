package com.diman.covidtrackerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;

public class AdapterProvinsi extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataProvinsi> data = Collections.emptyList();
    DataProvinsi current;
    int currentPos=0;
    private OnItemClickCallback onItemClickCallback;

    // buat constructor untuk inisialisasi context dan data yang dikirim dari MainActivity
    public AdapterProvinsi(Context context, List<DataProvinsi> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    //Inflate layout ketika viewholder di create
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_list, parent,false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //ambil posisi dari item pada recyclerview
        final MyHolder myHolder= (MyHolder) holder;
        DataProvinsi current = data.get(position);
        myHolder.textProvinsi.setText(current.namaProvinsi);
        myHolder.textMeninggal.setText("Meninggal: " + current.kasusMeninggal);
        myHolder.textPositif.setText("Total Positif (+): " + current.kasusPositif);
        myHolder.textSembuh.setText("Sembuh. " + current.kasusSembuh + " Orang");
        myHolder.textSembuh.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        myHolder.textMeninggal.setTextColor(ContextCompat.getColor(context, R.color.colorMeninggal));
        myHolder.textPositif.setTextColor(ContextCompat.getColor(context, R.color.colorPositif));

        Glide.with(context).load(R.drawable.avatar)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(myHolder.ivAvatar);

        myHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(data.get(myHolder.getAdapterPosition()));
            }
        });

    }

    // return total item dari list
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder{

        TextView textProvinsi;
        ImageView ivAvatar;
        TextView textMeninggal;
        TextView textPositif;
        TextView textSembuh;

        public MyHolder(View itemView) {
            super(itemView);
            textProvinsi= (TextView) itemView.findViewById(R.id.tvProvinsi);
            ivAvatar= (ImageView) itemView.findViewById(R.id.ivAvatar);
            textMeninggal = (TextView) itemView.findViewById(R.id.tvMeninggal);
            textPositif = (TextView) itemView.findViewById(R.id.tvPositif);
            textSembuh = (TextView) itemView.findViewById(R.id.tvSembuh);
        }

    }

    public void SetOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback{
        void onItemClicked(DataProvinsi data);
    }

}
