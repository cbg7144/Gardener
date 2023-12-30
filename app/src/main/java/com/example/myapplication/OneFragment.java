package com.example.myapplication;

import android.content.ClipData;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class OneFragment extends Fragment implements CustomDialog.OnContactAddedListener {
    public RecyclerView recyclerView;
    public ContactAdapter adapter;
    public ArrayList<ContactItem> contactList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog();
                customDialog.setOnContactAddedListener(OneFragment.this);
                customDialog.show(getParentFragmentManager(), "CustomDialog");
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new ContactAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Gson gson = new Gson();

        try {
            InputStream inputStream = getActivity().getAssets().open("contact.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];

            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, "UTF-8");

            JsonElement jsonElement = JsonParser.parseString(json);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray personArray = jsonObject.getAsJsonArray("person");

                Type listType = new TypeToken<ArrayList<ContactItem>>() {}.getType();
                ArrayList<ContactItem> contactItems = gson.fromJson(personArray, listType);

                contactList.addAll(contactItems);
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        // Adapter에 데이터 설정
        adapter.setContactList(contactList);
        adapter.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                contactList.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildDraw(
                    @NonNull Canvas c,
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    float dX,
                    float dY,
                    int actionState,
                    boolean isCurrentlyActive
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(50);
                    paint.setAntiAlias(true);

                    // 버튼 배경
                    RectF background = new RectF(
                            (float) itemView.getRight() + dX,
                            (float) itemView.getTop(),
                            (float) itemView.getRight(),
                            (float) itemView.getBottom()
                    );
                    c.drawRect(background, paint);

                    // 버튼 텍스트
                    paint.setColor(Color.WHITE);
                    c.drawText("삭제", (float) itemView.getRight() - 200, (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop()) / 2 + 15, paint);
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.5f; // 스와이프 이동 거리의 50% 이상을 스와이프하여 버튼이 나타나도록 설정
            }

            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT); // 왼쪽으로만 스와이프하도록 설정
            }


        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onContactAdded(ContactItem contactItem) {
        contactList.add(contactItem);
        adapter.setContactList(contactList);
        adapter.notifyDataSetChanged();
    }
}