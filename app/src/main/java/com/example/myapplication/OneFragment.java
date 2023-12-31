package com.example.myapplication;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

public class OneFragment extends Fragment {
    public RecyclerView recyclerView;
    public ContactAdapter adapter;
    public ArrayList<ContactItem> contactList = new ArrayList<>();
    public ArrayList<ContactItem> searchList = new ArrayList<>(); // 검색 시 같은 이름이 있는 아이템이 담길 리스트

    private static final String CONTACT_PREFS = "ContactPrefs";

    @Override // 뷰 객체가 반환된 직후에 호출, 뷰가 완전히 생성되었음을 보장
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        EditText search = view.findViewById(R.id.searchContact);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });

        adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                ContactItem clickedItem = contactList.get(position);
                showEditContactDialog(clickedItem);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override // 텍스트가 변경될 때마다 호출
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchList.clear();
                String searchName = search.getText().toString().toLowerCase();

                if (searchName.length() > 0) {
                    for (int i = 0; i < contactList.size(); i++) {
                        String contactName = contactList.get(i).getName().toLowerCase();
                        if (contactName.contains(searchName)) {
                            searchList.add(contactList.get(i));
                        }
                    }
                    adapter.setContactList(searchList);
                } else {
                    adapter.setContactList(contactList);
                }
                adapter.notifyDataSetChanged();
            }

            @Override // 텍스트가 변경되기 전에 호출
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // 텍스트가 변경된 후에 호출
            public void afterTextChanged(Editable s) {
            }
        });
    }
    @Override // 프래그먼트 뷰 초기화, 정상적으로 초기화가 되면 뷰 객체 반환
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new ContactAdapter();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        loadContactsFromSharedPreferences();
        adapter.setContactList(contactList);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                ContactItem contactItem = contactList.get(position);
                adapter.notifyItemRemoved(position);
                removeFromSharedPreferences(contactItem);
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

                    float cornerRadius = convertDpToPx(8); // 8dp를 픽셀로 변환
                    RectF background = new RectF(
                            (float) itemView.getRight() + dX - cornerRadius,
                            (float) itemView.getTop(),
                            (float) itemView.getRight(),
                            (float) itemView.getBottom()
                    );
                    c.drawRoundRect(background, cornerRadius, cornerRadius, paint);

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
    public void onPause() {
        super.onPause();
        saveContactsToSharedPreferences();
    }

    private void saveContactsToSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(CONTACT_PREFS, requireContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String contactsjson = gson.toJson(contactList);

        editor.putString("contacts", contactsjson);
        editor.apply();
    }

    private void loadContactsFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(CONTACT_PREFS, Context.MODE_PRIVATE);
        String contactsJson = sharedPreferences.getString("contacts", "");

        if (!contactsJson.isEmpty()) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ContactItem>>() {}.getType();
            contactList = gson.fromJson(contactsJson, listType);
            adapter.setContactList(contactList);
            adapter.notifyDataSetChanged();
        }
    }

    private int convertDpToPx(int dp){
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void showAddContactDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_custom_dialog, null);
        EditText editTextName = dialogView.findViewById(R.id.addName);
        EditText editTextPhoneNumber = dialogView.findViewById(R.id.addNumber);


        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Add Contact")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editTextName.getText().toString();
                        String phoneNumber = editTextPhoneNumber.getText().toString();

                        ContactItem contactItem = new ContactItem(name, phoneNumber);
                        contactList.add(contactItem);

                        saveContactsAndUpdateSharedPreferences();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void showEditContactDialog(ContactItem clickedItem) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.activity_custom_dialog, null);
        EditText editTextName = dialogView.findViewById(R.id.addName);
        EditText editTextPhoneNumber = dialogView.findViewById(R.id.addNumber);

        editTextName.setText(clickedItem.getName());
        editTextPhoneNumber.setText(clickedItem.getPhone_number());

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Edit Contact")
                .setView(dialogView)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editTextName.getText().toString();
                        String phoneNumber = editTextPhoneNumber.getText().toString();

                        clickedItem.setName(name);
                        clickedItem.setPhone_number(phoneNumber);

                        adapter.notifyDataSetChanged();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        contactList.remove(clickedItem);
                        adapter.notifyDataSetChanged();
                        removeFromSharedPreferences(clickedItem);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void saveContactsAndUpdateSharedPreferences() {
        saveContactsToSharedPreferences();
        adapter.notifyDataSetChanged();
    }

    private void removeFromSharedPreferences(ContactItem removedItem) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(CONTACT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String contactsJson = sharedPreferences.getString("contacts", "");
        Type listType = new TypeToken<ArrayList<ContactItem>>() {}.getType();

        if (!contactsJson.isEmpty()) {
            ArrayList<ContactItem> contactListFromPrefs = gson.fromJson(contactsJson, listType);

            // 삭제된 아이템을 찾아 제거
            boolean removed = contactListFromPrefs.removeIf(item -> item.getName().equals(removedItem.getName()) && item.getPhone_number().equals(removedItem.getPhone_number()));

            if (removed) {
                // 수정된 연락처 목록을 SharedPreferences에 저장
                String updatedContactsJson = gson.toJson(contactListFromPrefs);
                editor.putString("contacts", updatedContactsJson);
                editor.apply();

                // 저장된 목록을 새로 불러와서 갱신
                loadContactsFromSharedPreferences();
            }
        }
    }

}