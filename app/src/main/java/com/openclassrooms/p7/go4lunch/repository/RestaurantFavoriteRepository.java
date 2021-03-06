package com.openclassrooms.p7.go4lunch.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.p7.go4lunch.model.RestaurantFavorite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantFavoriteRepository {
    private static RestaurantFavoriteRepository mRestaurantFavoriteRepository;
    private final FirebaseHelper mFirebaseHelper = FirebaseHelper.getInstance();
    private final MutableLiveData<RestaurantFavorite> currentRestaurantFavorite = new MutableLiveData<>();
    private final MutableLiveData<List<RestaurantFavorite>> listOfRestaurantFavorite = new MutableLiveData<>();

    public static RestaurantFavoriteRepository getInstance() {
        if (mRestaurantFavoriteRepository == null) {
            mRestaurantFavoriteRepository = new RestaurantFavoriteRepository();
        }
        return mRestaurantFavoriteRepository;
    }

    /**
     * Create a favorite restaurant in firestore.
     * @param restaurantFavorite restaurant to create in firestore.
     */
    public void createRestaurantFavorite(RestaurantFavorite restaurantFavorite) {
        mFirebaseHelper.getRestaurantFavoriteReferenceForCurrentUser().document(restaurantFavorite.getRestaurantId()).set(restaurantFavorite);
    }

    /**
     * Get the current restaurant favorite livedata.
     * @param currentRestaurantId Current restaurant id.
     * @return Current restaurant favorite livedata.
     */
    public MutableLiveData<RestaurantFavorite> getCurrentRestaurantFavorite(String currentRestaurantId) {
        mFirebaseHelper.getRestaurantFavoriteReferenceForCurrentUser().document(currentRestaurantId).get().addOnCompleteListener(task -> {
            DocumentSnapshot documentSnapshot = task.getResult();
            currentRestaurantFavorite.postValue(documentSnapshot.toObject(RestaurantFavorite.class));
        });
        return currentRestaurantFavorite;
    }

    /**
     * Get the current user list of restaurant favorite livedata.
     * @return Current user list of restaurant favorite livedata.
     */
    public MutableLiveData<List<RestaurantFavorite>> getAllRestaurantFavorite() {
        List<RestaurantFavorite> restaurantFavoriteList = new ArrayList<>();
        mFirebaseHelper.getRestaurantFavoriteReferenceForCurrentUser().get().addOnCompleteListener(task -> {
           if (task.isSuccessful()) {
               QuerySnapshot querySnapshot = task.getResult();
               for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                   restaurantFavoriteList.add(documentSnapshot.toObject(RestaurantFavorite.class));
                   listOfRestaurantFavorite.postValue(restaurantFavoriteList);
               }
               if (task.getResult().isEmpty()) {
                   listOfRestaurantFavorite.postValue(restaurantFavoriteList);
               }
           }
        });
        return listOfRestaurantFavorite;
    }

    /**
     * Delete a favorite restaurant in firestore.
     * @param restaurantFavorite restaurant to delete in firestore.
     */
    public void deleteRestaurantFavorite(RestaurantFavorite restaurantFavorite) {
        mFirebaseHelper.getRestaurantFavoriteReferenceForCurrentUser()
                .document(restaurantFavorite.getRestaurantId())
                .delete();
    }
}
