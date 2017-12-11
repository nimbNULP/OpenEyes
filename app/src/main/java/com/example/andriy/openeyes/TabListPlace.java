package com.example.andriy.openeyes;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TabListPlace extends Fragment {

    FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    ArrayList<Place> arrayPlace=new ArrayList<Place>();
    ListView listOfPlace;
    PlaceAdapter adapter;
    boolean  gasStation, farmace, shop, cafe, hospital, culturePlace,  other,filture;


    public  TabListPlace(){
        filture=false;
    }
    public  TabListPlace( boolean setGasStation, boolean setFarmace,
                         boolean setShop, boolean setCafe, boolean setHospital, boolean setCulturePlace, boolean setOther ){
        gasStation=setGasStation;
        farmace=setFarmace;
        shop=setShop;
        cafe=setCafe;
        hospital=setHospital;
        culturePlace=setCulturePlace;
        other=setOther;
        filture=true;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_list_place, container, false);
        listOfPlace=(ListView) view.findViewById(R.id.listOfPlace);
        adapter=new PlaceAdapter(getActivity(),arrayPlace);
        adapter.clear();
        adapter.notifyDataSetChanged();
            if (!filture) {
                getPlaceFromDataBase();
            } else {
                if (gasStation) {
                    getFilturedPlaceFromDataBase("АЗС");
                }
                if (farmace) {
                    getFilturedPlaceFromDataBase("Аптека");
                }
                if (shop) {
                    getFilturedPlaceFromDataBase("Магазини");
                }
                if (hospital) {
                    getFilturedPlaceFromDataBase("Лікарня");
                }
                if (culturePlace) {
                    getFilturedPlaceFromDataBase("Культурні місця");
                }
                if (other) {
                    getFilturedPlaceFromDataBase("Інше");
                }

            }

        listOfPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Place entry= (Place) adapterView.getAdapter().getItem(i);
                Intent intent=new Intent(getActivity(),InformationComfortablePlace.class);
                intent.putExtra("name", entry.getName());
                intent.putExtra("describe", entry.getDescribe());
                intent.putExtra("adress", entry.getAdress());
                intent.putExtra("latitude", String.valueOf(entry.getLatitude()));
                intent.putExtra("longitude", String.valueOf(entry.getLongitude()));
                intent.putExtra("category", entry.getCategory());
                intent.putExtra("isHaveElevator", entry.isHaveElevator());
                intent.putExtra("isHaveToilet", entry.isHaveToilet());
                intent.putExtra("isHaveRamp", entry.isHaveRamp());
                intent.putExtra("isHaveSwaddingTable", entry.isHaveSwaddingTable());
                intent.putExtra("isHaveButtonHelp", entry.isHaveButtonHelp());
                startActivity(intent);

            }
        });


        return view;
    }
    public void getFilturedPlaceFromDataBase( String filture){
        dataBase.collection("place")
                .whereEqualTo("category", filture)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                arrayPlace.add(document.toObject(Place.class));
                            }
                            adapter=new PlaceAdapter(getActivity(), arrayPlace);
                            listOfPlace.setAdapter(adapter);
                        } else {
                            Log.d("Database", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
    public void getPlaceFromDataBase( ){
        dataBase.collection("place")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                arrayPlace.add(document.toObject(Place.class));
                            }
                            adapter=new PlaceAdapter(getActivity(), arrayPlace);
                            listOfPlace.setAdapter(adapter);
                        } else {
                            Log.d("Database", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    public void setFilture(){
        this.adapter.clear();
        this.adapter.notifyDataSetChanged();
    }




}
