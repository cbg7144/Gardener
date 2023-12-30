package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerPhoto extends RecyclerView.Adapter<RecyclerPhoto.MyViewHolder> {

    Context mContext;
    List<Gallery> mData;

    public RecyclerPhoto(Context mContext, List<Gallery> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public RecyclerPhoto.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.gallery_grid_item, parent, false);
        final MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerPhoto.MyViewHolder holder, int position) {
        Gallery gallery = mData.get(position%mData.size());
        if(gallery.getPhoto() == -1) { holder.image.setImageBitmap(gallery.getBitmap());}
        else { holder.image.setImageResource(gallery.getPhoto()); }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.grid_image);
        }
    }
}