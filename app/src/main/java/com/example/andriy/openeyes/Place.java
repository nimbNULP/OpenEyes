package com.example.andriy.openeyes;

/**
 * Created by Andriy on 16.11.2017.
 */

public class Place {
    String name;
    String describe;
    String adress;

    public Place(){}

    public Place(String setName, String setDescribe, String setAdress){
     name=setName;
     describe=setDescribe;
     adress=setAdress;
    };


    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
    }

    public String getAdress() {
        return adress;
    }




}
