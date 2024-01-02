package com.example.myapplication;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private ArrayList<ContactItem> contactList;

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조 변수
    private OnItemClickListener mListener = null;
    // 리스너 객체 참조를 어댑터에 전달
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }


    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    // 뷰홀더가 재활용될 때 실행되는 메소드
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(contactList.get(position));
    }

    // 데이터 값 갱신
    public void setContactList(ArrayList<ContactItem> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    // 아이템 개수 조회
    @Override
    public int getItemCount() {
        if (contactList == null)
            return 0;
        else
            return contactList.size();
    }

    // 뷰홀더 객체에 저장되어 화면에 표시되고, 필요에 따라 생성 및 재사용
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView number;
        Button btnCall, btnMessage;
        private static final int PERMISSIONS_CALL_PHONE = 1001;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            btnCall = itemView.findViewById(R.id.btn_call);
            btnMessage = itemView.findViewById(R.id.btn_message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(v, pos);
                    }
                }
            });

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        String phoneNumber = contactList.get(pos).getPhone_number();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));

                        if (ContextCompat.checkSelfPermission(itemView.getContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) itemView.getContext(), new String[]{android.Manifest.permission.CALL_PHONE}, PERMISSIONS_CALL_PHONE);
                        } else {
                            itemView.getContext().startActivity(intent);
                        }
                    }
                }
            });

            btnMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        String phoneNumber = contactList.get(pos).getPhone_number();
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("smsto:" + phoneNumber));
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
        }

        void onBind(ContactItem contactItem) {
            name.setText(contactItem.getName());
            number.setText(contactItem.getPhone_number());
        }
    }
}
