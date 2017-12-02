package com.example.andriy.openeyes;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.PublicKey;

public class ListComfortablePlace extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Fragment listPlace, mapPlace;
    FragmentTransaction fragmentTransaction;
    TabLayout tabLayout;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user=mAuth.getCurrentUser();
    View anonim, users;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comfortable_place);
        setTitle("Зручні місця");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        anonim=getLayoutInflater().inflate(R.layout.nav_header_anonim, null);
        users = getLayoutInflater().inflate(R.layout.nav_header_user, null);
        updateUI(user);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tabLayout=(TabLayout) findViewById(R.id.tabLayoutListPlace);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: showList();
                        break;
                    case 1:showMap();
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
        listPlace= new TabListPlace();
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContentListPlace,listPlace);
        fragmentTransaction.commit();
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
        getMenuInflater().inflate(R.menu.list_comfortable_place, menu);
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
        int id = item.getItemId();
        switch(id){
            case R.id.menuComfortablePlace:
                Intent intent=new Intent(getBaseContext(), ListComfortablePlace.class);
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
    public void showList(){
        fragmentTransaction=getFragmentManager().beginTransaction();
        listPlace= new TabListPlace();
        fragmentTransaction.replace(R.id.fragmentContentListPlace,listPlace);
        fragmentTransaction.commit();
    }
    public void showMap(){
        fragmentTransaction=getFragmentManager().beginTransaction();
        mapPlace= new TabMapPlace();
        fragmentTransaction.replace(R.id.fragmentContentListPlace,mapPlace);
        fragmentTransaction.commit();
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


}
