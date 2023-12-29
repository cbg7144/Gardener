package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>{
    private ArrayList<ContactItem> contactList;

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

    public void setContactList(ArrayList<ContactItem> contactList){
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    // 아이템 개수 조회
    @Override
    public int getItemCount() {
        if(contactList == null)
            return 0;
        else
            return contactList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profile;
        TextView name;
        TextView number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
        }

        void onBind(ContactItem contactItem){
            profile.setImageResource(contactItem.getResourceId());
            name.setText(contactItem.getName());
            number.setText(contactItem.getNumber());
        }
    }
}
