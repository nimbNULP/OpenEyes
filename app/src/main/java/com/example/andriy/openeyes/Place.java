package com.example.andriy.openeyes;

import android.widget.ImageView;

/**
 * Created by Andriy on 16.11.2017.
 */

public class Place {
    String name;
    String describe;
    String adress;
    float ratingPlace;
    ImageView logoPlace;

    public Place(){};

    public Place(String setName, String setDescribe, String setAdress, float setRatingPlace, ImageView setLogoPlace) {
        name=setName;
        describe=setDescribe;
        adress=setAdress;
        ratingPlace=setRatingPlace;
        logoPlace=setLogoPlace;
    };
    public Place(String setName, String setDescribe, String setAdress, float setRatingPlace){
     name=setName;
     describe=setDescribe;
     adress=setAdress;
     ratingPlace=setRatingPlace;
    };



    public String getName() {
        return name;
    }

    public float getRatingPlace() {
        return ratingPlace;
    }

    public String getDescribe() {
        return describe;
    }

    public String getAdress() {
        return adress;
    }

    public ImageView getLogoPlace() {
        return logoPlace;
    }
}
