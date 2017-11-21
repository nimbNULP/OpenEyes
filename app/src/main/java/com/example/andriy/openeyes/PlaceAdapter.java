package com.example.andriy.openeyes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlaceAdapter extends ArrayAdapter<Place> {

    FirebaseStorage storage= FirebaseStorage.getInstance();

    private static final String LOG_TAG = PlaceAdapter.class.getSimpleName();


    public PlaceAdapter(Activity context, ArrayList<Place> places) {
        super(context, 0, places);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view

            View listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_place, parent, false);


        // Get the {@link AndroidFlavor} object located at this position in the list
        Place currentPlace = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView namePlace = (TextView) listItemView.findViewById(R.id.placeName);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        namePlace.setText(currentPlace.getName());



        LinearLayout accessibilityLayout=(LinearLayout) listItemView.findViewById(R.id.accessibilityLayout);
        if (currentPlace.getRatingPlace()==1){
            accessibilityLayout.setBackgroundResource(R.color.badPlace);
        } else if(currentPlace.getRatingPlace()==2){
            accessibilityLayout.setBackgroundResource(R.color.normalPlace);
        }
        final ImageView iconView = (ImageView) listItemView.findViewById(R.id.logoPlace);
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(currentPlace.getName()+".jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iconView.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });


        return listItemView;
    }

}
