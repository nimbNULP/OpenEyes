package com.example.andriy.openeyes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class AddPositionPlace extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    Intent intent;
    Marker place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_position_place);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        intent=getIntent();
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng lviv = new LatLng(Double.valueOf(intent.getStringExtra("latitude")), Double.valueOf(intent.getStringExtra("longitude")));
        place=googleMap.addMarker(new MarkerOptions()
                .position(lviv)
                .draggable(true));
        googleMap.setOnMarkerDragListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lviv, 17));
    }
    public void getLatLng(View view){
        LatLng comfortablePlace=place.getPosition();
        intent=new Intent();
        intent.putExtra("latitude", String.valueOf(comfortablePlace.latitude));
        intent.putExtra("longitude", String.valueOf(comfortablePlace.longitude));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
