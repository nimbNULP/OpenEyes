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
    double latitude;
    double longitude;
    String category;
    boolean haveToilet, haveElevator,haveButtonHelp, haveSwaddingTable, haveRamp;

    public Place() {
    }


    public Place(String setName, String setDescribe, String setAdress, float setRatingPlace, ImageView setLogoPlace) {
        name = setName;
        describe = setDescribe;
        adress = setAdress;
        ratingPlace = setRatingPlace;
        logoPlace = setLogoPlace;

    }


    public Place(String setName, String setDescribe, String setAdress, float setRatingPlace,
                 double setLatitude, double setLongitude, String setCategory,boolean setHaveToilet,
                 boolean setHaveSwaddingTable, boolean setHaveRamp, boolean setHaveElevator, boolean setHaveButtonHelp) {
        name = setName;
        describe = setDescribe;
        adress = setAdress;
        ratingPlace = setRatingPlace;
        latitude=setLatitude;
        longitude=setLongitude;
        category=setCategory;
        haveButtonHelp=setHaveButtonHelp;
        haveElevator=setHaveElevator;
        haveRamp=setHaveRamp;
        haveSwaddingTable=setHaveSwaddingTable;
        haveToilet=setHaveToilet;
    }

    ;


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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCategory() {
        return category;
    }

    public boolean isHaveButtonHelp() {
        return haveButtonHelp;
    }

    public boolean isHaveElevator() {
        return haveElevator;
    }

    public boolean isHaveRamp() {
        return haveRamp;
    }

    public boolean isHaveSwaddingTable() {
        return haveSwaddingTable;
    }

    public boolean isHaveToilet() {
        return haveToilet;
    }
}
