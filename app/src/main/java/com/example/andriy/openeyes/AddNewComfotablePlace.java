package com.example.andriy.openeyes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class AddNewComfotablePlace extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    NavigationView navigationView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    RatingBar ratingPlace;
    ImageView addImage;
    FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
    EditText addNamePlace, addDescribePlace, addAdressPlace;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user;
    View anonim, users;
    Boolean position=false;
    double latitude, longitude;
    Spinner categoryPlace;
    CheckBox haveToilet, haveSwaddingTable, haveElevator, haveRamp, haveButtonHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_comfotable_place);
        anonim=getLayoutInflater().inflate(R.layout.nav_header_anonim, null);
        users = getLayoutInflater().inflate(R.layout.nav_header_user, null);
        ratingPlace = (RatingBar) findViewById(R.id.ratingPlace);
        addImage = (ImageView) findViewById(R.id.addImage);
        addImage.setOnClickListener(this);
        addNamePlace = (EditText) findViewById(R.id.addNamePlace);
        addAdressPlace = (EditText) findViewById(R.id.addAdressPlace);
        addDescribePlace = (EditText) findViewById(R.id.addDescribePlace);
        user=mAuth.getCurrentUser();
        updateUI(user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        addSpinner();
        addCheckBoxs();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.menuComfortablePlace:
                Intent intent = new Intent(AddNewComfotablePlace.this, ListPlace.class);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private boolean checkEditText(EditText editText) {
        if (editText.getText().length() == 0) {
            Toast.makeText(AddNewComfotablePlace.this, "Заповніть усі поля", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public void addDataToDataBase(View view) {
        if (checkEditText(addNamePlace) && checkEditText(addAdressPlace) && checkEditText(addDescribePlace)&&checkPosition()) {

            dataBase.collection("place")
                    .document(addNamePlace.getText().toString())
                    .set(new Place(addNamePlace.getText().toString(), addDescribePlace.getText().toString(), addAdressPlace.getText().toString(),
                            getRatingPlace(),latitude,longitude, categoryPlace.getSelectedItem().toString(),haveToilet.isChecked(),
                            haveSwaddingTable.isChecked(),haveRamp.isChecked(),haveElevator.isChecked(),haveButtonHelp.isChecked()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            View toastDone = AddNewComfotablePlace.this.getLayoutInflater().inflate(R.layout.toast_done, null);
                            Toast toast = new Toast(getApplicationContext());
                            toast.setView(toastDone);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.show();
                            Intent intent = new Intent(AddNewComfotablePlace.this, ListPlace.class);
                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddNewComfotablePlace.this, "Помилка запису данних", Toast.LENGTH_SHORT);
                        }
                    });
        }
    }

    private float getRatingPlace() {
        float valueRatingPlace = 1;
        if (ratingPlace.getRating() <= 1.5) {
            valueRatingPlace = 1;  // this place is very bad
        } else if (ratingPlace.getRating() <= 3.5) {
            valueRatingPlace = 2;  // this place is normal
        } else {
            valueRatingPlace = 3;  // this place is good
        }

        return valueRatingPlace;
    }

    ;


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addImage:
                if(addNamePlace.getText().length()!=0) {
                    Intent getImage = new Intent(Intent.ACTION_PICK);
                    getImage.setType("image/*");
                    startActivityForResult(getImage, 1);
                }
                else{
                    Toast.makeText(getBaseContext(),"Введіть назву місця", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       switch (requestCode) {
           case 1 :
               Bitmap logoImage = null;

               if (resultCode == RESULT_OK) {
                   Uri SelectedImage = data.getData();
                   try {
                       logoImage = MediaStore.Images.Media.getBitmap(getContentResolver(), SelectedImage);
                       StorageReference storageRef = storage.getReference();
                       StorageReference mountainsRef = storageRef.child(addNamePlace.getText().toString() + ".jpg");
                       StorageReference mountainImagesRef = storageRef.child("place/" + addNamePlace.getText().toString() + ".jpg");
                       mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                       mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
                       ByteArrayOutputStream baos = new ByteArrayOutputStream();
                       logoImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                       byte[] dataImage = baos.toByteArray();
                       UploadTask uploadTask = mountainsRef.putBytes(dataImage);
                       uploadTask.addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception exception) {
                           }
                       }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                           }
                       });
                       getImageFromFirebase();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   addImage.setImageBitmap(logoImage);
               }
               break;
           case 2:
               if(resultCode==RESULT_OK){

                   longitude=Double.valueOf(data.getStringExtra("longitude"));
                   latitude=Double.valueOf(data.getStringExtra("latitude"));
                   position=true;
               }

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
    public void addLatLngPlace(View view) throws IOException {
        Intent intent = new Intent(getBaseContext(), AddPositionPlace.class);
        Geocoder geocoder = new Geocoder(getBaseContext());
        List<Address> addresses;
        addresses = geocoder.getFromLocationName("Львів"+addAdressPlace.getText().toString(), 1);
        if (addresses.size() > 0) {
           intent.putExtra("latitude", String.valueOf(addresses.get(0).getLatitude()));
            intent.putExtra("longitude", String.valueOf(addresses.get(0).getLongitude()));
        }
        startActivityForResult(intent, 2);
    }
    public boolean checkPosition(){
        if(position){
            return true;
        }else{
            Toast.makeText(getBaseContext(),"Додайте розташування", Toast.LENGTH_SHORT ).show();
            return false;
        }
    }

    private  void addSpinner(){
        categoryPlace=(Spinner) findViewById(R.id.categoryPlace);
        String[] arrayCategory = {"АЗС", "Aптеки", "Магазини","Заклади харчування","Лікарня", "Культурні місця","Інше"};
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, arrayCategory);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryPlace.setAdapter(adapterCategory);
    }
    private  void addCheckBoxs(){
        haveToilet=(CheckBox) findViewById(R.id.haveToilet);
        haveButtonHelp=(CheckBox) findViewById(R.id.haveButtonHelp);
        haveElevator=(CheckBox) findViewById(R.id.haveElevator);
        haveRamp=(CheckBox) findViewById(R.id.haveRamp);
        haveSwaddingTable=(CheckBox) findViewById(R.id.haveSwaddingTable);
    }
    private  void getImageFromFirebase(){
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(addNamePlace.getText().toString());

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                addImage.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }


}

