package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    Context context;
    String[] flowerName;
    int[] image;

    LayoutInflater inflater;

    public GridAdapter(Context context, String[] flowerName, int[] image) {
        this.context = context;
        this.flowerName = flowerName;
        this.image = image;
    }

    @Override
    public int getCount() {
        return flowerName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gallery_grid_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.grid_image);
        TextView textView = convertView.findViewById(R.id.item_name); // Corrected the case

        imageView.setImageResource(image[position]);
        textView.setText(flowerName[position]);

        return convertView; // Corrected to return the view
    }
}
