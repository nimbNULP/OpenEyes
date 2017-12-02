package com.example.andriy.openeyes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RegistrationPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    Spinner categorySpinner;
    ImageView addAvatarUser;
    LinearLayout signUp;
    private EditText editNewUserName, editNewUserPassword, editNewUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
        String[] arrayCategory = {"Мама з немовлям", "Людина на інвалідному візку", "Інше"};
        categorySpinner = (Spinner) findViewById(R.id.spinerCategoryNewUser);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(RegistrationPage.this, android.R.layout.simple_spinner_dropdown_item, arrayCategory);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterCategory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();
        signUp=(LinearLayout) findViewById(R.id.buttonSignUp) ;
        editNewUserEmail = (EditText) findViewById(R.id.editNewUserEmail);
        editNewUserPassword = (EditText) findViewById(R.id.editNewUserPassword);
        editNewUserName=(EditText) findViewById(R.id.editNewUserName);
        addAvatarUser=(ImageView) findViewById(R.id.addAvatarUser);
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
        getMenuInflater().inflate(R.menu.registration_page, menu);
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void addUser(View view) {
        signUp.setClickable(false);
        mAuth.createUserWithEmailAndPassword(editNewUserEmail.getText().toString(), editNewUserPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tag", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            setInformation(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getBaseContext(), "Не вірний формат email",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    private void setInformation(FirebaseUser user){

        dataBase.collection("users")
                .document(editNewUserEmail.getText().toString())
                .set(new User(editNewUserName.getText().toString(), categorySpinner.getSelectedItem().toString()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        uploadImageToDataBase();
                        View toastDone = RegistrationPage.this.getLayoutInflater().inflate(R.layout.toast_done, null);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setView(toastDone);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(getBaseContext(), ListComfortablePlace.class);
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Помилка запису данних", Toast.LENGTH_SHORT);
                    }
                });

    }
    public void uploadImageToDataBase() {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();


        StorageReference mountainsRef = storageRef.child(editNewUserEmail.getText().toString() + ".jpg");


        StorageReference mountainImagesRef = storageRef.child("user/" + editNewUserEmail.getText().toString() + ".jpg");

        mountainsRef.getName().equals(mountainImagesRef.getName());
        mountainsRef.getPath().equals(mountainImagesRef.getPath());
        addAvatarUser.setDrawingCacheEnabled(true);
        addAvatarUser.buildDrawingCache();
        Bitmap bitmap = addAvatarUser.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }
    public  void  setImage(View view){
        Intent getImage = new Intent(Intent.ACTION_PICK);
        getImage.setType("image/*");
        startActivityForResult(getImage, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent getImage) {
        super.onActivityResult(requestCode, resultCode, getImage);
        Bitmap logoImage = null;
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri SelectedImage = getImage.getData();
                try {
                    logoImage = MediaStore.Images.Media.getBitmap(getContentResolver(), SelectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addAvatarUser.setImageBitmap(logoImage);
            }
        }
    }
}
