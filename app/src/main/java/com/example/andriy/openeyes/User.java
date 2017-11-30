package com.example.andriy.openeyes;

/**
 * Created by Andriy on 27.11.2017.
 */

public class User {
    String name, category;
    public  User(){}
    public User(String setName, String setCategory){
        name=setName;
        category=setCategory;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}
