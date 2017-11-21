package com.example.andriy.openeyes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class AddNewComfotablePlace extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {


        FirebaseStorage storage=FirebaseStorage.getInstance();
        RatingBar ratingPlace;
        ImageView addImage;
        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
        EditText addNamePlace, addDescribePlace, addAdressPlace;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_new_comfotable_place);

            ratingPlace=(RatingBar) findViewById(R.id.ratingPlace);
            addImage=(ImageView) findViewById(R.id.addImage);
            addImage.setOnClickListener(this);
            addNamePlace=(EditText) findViewById(R.id.addNamePlace);
            addAdressPlace=(EditText) findViewById(R.id.addAdressPlace);
            addDescribePlace=(EditText) findViewById(R.id.addDescribePlace);




            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);



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
            getMenuInflater().inflate(R.menu.add_new_comfotable_place, menu);
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
                    Intent intent=new Intent(AddNewComfotablePlace.this, ListPlace.class);
                    startActivity(intent);
                    break;
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }


        private boolean checkEditText(EditText editText){
            if (editText.getText().length() == 0) {
                Toast.makeText(AddNewComfotablePlace.this,"Заповніть усі поля", Toast.LENGTH_SHORT).show();
                return false;
            }else {
                return true;
            }
        }

       public void addDataToDataBase(View view){
            if (checkEditText(addNamePlace) &&checkEditText(addAdressPlace)&&checkEditText(addDescribePlace)) {

                dataBase.collection("place")
                        .document(addNamePlace.getText().toString())
                        .set(new Place(addNamePlace.getText().toString(),addDescribePlace.getText().toString(), addAdressPlace.getText().toString(), getRatingPlace()))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                uploadImageToDataBase();
                                Toast.makeText(AddNewComfotablePlace.this,"Дані успішно записані", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewComfotablePlace.this,"Помилка запису данних", Toast.LENGTH_SHORT);
                            }
                        });
            }
        }

        private float getRatingPlace(){
           float valueRatingPlace=1;
           if (ratingPlace.getRating()<=1.5){
               valueRatingPlace=1;  // this place is very bad
           }else if(ratingPlace.getRating()<=3.5){
               valueRatingPlace=2;  // this place is normal
           }else {
               valueRatingPlace=3;  // this place is good
           }

           return valueRatingPlace;
        };


        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.addImage:
                    Intent getImage = new Intent(Intent.ACTION_PICK);
                    getImage.setType("image/*");
                    startActivityForResult(getImage, 1);
                    break;

            }
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent getImage) {
        super.onActivityResult(requestCode, resultCode, getImage);
        Bitmap logoImage=null ;
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri SelectedImage = getImage.getData();
                try {
                    logoImage = MediaStore.Images.Media.getBitmap(getContentResolver(), SelectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addImage.setImageBitmap(logoImage);
            }
        }
    }


    public  void uploadImageToDataBase(){
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

// Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child(addNamePlace.getText().toString()+".jpg");

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("place/"+addNamePlace.getText().toString()+".jpg");

// While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
        addImage.setDrawingCacheEnabled(true);
        addImage.buildDrawingCache();
        Bitmap bitmap = addImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }


    }

