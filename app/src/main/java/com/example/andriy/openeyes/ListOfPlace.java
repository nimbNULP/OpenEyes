package com.example.andriy.openeyes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListOfPlace extends AppCompatActivity implements View.OnClickListener{

    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();

    Button addButton;
    ArrayList<Place> arrayPlace = new ArrayList();
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_place);
        getDataFromDatabase();
        text = (TextView) findViewById(R.id.text1);
        addButton=(Button) findViewById(R.id.goToAddPlace);
        addButton.setOnClickListener(this);

    }


    private void getDataFromDatabase() {

        DocumentReference docRef = dataBase.collection("place").document("Аптека");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               Place place = documentSnapshot.toObject(Place.class);
                text.setText(place.describe);
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.goToAddPlace:
                goToAddPlaceActivity();
        }
    }

    private void  goToAddPlaceActivity(){
        Intent intent= new Intent(ListOfPlace.this, AddComfortablePlace.class);
        startActivity(intent);
    }
}


