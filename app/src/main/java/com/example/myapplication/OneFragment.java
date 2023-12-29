package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class OneFragment extends Fragment {
    public RecyclerView recyclerView;
    public ContactAdapter adapter;
    public ArrayList<ContactItem> contactList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new ContactAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        try {
            InputStream inputStream = getActivity().getAssets().open("contact.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            // JSON 형식의 데이터 파싱
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            // 파싱한 데이터를 ArrayList에 추가
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String phoneNumber = jsonObject.getString("phone_number");
                int imageResource = getResources().getIdentifier(jsonObject.getString("image_resource"), "drawable", getActivity().getPackageName());

                // ContactItem 객체 생성 후 ArrayList에 추가
                contactList.add(new ContactItem(name, phoneNumber, imageResource));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        // Adapter에 데이터 설정
        adapter.setContactList(contactList);
        adapter.notifyDataSetChanged();
        return view;
    }
}