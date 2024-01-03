package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ThreeFragment extends Fragment {
    public RecyclerView recyclerView;
    public RecyclerView weatherRecyclerView;
    public TodoAdapter adapter;
    public WeatherAdapter weatherAdapter;
    private ArrayList<TodoItem> todoList = new ArrayList<>();
    private ArrayList<WeatherItem> weatherList = new ArrayList<>();
    private static final String TODO_PREFS = "todoPrefs";

    // open weather 설정
    EditText etCity;
    TextView currWeather, treatMethod;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "c52827cc3e964279095d372436481fe2";
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new TodoAdapter(todoList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setTodoList(todoList);

        loadTodoFromSharedPreferences();

        weatherRecyclerView = view.findViewById(R.id.weatherRecyclerView);
        weatherAdapter = new WeatherAdapter(weatherList);

        weatherRecyclerView.setAdapter(weatherAdapter);
        weatherRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));

        weatherAdapter.setWeatherList(weatherList);

        EditText addTodo = view.findViewById(R.id.addTodo);
        Button insertButton = view.findViewById(R.id.insertBtn);
        Button weatherButton = view.findViewById(R.id.weather_click);

        // 기상 맞는지 확인 요망 ///
        etCity = view.findViewById(R.id.etCity);
        currWeather = view.findViewById(R.id.currWeather);
        treatMethod = view.findViewById(R.id.treatMethod);
        Button getButton = view.findViewById(R.id.btnGet);

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherAdapter.notifyDataSetChanged();
            }
        });
        // weather 버튼
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherDetails(v);
            }
        });

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TodoItem newTodo = new TodoItem(addTodo.getText().toString());
                todoList.add(newTodo);
                adapter.notifyDataSetChanged();
                saveTodoAndUpdateSharedPreferences();
                addTodo.setText(null);
            }
        });
        return view;
    }

    public void getWeatherDetails(View view) {
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
//        String country = etCountry.getText().toString().trim();
        if (city.equals("")) {
            treatMethod.setText("City field can not be empty");
        } else {
            tempUrl = url + "?q=" + city + "," + "&appid=" + appid;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Log.d("response", response);
                String output = "";
                String weatherIcon = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArrayWeather = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                    String main_weather = jsonObjectWeather.getString("main"); ///////////////////
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    int humidity = jsonObjectMain.getInt("humidity");
                    float pressure = jsonObjectMain.getInt("pressure");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");

                    // 글자 색깔 설정
                    treatMethod.setTextColor(Color.rgb(68, 134, 199));


                    if (main_weather.equals("Clear")) {
                        output = "Provide water and sunlight but avoid overexposure.";
                        weatherIcon = "☀️";
                    } else if (main_weather.equals("Clouds") || main_weather.equals("Atmosphere")) {
                        output = "Use artificial light and reduce watering.";
                        weatherIcon = "☁️";
                    } else if (main_weather.equals("Drizzle") || main_weather.equals("Rain")) {
                        output = "Ensure ventilation to prevent root rot.";
                        weatherIcon = "\uD83C\uDF27️️️";
                    } else if (main_weather.equals("Snow")) {
                        output = "Remove accumulated snow to prevent damage.";
                        weatherIcon = "\uD83C\uDF28️";
                    } else if (main_weather.equals("Thunderstrom")) {
                        output = "Bring plants indoors for protection.";
                        weatherIcon = "⛈️";
                    } else {
                        output = "Use artificial light and reduce watering.";
                        weatherIcon = "☁️";
                    }

                    currWeather.setText("Today weather is " + main_weather + weatherIcon);
                    treatMethod.setText(output);

                    ArrayList<WeatherItem> newWeatherList = new ArrayList<>(weatherList);
                    newWeatherList.add(new WeatherItem("Today", main_weather, (int) Math.round(temp) + "°C", R.drawable.weather_cloud));
                    newWeatherList.add(new WeatherItem("Tomorrow", "Sunny", "4°C", R.drawable.weather_sunny));
                    newWeatherList.add(new WeatherItem("Friday", "Rainy", "10°C", R.drawable.weather_rainy));
                    newWeatherList.add(new WeatherItem("Saturday", "Snowy", "5°C", R.drawable.weather_snowy));
                    newWeatherList.add(new WeatherItem("Sunday", "Thunderstorm", "1°C", R.drawable.weather_thunderstorm));
                    newWeatherList.add(new WeatherItem("Monday", "Sunny", "3°C", R.drawable.weather_sunny));
                    newWeatherList.add(new WeatherItem("Tuesday", "Cloudy", "4°C", R.drawable.weather_cloud));

                    newWeatherList.addAll(weatherList);
                    weatherAdapter.setWeatherList(newWeatherList);
                    weatherAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity().getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        saveTodoToSharedPreferences();
    }

    private void saveTodoToSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(TODO_PREFS, requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String todojson = gson.toJson(todoList);

        editor.putString("todo", todojson);
        editor.apply();
    }

    private void loadTodoFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(TODO_PREFS, Context.MODE_PRIVATE);
        String todoJson = sharedPreferences.getString("todo", "");

        if (!todoJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<TodoItem>>() {}.getType();
            todoList = gson.fromJson(todoJson, listType);
            adapter.setTodoList(todoList);
            adapter.notifyDataSetChanged();
        }
    }

    private void saveTodoAndUpdateSharedPreferences() {
        saveTodoToSharedPreferences();
        adapter.notifyDataSetChanged();
    }
}