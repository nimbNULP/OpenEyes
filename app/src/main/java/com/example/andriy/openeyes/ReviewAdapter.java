package com.example.andriy.openeyes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by Andriy on 26.11.2017.
 */

public class ReviewAdapter  extends ArrayAdapter<Review>{


    public ReviewAdapter (Context context, ArrayList<Review> reviews) {
        super(context,0,reviews);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Log.d("tag", "setreviewAdapter");
        View listItemView = LayoutInflater.from(getContext()).inflate(
                R.layout.item_review, parent, false);

        Review currentReview = getItem(position);

        TextView themeReview = (TextView) listItemView.findViewById(R.id.themeReview);
        themeReview.setText(currentReview.getTheme());

        TextView textReview=(TextView) listItemView.findViewById(R.id.textReview);
        textReview.setText(currentReview.getTextReview());

        TextView ratingReview=(TextView) listItemView.findViewById(R.id.ratingReview);
        ratingReview.setText(currentReview.getRating());


        return listItemView;
    }
}
