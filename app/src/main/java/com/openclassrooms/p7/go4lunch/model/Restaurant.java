package com.openclassrooms.p7.go4lunch.model;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;

/**
 * Created by lleotraas on 21.
 */
public class Restaurant {

    private final String id;
    private final String name;
    private final String address;
    private final String openingHours;
    private final String phoneNumber;
    private final String uriWebsite;
    private final float distance;
    private final double rating;
    private final LatLng position;
    private final Bitmap pictureUrl;
    private Integer numberOfFriendInterested;
    private Boolean isFavorite;
    private Boolean isSearched;

    // --- CONSTRUCTOR ---


    public Restaurant(String id, String name, String address, String openingHours, String phoneNumber, String uriWebsite, float distance, double rating, LatLng position, Bitmap pictureUrl, Integer numberOfFriendInterested, Boolean isFavorite, Boolean isSearched) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.openingHours = openingHours;
        this.phoneNumber = phoneNumber;
        this.uriWebsite = uriWebsite;
        this.distance = distance;
        this.rating = rating;
        this.position = position;
        this.pictureUrl = pictureUrl;
        this.numberOfFriendInterested = numberOfFriendInterested;
        this.isFavorite = isFavorite;
        this.isSearched = isSearched;
    }

    // --- GETTERS ---
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getOpeningHours() { return openingHours; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getUriWebsite() { return uriWebsite; }
    public float getDistance() { return distance; }
    public double getRating() { return rating; }
    public LatLng getPosition() {
        return position;
    }
    public Bitmap getPictureUrl() { return pictureUrl; }
    public Integer getNumberOfFriendInterested() {
        return numberOfFriendInterested;
    }
    public Boolean isFavorite() {
        return isFavorite;
    }
    public Boolean isSearched() {
        return isSearched;
    }

    // --- SETTERS ---
    public void setNumberOfFriendInterested(int numberOfFriendInterested) {
        this.numberOfFriendInterested = numberOfFriendInterested;
    }
    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    // --- COMPARATOR CLASS ---
<<<<<<< HEAD
    public static class RestaurantFriendInterestedComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return restaurantRight.numberOfFriendInterested.compareTo(restaurantLeft.numberOfFriendInterested);
        }
    }

    public static class RestaurantRatingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            if (restaurantRight.rating > restaurantLeft.rating) {
                return -1;
            } else {
                return 1;
            }
        }
    }



    public static class RestaurantDistanceComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return restaurantRight.numberOfFriendInterested.compareTo(restaurantLeft.numberOfFriendInterested);
        }
    }

    public static class RestaurantFavoriteComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return restaurantRight.numberOfFriendInterested.compareTo(restaurantLeft.numberOfFriendInterested);
        }
    }

    public static class RestaurantSearchedComparator implements Comparator<Restaurant> {
=======
    public static class RestaurantFriendInterestedAscendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return restaurantLeft.numberOfFriendInterested.compareTo(restaurantRight.numberOfFriendInterested);
        }
    }

    public static class RestaurantFriendInterestedDescendingComparator implements Comparator<Restaurant> {
>>>>>>> f0c8bfb
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return restaurantRight.numberOfFriendInterested.compareTo(restaurantLeft.numberOfFriendInterested);
        }
    }

    public static class RestaurantRatingAscendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return Double.compare(restaurantLeft.rating, restaurantRight.rating);
        }
    }

    public static class RestaurantRatingDescendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return Double.compare(restaurantRight.rating, restaurantLeft.rating);
        }
    }

    public static class RestaurantDistanceAscendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return Double.compare(restaurantRight.distance, restaurantLeft.distance);
        }
    }

    public static class RestaurantDistanceDescendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return Double.compare(restaurantLeft.distance, restaurantRight.distance);
        }
    }

    public static class RestaurantFavoriteAscendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return Boolean.compare(restaurantRight.isFavorite, restaurantLeft.isFavorite);
        }
    }

    public static class RestaurantFavoriteDescendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return Boolean.compare(restaurantLeft.isFavorite, restaurantRight.isFavorite);
        }
    }

    public static class RestaurantSearchedAscendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return Boolean.compare(restaurantRight.isSearched, restaurantLeft.isSearched);
        }
    }

    public static class RestaurantSearchedDescendingComparator implements Comparator<Restaurant> {
        @Override
        public int compare(Restaurant restaurantLeft, Restaurant restaurantRight) {
            return Boolean.compare(restaurantLeft.isSearched, restaurantRight.isSearched);
        }
    }
}
