package com.example.myapplication;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment{

    View v;
    // Usage GridAdapter , need to implement
    GridAdapter adapter;
    List<Gallery>  lstGallery;


    public TwoFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_two, container, false);
        GridView gridView = v.findViewById(R.id.gridView);
        adapter = new GridAdapter(v.getContext(), lstGallery);
        gridView.setAdapter(adapter);
        return v;
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstGallery = new ArrayList<>();
        lstGallery.add(new Gallery(R.drawable.one));
        lstGallery.add(new Gallery(R.drawable.two));
        lstGallery.add(new Gallery(R.drawable.three));
        lstGallery.add(new Gallery(R.drawable.four));
        lstGallery.add(new Gallery(R.drawable.five));
        lstGallery.add(new Gallery(R.drawable.six));
        lstGallery.add(new Gallery(R.drawable.seven));
        lstGallery.add(new Gallery(R.drawable.eight));
        lstGallery.add(new Gallery(R.drawable.nine));
    }

}
