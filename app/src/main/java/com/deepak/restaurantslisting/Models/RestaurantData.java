package com.deepak.restaurantslisting.Models;

import java.util.ArrayList;

/**
 * Created by Sharma on 6/1/2016.
 */
public class RestaurantData {
    private String restImage, restname,   locality;
    private boolean isLiked;
    private int noOfOffers;
    private double latitude, longitude, distance;
    private ArrayList<String> categories;

    public RestaurantData(String restImage, String restname, String locality, boolean isLiked, int noOfOffers, double latitude, double longitude, double distance, ArrayList<String> categories) {
        this.restImage = restImage;
        this.restname = restname;
        this.locality = locality;
        this.isLiked = isLiked;
        this.noOfOffers = noOfOffers;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.categories = categories;
    }

    public String getRestImage() {
        return restImage;
    }

    public void setRestImage(String restImage) {
        this.restImage = restImage;
    }

    public String getRestname() {
        return restname;
    }

    public void setRestname(String restname) {
        this.restname = restname;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public int getNoOfOffers() {
        return noOfOffers;
    }

    public void setNoOfOffers(int noOfOffers) {
        this.noOfOffers = noOfOffers;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }
}
