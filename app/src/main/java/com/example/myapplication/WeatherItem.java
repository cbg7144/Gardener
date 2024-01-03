package com.example.myapplication;

public class WeatherItem {
    private String date;
    private String detail;
    private String temperature;
    private int weatherIcon; // drawable resource ID for weather icon

    public WeatherItem(String date, String detail, String temperature, int weatherIcon) {
        this.date = date;
        this.detail = detail;
        this.temperature = temperature;
        this.weatherIcon = weatherIcon;
    }

    // Getters and setters for WeatherItem properties
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
}

