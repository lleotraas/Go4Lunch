package com.openclassrooms.p7.go4lunch.model;

public class FavoriteOrSelectedRestaurant {

    private String userId;
    private String restaurantId;
    private String restaurantName;
    private boolean isFavorite;
    private boolean isSelected;

    public FavoriteOrSelectedRestaurant() { }

    public FavoriteOrSelectedRestaurant(String userId, String restaurantId, String restaurantName, boolean isFavorite, boolean isSelected) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.isFavorite = isFavorite;
        this.isSelected = isSelected;
    }

    // --- GETTERS ---

    public String getUserId() { return userId; }
    public String getRestaurantId() {
        return restaurantId;
    }
    public String getRestaurantName() {
        return restaurantName;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public boolean isSelected() {
        return isSelected;
    }

    // --- SETTERS ---
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
