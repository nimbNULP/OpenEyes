package com.example.andriy.openeyes;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainMenu extends AppCompatActivity implements View.OnClickListener  {

    private LinearLayout comfortableTransport;
    private LinearLayout comfortablePlace;
    private LinearLayout goodLinks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        comfortablePlace = (LinearLayout) findViewById(R.id.comfortablePlace);
        comfortablePlace.setOnClickListener(this);
        comfortableTransport = (LinearLayout) findViewById(R.id.comfortableTransport);
        comfortableTransport.setOnClickListener(this);
        goodLinks = (LinearLayout) findViewById(R.id.goodLink);
        goodLinks.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comfortablePlace:
                goToComfortablePlace();
                break;
            case R.id.comfortableTransport:
                goToList();
                break;
            case R.id.goodLink:
                goToGoodLink();
                break;
        }
    }

    private void goToComfortablePlace() {
        Intent intent =new Intent(MainMenu.this, ListOfPlace.class);
        startActivity(intent);
    }
    private void goToList(){

    }
    private void goToGoodLink(){

    }


}
