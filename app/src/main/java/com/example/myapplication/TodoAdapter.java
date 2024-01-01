package com.example.myapplication;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private ArrayList<TodoItem> mData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView todoName;
        protected Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            todoName = itemView.findViewById(R.id.todoItem);
            deleteButton = itemView.findViewById(R.id.deleteBtn);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // 현재 어댑터가 다루고 있는 리스트의 포지션을 가져옴
                    if(position != RecyclerView.NO_POSITION){ // 삭제된 포지션이 아닌 경우
                        mData.remove(position); // 해당 포지션의 item을 제거
                        notifyItemRemoved(position); // 어댑터에게 데이터셋이 변경되었음을 알림
                    }
                }
            });
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음
    TodoAdapter(ArrayList<TodoItem> list){
        mData = list;
    }

    // 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_todo, parent, false);
        TodoAdapter.ViewHolder viewHolder = new TodoAdapter.ViewHolder(view);

        return viewHolder;
    }

    // position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.todoName.setText(mData.get(position).getTodoName()); // 직접적으로 binding
    }

    // 전체 데이터 갯수 리턴
    @Override
    public int getItemCount() {
        return mData.size();
    }
}
