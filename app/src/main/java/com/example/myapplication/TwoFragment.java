package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TwoFragment extends Fragment{

    Bitmap image;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        GridView gridView = view.findViewById(R.id.gridView);
        String[] flowerName = {"a", "b", "b", "b", "b", "b", "b", "b", "b"};
        int[] flowerImages = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine};

        // Set up the GridAdapter
        GridAdapter gridAdapter = new GridAdapter(getActivity(), flowerName, flowerImages);
        gridView.setAdapter(gridAdapter);

        // Set up the Item Click Listener
        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            Toast.makeText(getActivity(), "You Click on " + flowerName[position], Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}