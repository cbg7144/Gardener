package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class ThreeFragment extends Fragment {
    public RecyclerView recyclerView;
    public TodoAdapter adapter;
    private ArrayList<TodoItem> todoList = new ArrayList<>();

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
}