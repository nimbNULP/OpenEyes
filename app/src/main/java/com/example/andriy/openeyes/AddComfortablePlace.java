package com.example.andriy.openeyes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;

public class AddComfortablePlace extends AppCompatActivity implements View.OnClickListener {
    ImageView addImage;
    FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    EditText addNamePlace, addDescribePlace, addAdressPlace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comfortable_place);
        addImage=(ImageView) findViewById(R.id.addImage);
        addImage.setOnClickListener(this);
        addNamePlace=(EditText) findViewById(R.id.addNamePlace);
        addAdressPlace=(EditText) findViewById(R.id.addAdressPlace);
        addDescribePlace=(EditText) findViewById(R.id.addDescribePlace);

    }


    private boolean checkEditText(EditText editText){
        if (editText.getText().length() == 0) {
            Toast.makeText(AddComfortablePlace.this,"Заповніть усі поля", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public void addDataToDataBase(View view){
        if (checkEditText(addNamePlace) &&checkEditText(addAdressPlace)&&checkEditText(addDescribePlace)) {

            dataBase.collection("place")
                    .document(addNamePlace.getText().toString())
                    .set(new Place(addNamePlace.getText().toString(),addDescribePlace.getText().toString(), addAdressPlace.getText().toString()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(AddComfortablePlace.this,"Дані успішно записані", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddComfortablePlace.this,"Помилка запису данних",Toast.LENGTH_SHORT);
                        }
        });
    }
    }

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








}
