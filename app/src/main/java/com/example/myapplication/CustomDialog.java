package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CustomDialog extends AppCompatDialogFragment {
    private EditText addNameEditText;
    private EditText addNumberEditText;

    public interface OnContactAddedListener {
        void onContactAdded(ContactItem contactItem);
    }

    private OnContactAddedListener onContactAddedListener;
    public void setOnContactAddedListener(OnContactAddedListener onContactAddedListener) {
        this.onContactAddedListener = onContactAddedListener;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        try {
            super.onViewStateRestored(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_custom_dialog, null);

        addNameEditText = view.findViewById(R.id.addName);
        addNumberEditText = view.findViewById(R.id.addNumber);

        builder.setView(view)
                .setTitle("Add New Contact")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })

                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = addNameEditText.getText().toString();
                        String number = addNumberEditText.getText().toString();

                        ContactItem contactItem = new ContactItem(name, number);

                        if(onContactAddedListener != null) {
                            onContactAddedListener.onContactAdded(contactItem);
                        }
                    }
                });

        return builder.create();
    }
}