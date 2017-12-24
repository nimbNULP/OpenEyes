package com.example.andriy.openeyes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
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
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.PublicKey;

public class ListComfortablePlace extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Fragment listPlace, mapPlace;
    FragmentTransaction fragmentTransaction;
    TabLayout tabLayout;
    TabItem tabList, tabMap;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user=mAuth.getCurrentUser();
    View anonim, users;
    NavigationView navigationView;
    AlertDialog.Builder ad;
    Context context;
    boolean map;
    boolean[] mCheckedItems = {true,true,true,true,true,true,true};
    String[] checkPlaceName = {"АЗС", "Аптеки", "Магазини","Заклади харчування","Лікарні","Культурні місця","Інше"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_comfortable_place);
        setTitle("Зручні місця");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tabList=(TabItem) findViewById(R.id.tabShowList);
        tabMap=(TabItem) findViewById(R.id.tabShowMap);
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
        listPlace= new TabListPlace();
        mapPlace= new TabMapPlace();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:showMap();
                        break;
                    case 1:showList();
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
        map=true;
        listPlace= new TabListPlace();
        mapPlace=new TabMapPlace();
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragmentContentListPlace,mapPlace);
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




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
            Intent intent=new Intent();
            switch(id){
                case R.id.menuComfortablePlace:
                    intent=new Intent(getBaseContext(), ListComfortablePlace.class);
                    startActivity(intent);
                    break;
                case R.id.menuComfortableTransport:
                    intent=new Intent(getBaseContext(), ComfortableTransport.class);
                    startActivity(intent);
                    break;
                case R.id.navAboutUs:
                    intent=new Intent(getBaseContext(), AboutUs.class);
                    startActivity(intent);
                    break;
                case  R.id.navGoodLink:
                    intent=new Intent(getBaseContext(), GoodLink.class);
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
        map=false;
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContentListPlace,listPlace);
        fragmentTransaction.commit();
    }
    public void showMap(){
        map=true;
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContentListPlace,mapPlace);
        fragmentTransaction.commit();

    }
    public void updateUI(FirebaseUser user){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(user!=null) {
            navigationView.removeHeaderView(anonim);
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
        if(user!=null) {
            FirebaseAuth.getInstance().signOut();
            user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }
    public  void goToLogin(View view){
        Intent intent =new Intent(getBaseContext(), LoginPage.class);
        startActivityForResult(intent,4);

    }
    public  void goToRegistration(View view){
        Intent intent =new Intent(getBaseContext(), RegistrationPage.class);
        startActivityForResult(intent,4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("update", "OK");
        switch (requestCode) {
            case 4:
                if(resultCode==RESULT_OK) {
                    updateUI(FirebaseAuth.getInstance().getCurrentUser());

                }
                break;
        }

    }
    public  void goToAddPlace(View view){
        if (user!=null) {
            Intent intent = new Intent(getBaseContext(), AddNewComfotablePlace.class);
            startActivity(intent);
        }else
        {
            Toast.makeText(getBaseContext(),"Для додавання місця потрібно авторизуватись", Toast.LENGTH_SHORT).show();
        }
    }
    public void  showFilture(View view){
        onCreateDialog(3);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 3:
                ad = new AlertDialog.Builder(this);

                ad.setTitle("Виберіть категорію")
                        .setCancelable(false)

                        .setMultiChoiceItems(checkPlaceName, mCheckedItems,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which, boolean isChecked) {
                                        mCheckedItems[which] = isChecked;
                                    }
                                })

                        // Добавляем кнопки
                        .setPositiveButton(R.string.apply,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        listPlace=new TabListPlace(mCheckedItems[0],mCheckedItems[1],mCheckedItems[2],mCheckedItems[3],mCheckedItems[4],mCheckedItems[5],mCheckedItems[6]);
                                        mapPlace=new TabMapPlace(mCheckedItems[0],mCheckedItems[1],mCheckedItems[2],mCheckedItems[3],mCheckedItems[4],mCheckedItems[5],mCheckedItems[6]);
                                        if(map) {
                                            fragmentTransaction = getFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.fragmentContentListPlace, mapPlace);
                                            fragmentTransaction.commit();
                                        }
                                        else {
                                            fragmentTransaction = getFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.fragmentContentListPlace, listPlace);
                                            fragmentTransaction.commit();
                                        }
                                    }
                                })

                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();

                                    }
                                });

        }
      
        ad.show();
        return ad.create();
    }





}
