package com.openclassrooms.p7.go4lunch;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.p7.go4lunch.model.Restaurant;
import com.openclassrooms.p7.go4lunch.model.RestaurantFavorite;
import com.openclassrooms.p7.go4lunch.model.User;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public abstract class TestUtils {

    public static final String USER_EMAIL_TEST = "user@test.fr";
    public static final String USER_PASSWORD_TEST = "password";
    public static final String USER_RESTAURANT_NAME_TEST = "restaurant test";
    public static final String USER_RESTAURANT_ID = "123456";
    public static final LatLng USER_CURRENT_LOCATION = new LatLng(43.4073612, 3.6997723);
    public static RectangularBounds USER_RECTANGULAR_BOUNDS = RectangularBounds.newInstance(
            new LatLng(USER_CURRENT_LOCATION.latitude - 0.060000, USER_CURRENT_LOCATION.longitude + 0.060000),
            new LatLng(USER_CURRENT_LOCATION.latitude + 0.060000, USER_CURRENT_LOCATION.longitude + 0.060000));

    public static final String FIRST_RESTAURANT_ID =  "ChIJexGknqI1sRIRWr8XRhcWfKw";
    public static final String SECOND_RESTAURANT_ID = "ChIJ5X07y6M1sRIRNiPZimTgF-4";
    public static final String THIRD_RESTAURANT_ID =  "ChIJ34_PP541sRIR_sG_wtG5EZE";

    public static final String RESTAURANT_NAME = "fleur de sel";
    public static final String RESTAURANT_ADDRESS = "15 Rue du 11 Novembre 1918, 34200 Sète";
    public static final String RESTAURANT_PHONE_NUMBER = "+33448171742";
    public static final String RESTAURANT_WEBSITE_URL = "https://app.edgar.travel/#/restaurants/cksvlhzxu3fez0709gcc71ooj";
    public static final String RESTAURANT_OPENING_HOURS = "still closed";
    public static final LatLng RESTAURANT_LOCATION = new LatLng(43.4146856,3.6927541);
    public static final float[] RESTAURANT_DISTANCE = new float[]{500};
    public static final double RESTAURANT_RATING = 4.7;

    public static final String NEARBY_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
            + "?keyword=restaurant"
            + "&location=" + USER_CURRENT_LOCATION.latitude + "," + USER_CURRENT_LOCATION.longitude
            + "&radius=1500"
            + "&sensor=true"
            + "&key=" + BuildConfig.GMP_KEY;

    public static void signInUser(String userEmail, String userPassword) throws ExecutionException, InterruptedException {
        FirebaseUser currentFirebaseUser = Tasks.await(FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail, userPassword)).getUser();
        Assert.assertNotNull(currentFirebaseUser);
    }


    public static Task<DocumentSnapshot> getCurrentFirestoreUser(String userID) {
        return FirebaseFirestore.getInstance()
                .collection("users_test")
                .document(userID).get();
    }

    public static void createFirebaseUser(String userEmail, String userPassword) throws ExecutionException, InterruptedException {
        Tasks.await(FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPassword));
    }

    public static List<RestaurantFavorite> getDefaultRestaurantFavorite() {
        List<RestaurantFavorite> restaurantFavoriteList = new ArrayList<>();
        restaurantFavoriteList.add(new RestaurantFavorite(FIRST_RESTAURANT_ID));
        restaurantFavoriteList.add(new RestaurantFavorite(SECOND_RESTAURANT_ID));
        restaurantFavoriteList.add(new RestaurantFavorite(THIRD_RESTAURANT_ID));
        return restaurantFavoriteList;
    }

    public static List<User> getDefaultUserList() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("1111", "test1", "photo", "restaurant1", "ChIJexGknqI1sRIRWr8XRhcWfKw", true));
        userList.add(new User("2222", "test2", "photo", "", "", false));
        userList.add(new User("3333", "test3", "photo", "restaurant1", "ChIJexGknqI1sRIRWr8XRhcWfKw", true));
        userList.add(new User("4444", "test4", "photo", "restaurant2", "ChIJ5X07y6M1sRIRNiPZimTgF-4", true));
        return userList;
    }

    public static Task<DocumentSnapshot> getFirestoreRestaurantFavorite(String userId, String restaurantId) {
        return FirebaseFirestore.getInstance()
                .collection("users_test")
                .document(userId)
                .collection("restaurants_test")
                .document(restaurantId)
                .get();
    }
}
