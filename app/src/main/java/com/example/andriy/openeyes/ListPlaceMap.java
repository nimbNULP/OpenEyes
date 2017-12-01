package com.example.andriy.openeyes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ListPlaceMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    Place place = new Place();
    GoogleMap map;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user=mAuth.getCurrentUser();
    View anonim, users;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_place_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getDataFromFirebase();
        anonim=getLayoutInflater().inflate(R.layout.nav_header_anonim, null);
        users = getLayoutInflater().inflate(R.layout.nav_header_user, null);
        updateUI(user);
        setTitle("Карта місць");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_place_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.showList:
                startActivity(new Intent(getBaseContext(), ListPlace.class));
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.menuComfortablePlace:
                Intent intent=new Intent(getBaseContext(), ListPlace.class);
                startActivity(intent);
                break;
            case R.id.nav_exit:
                exitUser();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void getDataFromFirebase() {
        dataBase.collection("place")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                place = document.toObject(Place.class);
                                map.addMarker(new MarkerOptions()
                                        .position(new LatLng(place.getLatitude(), place.getLongitude()))
                                        .title(place.name).snippet(place.describe));
                                Log.d("map", place.getName());

                            }
                        } else {
                            Log.d("Database", "Error getting documents: ", task.getException());
                        }
                    }
                });
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
                Intent intent=new Intent(getBaseContext(), InformationComfortablePlace.class);
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
    public void updateUI(FirebaseUser user){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(user!=null) {
            ((Button) findViewById(R.id.addNewPlace)).setVisibility(View.VISIBLE);
            navigationView.addHeaderView(users);
            GetUserInformation getInformation = new GetUserInformation();
            TextView nameText = (TextView) users.findViewById(R.id.userName);
            ImageView userAvatar=(ImageView) users.findViewById(R.id.userAvatar);
            getInformation.getInformation(user.getEmail(), nameText, userAvatar);
            ((TextView) users.findViewById(R.id.userEmail)).setText(user.getEmail());
        }else{
            ((Button) findViewById(R.id.addNewPlace)).setVisibility(View.INVISIBLE);
            navigationView.removeHeaderView(users);
            navigationView.addHeaderView(anonim);
        }

    }
    public void exitUser(){
        FirebaseAuth.getInstance().signOut();
        user=mAuth.getCurrentUser();
        updateUI(user);
    }
    public  void goToLogin(View view){
        Intent intent =new Intent(getBaseContext(), LoginPage.class);
        startActivity(intent);
    }
    public  void goToRegistration(View view){
        Intent intent =new Intent(getBaseContext(), RegistrationPage.class);
        startActivity(intent);
    }
    public  void goToAddPlace(View view){
        Intent intent= new Intent(getBaseContext(), AddNewComfotablePlace.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng lviv = new LatLng(49.842133, 24.027282);
        map = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lviv, 10));
        googleMap.setOnInfoWindowClickListener(this);
    }
}
