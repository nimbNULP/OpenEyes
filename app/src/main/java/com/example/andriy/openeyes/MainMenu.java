package com.example.andriy.openeyes;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainMenu extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }

    public void goToList(View view) {
        Intent intent = new Intent(MainMenu.this, ListComfortablePlace.class);
        startActivity(intent);
    }

    public void goToLink(View view) {
//        Intent intent = new Intent(getApplicationContext(), MapLayout.class);
//        startActivity(intent);
    }
    public void  goToTransport(View view){
        Intent intent = new Intent(getApplicationContext(), ComfortableTransport.class);
        startActivity(intent);
    }



}
