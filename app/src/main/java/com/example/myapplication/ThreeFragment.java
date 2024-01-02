package com.example.myapplication;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ThreeFragment extends Fragment {
    public RecyclerView recyclerView;
    public TodoAdapter adapter;
    private ArrayList<TodoItem> todoList = new ArrayList<>();


    // open weather 설정
    EditText etCity, etCountry;
    TextView tvResult;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "c52827cc3e964279095d372436481fe2";
    DecimalFormat df = new DecimalFormat("#.##");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(adapter);

        EditText addTodo = view.findViewById(R.id.addTodo);
        Button insertButton = view.findViewById(R.id.insertBtn);

        // 기상 맞는지 확인 요망 ///
        etCity = view.findViewById(R.id.etCity);
        tvResult = view.findViewById(R.id.tvResult);
        Button getButton = view.findViewById(R.id.btnGet);

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
                addTodo.setText(null);
            }
        });
        return view;
    }

    public void getWeatherDetails(View view){
        String tempUrl = "";
        String city = etCity.getText().toString().trim();
//        String country = etCountry.getText().toString().trim();
        if(city.equals("")){
            tvResult.setText("City field can not be empty");
        } else {
            tempUrl = url + "?q=" + city + "," + "&appid=" + appid;
        }
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                   // Log.d("response", response);
                    String output = "";
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
                        tvResult.setTextColor(Color.rgb(68,134,199));


//                        output += "Current weather of " + cityName + " (" + countryName + ")"
//                                + "\n Temp: " + df.format(temp) + " °C"
//                                + "\n Feels Like: " + df.format(feelsLike) + " °C"
//                                + "\n Humidity: " + humidity + "%"
//                                + "\n Description: " + main_weather
//                                + "\n Wind Speed: " + wind + "m/s"
//                                + "\n Cloudiness: " + clouds + "%"
//                                + "\n Pressure: " + pressure + "hPa";

                        if( main_weather == "Clear"){
                            output = "빛을 받으시게";
                        } else if(main_weather.equals("Clouds") ||main_weather.equals("Atmosphere")){
                            output = "빛이 부족";
                        } else if(main_weather.equals("Drizzle") || main_weather.equals("Rain")){
                            output = "물을 받아서 행복";
                        } else if(main_weather.equals("Snow")){
                            output = "눈 쌓이면 제거 요망";
                        } else if(main_weather.equals("Thunderstrom")){
                            output = "안으로 집어넣으세요";
                        } else{
                            output = "잘 가꾸어 주세요";
                        }

                        tvResult.setText(output);

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

}