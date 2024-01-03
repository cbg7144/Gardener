package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private ArrayList<WeatherItem> weatherDataList;

    public WeatherAdapter(ArrayList<WeatherItem> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

    public void setWeatherList(ArrayList<WeatherItem> weatherList) {
        this.weatherDataList = weatherList;
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        public TextView date, detail, temp;
        public ImageView weatherIcon;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            detail = itemView.findViewById(R.id.detail);
            temp = itemView.findViewById(R.id.temp);
            weatherIcon = itemView.findViewById(R.id.weather_icon);
        }
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        WeatherItem data = weatherDataList.get(position);
        holder.date.setText(data.getDate());
        holder.detail.setText(data.getDetail());
        holder.temp.setText(data.getTemperature());
        holder.weatherIcon.setImageResource(data.getWeatherIcon());
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    public void updateData(ArrayList<WeatherItem> newData) {
        weatherDataList = newData;
        notifyDataSetChanged();
    }
}
