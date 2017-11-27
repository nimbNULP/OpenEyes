package com.example.andriy.openeyes;

/**
 * Created by Andriy on 26.11.2017.
 */

public class Review {
    String theme, textReview;
    String rating;
    public Review(){};
    public Review(String setTheme, String setTextReviews, String setRating){
        theme=setTheme;
        textReview=setTextReviews;
        rating=setRating;
    }

    public String getTheme() {
        return theme;
    }

    public String getTextReview() {
        return textReview;
    }

    public String getRating() {
        return rating;
    }
}
