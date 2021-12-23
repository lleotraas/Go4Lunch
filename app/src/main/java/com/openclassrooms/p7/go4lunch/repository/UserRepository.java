package com.openclassrooms.p7.go4lunch.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.openclassrooms.p7.go4lunch.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {

    private final MutableLiveData<User> currentUser;
    private final MutableLiveData<List<User>> listOfUser;
    private final MutableLiveData<List<User>> listOfUserInterested;

    private static UserRepository mUserRepository;
    private final FirebaseHelper mFirebaseHelper;

    public static UserRepository getInstance() {
        if (mUserRepository == null) {
            FirebaseHelper firebaseHelper = new FirebaseHelper();
            mUserRepository = new UserRepository(firebaseHelper);
        }
        return mUserRepository;
    }

    public UserRepository(FirebaseHelper firebaseHelper) {
        mFirebaseHelper = firebaseHelper;
        currentUser = new MutableLiveData<>();
        listOfUser = new MutableLiveData<>();
        listOfUserInterested = new MutableLiveData<>();
    }

    public MutableLiveData<User> getCurrentFirestoreUser() {
        return currentUser;
    }
    /**
     * Create user in Firestore, if user already exist just update it.
     */
    public void createFireStoreUser() {
        FirebaseUser user = mFirebaseHelper.getCurrentUser();
        User userToCreate = new User(
                Objects.requireNonNull(user).getUid(),
                user.getDisplayName(),
                Objects.requireNonNull(user.getPhotoUrl()).toString(),
                "",
                "",
                false
        );
        mFirebaseHelper.getCurrentFirestoreUser().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    mFirebaseHelper.getUsersCollection().document(user.getUid()).set(userToCreate);
                    getCurrentFirestoreUser().postValue(userToCreate);
                } else {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User u = documentSnapshot.toObject(User.class);
                    getCurrentFirestoreUser().postValue(u);
                    Log.d(TAG, "createUser: ");
                }

            }
        }).addOnFailureListener(exception -> {

        });
    }

    public void updateUser(User user) {
        mFirebaseHelper.getUsersCollection().document(user.getUid()).update(
                "restaurantId", user.getRestaurantId(),
                "restaurantName", user.getRestaurantName(),
                "restaurantSelected", user.isRestaurantSelected()
        );
    }

    public void deleteUserFromFirestore() {
        String uid = Objects.requireNonNull(mFirebaseHelper.getCurrentUser()).getUid();
        mFirebaseHelper.getUsersCollection().document(uid).collection("restaurants").document().delete();
        mFirebaseHelper.getUsersCollection().document(uid).delete();
    }
    /**
     * Get userList from Firestore and store it in DUMMY_USER.
     */
    public MutableLiveData<List<User>> getAllUsers() {
        mFirebaseHelper.getAllUsers().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<User> users = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    users.add(document.toObject(User.class));
                }
                listOfUser.postValue(users);
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        }).addOnFailureListener(exception -> {

        });
        return listOfUser;
    }

    public MutableLiveData<List<User>> getAllInterestedUsers() {
        mFirebaseHelper.getUsersCollection().whereEqualTo("restaurantSelected", true).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }
            if (value != null) {
                ArrayList<User> users = new ArrayList<>();
                for (DocumentSnapshot document : value.getDocuments()) {
                    users.add(document.toObject(User.class));
                }
                listOfUserInterested.postValue(users);
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
        return listOfUserInterested;
    }

    public List<User> getAllInterestedUsersAtCurrentRestaurant(String restaurantId, List<User> users) {
        List<User> listMutableLiveData = new ArrayList<>();
        String userId = Objects.requireNonNull(mFirebaseHelper.getCurrentUser()).getUid();
        for (User user : users) {
            if (
                    user.getRestaurantId().equals(restaurantId)
//                            && !user.getUid().equals(userId)
            ) {
                listMutableLiveData.add(user);
            }
        }
        return listMutableLiveData;
    }

//    public void updateNumberOfFriendInterested(String restaurantId) {
//        for (User u : Objects.requireNonNull(listOfUserInterested.getValue())) {
//            if (u.getRestaurantId().equals(restaurantId)) {
//                u.setNumberOfFriendInterested(u.getNumberOfFriendInterested()+1);
//            }
//        }
//    }

    /**
     * Update current user.
     * @param user user to update.
     */
    public void updateFirestoreUser(User user) {
        mFirebaseHelper.getUsersCollection().document(user.getUid()).update(
                "restaurantId", user.getRestaurantId(),
                "restaurantName", user.getRestaurantName(),
                "restaurantSelected", user.isRestaurantSelected()
        );
    }

    /**
     * Delete user from Firestore.
     */
    public void deleteFirestoreUser() {
        String uid = Objects.requireNonNull(mFirebaseHelper.getCurrentUser()).getUid();
        mFirebaseHelper.getUsersCollection().document(uid).collection("restaurants").document().delete();
        mFirebaseHelper.getUsersCollection().document(uid).delete();
    }

    public FirebaseUser getCurrentUser() {
        return mFirebaseHelper.getCurrentUser();
    }

    public Task<Void> deleteUser(Context context) {
        return mFirebaseHelper.deleteUser(context);
    }

    public Task<Void> signOut(Context context) {
        return mFirebaseHelper.signOut(context);
    }
}
