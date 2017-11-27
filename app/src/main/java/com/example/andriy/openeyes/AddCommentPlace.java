package com.example.andriy.openeyes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;



public class AddCommentPlace extends DialogFragment {

    FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    String name;
    EditText setTheme,setText;
    RatingBar setRating;

    public AddCommentPlace(){}

    @SuppressLint("ValidFragment")
    public AddCommentPlace(String setName){
        name=setName;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
          View view=inflater.inflate(R.layout.fragment_add_comment_place, null);
          setTheme=(EditText) view.findViewById(R.id.setTheme);
          setText=(EditText) view.findViewById(R.id.setText);
          setRating=(RatingBar) view.findViewById(R.id.setRating);

        builder.setView(view)
                             .setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Log.d("dialog", setText.getText().toString());
                        if (checkEditText(setText) &&checkEditText(setTheme)&&setRating.getRating()!=0) {
                                dataBase.collection("place")
                                        .document(name).collection("review").document(setTheme.getText().toString())
                                        .set(new Review(setTheme.getText().toString(), setText.getText().toString(), String.valueOf(setRating.getRating())))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                               Toast.makeText(getActivity(),"Коментар додано", Toast.LENGTH_SHORT);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(),"Помилка запису данних", Toast.LENGTH_SHORT);
                                            }
                                        });

                        }else if(setRating.getRating()==0){
                            Toast.makeText(getActivity(),"Ви не поставили рейтинг", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddCommentPlace.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
    private boolean checkEditText(EditText editText){
        if (editText.getText().length() == 0) {
            Toast.makeText(getActivity(),"Заповніть усі поля", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
}