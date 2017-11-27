package com.example.andriy.openeyes;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.acl.LastOwnerException;
import java.util.ArrayList;


public class TabComentComfortablePlace extends Fragment {
    ListView listReviews;
    String name;
    ReviewAdapter reviewAdapter;
    ArrayList<Review> arrayReviews=new ArrayList<Review>();
    public TabComentComfortablePlace(){}

    @SuppressLint("ValidFragment")
    public TabComentComfortablePlace(String setName){
        name=setName;
        Log.d("tab", name);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_tab_coment_comfortable_place, container, false);
       listReviews=(ListView) view.findViewById(R.id.listReviews) ;
        getComentFromDataBase();

        return  view;
    }

    public void getComentFromDataBase(){
        Log.d("tab", name);
        FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
        dataBase.collection("place").document(name).collection("review")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("tab", document.toObject(Review.class).getTheme());
                                arrayReviews.add(document.toObject(Review.class));
                            }
                            listReviews.setAdapter(new ReviewAdapter(getActivity(),arrayReviews));

                        } else {
                            Log.d("Database", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}
