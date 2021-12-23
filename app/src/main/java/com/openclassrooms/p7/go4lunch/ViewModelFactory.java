package com.openclassrooms.p7.go4lunch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.p7.go4lunch.repository.FirebaseHelper;
import com.openclassrooms.p7.go4lunch.repository.MapViewRepository;
import com.openclassrooms.p7.go4lunch.repository.RestaurantDataRepository;
import com.openclassrooms.p7.go4lunch.repository.UserRepository;
import com.openclassrooms.p7.go4lunch.ui.UserAndRestaurantViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();
                }
            }
        }
        return factory;
    }

    private final FirebaseHelper firebaseHelper = new FirebaseHelper();
    private final UserRepository userRepository = new UserRepository(firebaseHelper);
    private final RestaurantDataRepository restaurantDataRepository = new RestaurantDataRepository();
    private final MapViewRepository mapViewRepository = new MapViewRepository();

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserAndRestaurantViewModel.class)) {
            return (T) new UserAndRestaurantViewModel(userRepository, restaurantDataRepository, mapViewRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
