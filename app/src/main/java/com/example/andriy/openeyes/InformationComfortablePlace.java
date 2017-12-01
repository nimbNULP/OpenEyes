package com.example.andriy.openeyes;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class InformationComfortablePlace extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    Fragment fragmentInformation, fragmentReviews;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    FragmentTransaction fragmentTransaction;
    String adress, describe, name, latitude, longitude, category;
    TabLayout tabLayout;
    ArrayList<Review> arrayReviews;
    FirebaseFirestore dataBase= FirebaseFirestore.getInstance();
    ReviewAdapter reviewAdapter;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user;
    View anonim, users;
    Button addButton;
    boolean isHaveToilet, isHaveElevator, isHaveRamp, isHaveButtonHelp, isHaveSwaddingTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_comfortable_place);
        anonim=getLayoutInflater().inflate(R.layout.nav_header_anonim, null);
        users=getLayoutInflater().inflate(R.layout.nav_header_user,null);
        user=mAuth.getCurrentUser();
        updateUI(user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        tabLayout= (TabLayout)findViewById(R.id.tabLayoutInformationComfortablePlace);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: showInformation();
                    break;
                    case 1:showReviews();
                    break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        name=intent.getStringExtra("name");
        adress=intent.getStringExtra("adress");
        describe=intent.getStringExtra("describe");
        latitude=intent.getStringExtra("latitude");
        longitude=intent.getStringExtra("longitude");
        category=intent.getStringExtra("category");
        isHaveButtonHelp=intent.getBooleanExtra("isHaveButtonHelp",false);
        isHaveToilet=intent.getBooleanExtra("isHaveToilet",false);
        isHaveElevator=intent.getBooleanExtra("isHaveElevator",false);
        isHaveRamp=intent.getBooleanExtra("isHaveRamp",false);
        isHaveSwaddingTable=intent.getBooleanExtra("isHaveSwaddingTable",false);
        addButton=(Button) findViewById(R.id.addButton);
        addButton.setVisibility(View.INVISIBLE);
        setTitle(intent.getStringExtra("name"));
        final ImageView logoPlace=(ImageView) findViewById(R.id.logoInformationPlace) ;
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(intent.getStringExtra("name")+".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                logoPlace.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
        fragmentInformation= new TabInfrormationComfortablePlace(adress,describe,category,isHaveToilet,
                isHaveElevator,isHaveRamp, isHaveButtonHelp, isHaveSwaddingTable);
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContent,fragmentInformation);
        fragmentTransaction.commit();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void setInformation(View view) {

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
        getMenuInflater().inflate(R.menu.information_comfortable_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menuComfortablePlace:
                Intent intent=new Intent(getBaseContext(), ListPlace.class);
                startActivity(intent);
                break;
            case R.id.nav_exit:
                exitUser();
                break;
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

    public void goToNavigation(View view){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+latitude+", "+longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
    public  void goToLogin(View view){
        Intent intent =new Intent(getBaseContext(), LoginPage.class);
        startActivity(intent);
    }
    public  void goToRegistration(View view){
        Intent intent =new Intent(getBaseContext(), RegistrationPage.class);
        startActivity(intent);
    }



    public void showInformation(){
        addButton.setVisibility(View.INVISIBLE);
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentInformation= new TabInfrormationComfortablePlace(adress,
                describe,category,isHaveToilet,isHaveElevator,isHaveRamp,
                isHaveButtonHelp, isHaveSwaddingTable);
        fragmentTransaction.replace(R.id.fragmentContent,fragmentInformation);
        fragmentTransaction.commit();
    }

    public void showReviews(){
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentReviews= new TabComentComfortablePlace(name);
        fragmentTransaction.replace(R.id.fragmentContent, fragmentReviews);
        fragmentTransaction.commit();
        addButton.setVisibility(View.VISIBLE);
        Log.d("tag", "good");
    }

    public void confirmFireMissiles(View view) {
        DialogFragment newFragment = new AddCommentPlace(name);
        newFragment.show(getFragmentManager(),"Comment");

    }



    public void updateUI(FirebaseUser user){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(user!=null) {
            navigationView.addHeaderView(users);
            GetUserInformation getInformation = new GetUserInformation();
            TextView nameText = (TextView) users.findViewById(R.id.userName);
            ImageView userAvatar=(ImageView) users.findViewById(R.id.userAvatar);
            getInformation.getInformation(user.getEmail(), nameText, userAvatar);
            ((TextView) users.findViewById(R.id.userEmail)).setText(user.getEmail());
        }else{

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
