package com.example.andriy.openeyes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.v4.app.FragmentActivity;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;




public class TabMapPlace extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    ListView listOfPlace;
    Place place = new Place();
    GoogleMap map;
    MapView mapView;
    BitmapDescriptor icon;
    boolean gasStation, farmace, shop, cafe, hospital, culturePlace, other, filture;


    public TabMapPlace() {
        filture = false;
    }

    public TabMapPlace(boolean setGasStation, boolean setFarmace,
                       boolean setShop, boolean setCafe, boolean setHospital, boolean setCulturePlace, boolean setOther) {
        gasStation = setGasStation;
        farmace = setFarmace;
        shop = setShop;
        cafe = setCafe;
        hospital = setHospital;
        culturePlace = setCulturePlace;
        other = setOther;
        filture = true;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_map_place, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(this);
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


        return view;
}
    public void getPlaceFromDataBase() {
        dataBase.collection("place")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                place = document.toObject(Place.class);
                                switch (place.getCategory()){
                                    case "Магазини":
                                        if(place.getRatingPlace()==1){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.bad_shop);
                                        }else if(place.getRatingPlace()==2){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.normal_shop);
                                        } else{
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_shop);
                                        }
                                        break;
                                    case "АЗС":
                                        if(place.getRatingPlace()==1){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.bad_gasstation);
                                        }else if(place.getRatingPlace()==2){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.normal_gasstation);
                                        } else{
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_gasstation);
                                        }
                                        break;
                                    case "Аптека":
                                        if(place.getRatingPlace()==1){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.bad_pharmacy);
                                        }else if(place.getRatingPlace()==2){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.normal_pharmacy);
                                        } else{
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_pharmacy);
                                        }
                                        break;
                                    case "Лікарня":
                                        if(place.getRatingPlace()==1){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.bad_hospital);
                                        }else if(place.getRatingPlace()==2){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.normal_hospital);
                                        } else{
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_hospital);
                                        } break;
                                    case "Заклади харчування":
                                        if(place.getRatingPlace()==1){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.bad_cafe);
                                        }else if(place.getRatingPlace()==2){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.normal_cafe);
                                        } else{
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_cafe);
                                        } break;
                                    case "Культурні місця":
                                        if(place.getRatingPlace()==1){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.bad_culturalplace);
                                        }else if(place.getRatingPlace()==2){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.normal_culturalplace);
                                        } else{
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_culturalplace);
                                        } break;
                                    case "Інше":
                                        if(place.getRatingPlace()==1){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.bad_sthelse);
                                        }else if(place.getRatingPlace()==2){
                                            icon= BitmapDescriptorFactory.fromResource(R.drawable.normal_sthelse);
                                        } else{
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_sthelse);
                                        } break;
                                };

                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(place.getLatitude(), place.getLongitude()))
                                        .title(place.name)
                                        .snippet(place.describe))
                                        .setIcon(icon);




                            }
                        } else {
                            Log.d("Database", "Error getting documents: ", task.getException());
                        }
                    }
                });

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
                                place = document.toObject(Place.class);
                                switch (place.getCategory()) {
                                    case "АЗС":
                                        if (place.getRatingPlace() == 1) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.bad_gasstation);
                                        } else if (place.getRatingPlace() == 2) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.normal_gasstation);
                                        } else {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_gasstation);
                                        }
                                        break;
                                    case "Аптека":
                                        if (place.getRatingPlace() == 1) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.bad_pharmacy);
                                        } else if (place.getRatingPlace() == 2) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.normal_pharmacy);
                                        } else {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_pharmacy);
                                        }
                                        break;
                                    case "Лікарня":
                                        if (place.getRatingPlace() == 1) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.bad_hospital);
                                        } else if (place.getRatingPlace() == 2) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.normal_hospital);
                                        } else {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_hospital);
                                        }
                                        break;
                                    case "Магазини":
                                        if (place.getRatingPlace() == 1) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.bad_shop);
                                        } else if (place.getRatingPlace() == 2) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.normal_shop);
                                        } else {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_shop);
                                        }
                                        break;
                                    case "Заклади харчування":
                                        if (place.getRatingPlace() == 1) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.bad_cafe);
                                        } else if (place.getRatingPlace() == 2) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.normal_cafe);
                                        } else {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_cafe);
                                        }
                                        break;
                                    case "Культурні місця":
                                        if (place.getRatingPlace() == 1) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.bad_culturalplace);
                                        } else if (place.getRatingPlace() == 2) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.normal_culturalplace);
                                        } else {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_culturalplace);
                                        }
                                        break;
                                    case "Інше":
                                        if (place.getRatingPlace() == 1) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.bad_sthelse);
                                        } else if (place.getRatingPlace() == 2) {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.normal_sthelse);
                                        } else {
                                            icon = BitmapDescriptorFactory.fromResource(R.drawable.good_sthelse);
                                        }
                                        break;
                                }
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(place.getLatitude(), place.getLongitude()))
                                        .title(place.name)
                                        .snippet(place.describe))
                                        .setIcon(icon);
                            }
                        }else {
                            Log.d("Database", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng lviv = new LatLng(49.842133, 24.027282);
        map = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lviv, 10));
        googleMap.setOnInfoWindowClickListener(this);


    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        goToInformationPlace(marker.getTitle());
    }
    public void goToInformationPlace(String title) {
        dataBase.collection("place").document(title).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                place=documentSnapshot.toObject(Place.class);
                Intent intent=new Intent(getActivity(), InformationComfortablePlace.class);
                intent.putExtra("name", place.getName());
                intent.putExtra("describe", place.getDescribe());
                intent.putExtra("adress", place.getAdress());
                intent.putExtra("latitude", String.valueOf(place.getLatitude()));
                intent.putExtra("longitude", String.valueOf(place.getLongitude()));
                intent.putExtra("category", place.getCategory());
                intent.putExtra("isHaveElevator", place.isHaveElevator());
                intent.putExtra("isHaveToilet", place.isHaveToilet());
                intent.putExtra("isHaveRamp", place.isHaveRamp());
                intent.putExtra("isHaveSwaddingTable", place.isHaveSwaddingTable());
                intent.putExtra("isHaveButtonHelp", place.isHaveButtonHelp());
                startActivity(intent);
            }
        });
    }
}
