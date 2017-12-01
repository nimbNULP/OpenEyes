package com.example.andriy.openeyes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListPlace extends AppCompatActivity implements View.OnClickListener ,
        NavigationView.OnNavigationItemSelectedListener {
    FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    ArrayList<Place> arrayPlace = new ArrayList();
    ListView listOfPlace;
    Button addNewPlace;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user=mAuth.getCurrentUser();
    TextView text, buttonSignIn, adressTextComfortablePlace , describeTextComfortablePlace;
    View anonim, users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_place);
        setTitle("Список місць");
        anonim=getLayoutInflater().inflate(R.layout.nav_header_anonim, null);
        users = getLayoutInflater().inflate(R.layout.nav_header_user, null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addNewPlace=(Button) findViewById(R.id.addNewPlace)  ;
        addNewPlace.setOnClickListener(this);
        listOfPlace=(ListView) findViewById(R.id.listOfPlace);
        getDataFromDatabase();
        listOfPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Place entry= (Place) adapterView.getAdapter().getItem(i);
               Intent intent=new Intent(getApplicationContext(),InformationComfortablePlace.class);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateUI(user);
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
        getMenuInflater().inflate(R.menu.list_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.showMap:
                startActivity(new Intent(getBaseContext(), ListPlaceMap.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.menuComfortablePlace:
                Intent intent=new Intent(ListPlace.this, ListPlace.class);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addNewPlace:
               Intent intent =new Intent(ListPlace.this, AddNewComfotablePlace.class);
                startActivity(intent);
            break;
            case R.id.buttonSignIn:
                goToLogin(view);
                break;
        }
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(getBaseContext(), LoginPage.class);
        startActivity(intent);
    }

    public void goToRegistration(View view) {
        Intent intent = new Intent(getBaseContext(), RegistrationPage.class);
        startActivity(intent);
    }

    private void getDataFromDatabase() {

        dataBase.collection("place")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                arrayPlace.add(document.toObject(Place.class));
                            }
                            PlaceAdapter placeAdapter = new PlaceAdapter(ListPlace.this, arrayPlace);
                            listOfPlace.setAdapter(placeAdapter);
                        } else {
                            Log.d("Database", "Error getting documents: ", task.getException());
                        }
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




}
