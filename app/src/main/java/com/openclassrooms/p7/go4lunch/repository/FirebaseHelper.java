package com.openclassrooms.p7.go4lunch.repository;

import android.content.Context;

import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class FirebaseHelper {

    private static final String USERS_COLLECTION_NAME = "users";
    private static final String RESTAURANT_COLLECTION_NAME = "restaurants";
    public static FirebaseHelper firebaseHelper;

    public static FirebaseHelper getInstance() {
        if (firebaseHelper == null) {
            firebaseHelper = new FirebaseHelper();
        }
        return firebaseHelper;
    }
    /**
     * Get current user from Firebase.
     * @return current User.
     */
    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * Call to get the collection where user is store in Firestore.
     * @return Collection reference.
     */
    public CollectionReference getUsersCollection() { return FirebaseFirestore.getInstance().collection(USERS_COLLECTION_NAME); }

    /**
     * Call a task to do a request to the collection where current user.
     * @return A query task.
     */
    public Task<DocumentSnapshot> getUserData() {
        String uid = Objects.requireNonNull(this.getCurrentUser()).getUid();
        return this.getUsersCollection().document(uid).get();
    }

    /**
     * Call a task to do a request to get all users in Firestore.
     * @return A query task.
     */
    public Task<QuerySnapshot> getUserDataCollection() {
        return this.getUsersCollection().get();
    }

    /**
     * Call to signOut the current user.
     * @param context context of the activity.
     * @return a task to signOut.
     */
    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context);
    }

    /**
     * Call to delete user account from Firestore.
     * @param context context of the activity.
     * @return a task to delete user.
     */
    public Task<Void> deleteUser(Context context) {
        return AuthUI.getInstance().delete(context);
    }

    public CollectionReference getRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(RESTAURANT_COLLECTION_NAME);
    }

    public Task<QuerySnapshot> getRestaurantsDataCollection() {
        return this.getRestaurantsCollection().get();
    }
}