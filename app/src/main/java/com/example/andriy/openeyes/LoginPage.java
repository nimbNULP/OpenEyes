package com.example.andriy.openeyes;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    EditText editEmail, editPassword;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseUser user;
    View anonim, users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        anonim=getLayoutInflater().inflate(R.layout.nav_header_anonim, null);
        users = getLayoutInflater().inflate(R.layout.nav_header_user, null);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        user = mAuth.getCurrentUser();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        editEmail=(EditText) findViewById(R.id.editEmail);
        editPassword=(EditText) findViewById(R.id.editPassword);

        updateUI(user);
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
        getMenuInflater().inflate(R.menu.login_page, menu);
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


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(getBaseContext(), LoginPage.class);
        startActivity(intent);
    }

    public void goToRegistration(View view) {
        Intent intent = new Intent(getBaseContext(), RegistrationPage.class);
        startActivity(intent);
    }
    public  void signIn(View view){
        if (user==null){
        mAuth.signInWithEmailAndPassword(editEmail.getText().toString(), editPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            updateUI(user);
                            Intent intent=new Intent(getBaseContext(),MainMenu.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getBaseContext(),"Невірний логін або пароль",Toast.LENGTH_SHORT);
                        }


                    }
                });
        }else{
            Toast.makeText(getBaseContext(),"Ви вже увійшли", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateUI(FirebaseUser user){
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
      FirebaseAuth.getInstance().signOut();
      user=mAuth.getCurrentUser();
       updateUI(user);
    }
}
