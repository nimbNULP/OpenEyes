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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    TextView text, buttonSignIn, adressTextComfortablePlace , describeTextComfortablePlace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_place);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.menuComfortablePlace:
                Intent intent=new Intent(ListPlace.this, ListPlace.class);
                startActivity(intent);
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
    public  void goToLogin(View view){
        Intent intent =new Intent(getBaseContext(), LoginPage.class);
        startActivity(intent);
    }
    public  void goToRegistration(View view){
        Intent intent =new Intent(getBaseContext(), RegistrationPage.class);
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




}
