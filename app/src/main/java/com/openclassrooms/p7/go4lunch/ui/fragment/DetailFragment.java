package com.openclassrooms.p7.go4lunch.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.openclassrooms.p7.go4lunch.R;
import com.openclassrooms.p7.go4lunch.databinding.FragmentDetailBinding;
import com.openclassrooms.p7.go4lunch.injector.DI;
import com.openclassrooms.p7.go4lunch.model.Restaurant;
import com.openclassrooms.p7.go4lunch.model.RestaurantFavorite;
import com.openclassrooms.p7.go4lunch.model.User;
import com.openclassrooms.p7.go4lunch.service.ApiService;
import com.openclassrooms.p7.go4lunch.ui.DetailActivityAdapter;
import com.openclassrooms.p7.go4lunch.ui.UserAndRestaurantViewModel;

import java.util.HashMap;
import java.util.Map;

public class DetailFragment extends Fragment {

    private static final int PERMISSION_CODE = 100;
    public static int LIKE_BTN_TAG;
    private FragmentDetailBinding mBinding;
    private User mCurrentUser;
    private Restaurant mCurrentRestaurant;
    private RestaurantFavorite mCurrentRestaurantFavorite;
    private ApiService mApiService;
    private UserAndRestaurantViewModel mViewModel;
    private final ImageView[] ratingStarsArray = new ImageView[3];
    private DetailActivityAdapter mAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = mBinding.getRoot();
        this.configureViewBinding();
        this.initServiceAndViewModel();
        this.searchById();
        this.configureView();
        this.initRecyclerView();
        this.configureListeners();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.activityDetailRecyclerview.setAdapter(mAdapter);
        mViewModel.getAllInterestedUsersAtCurrentRestaurant(mCurrentRestaurant.getId()).observe(getViewLifecycleOwner(), mAdapter::submitList);
    }

    private void configureViewBinding() {
        ratingStarsArray[0] = mBinding.activityDetailFirstRatingImg;
        ratingStarsArray[1] = mBinding.activityDetailSecondRatingImg;
        ratingStarsArray[2] = mBinding.activityDetailThirdRatingImg;
        LIKE_BTN_TAG = mBinding.activityDetailLikeBtn.getId();
    }

    private void initServiceAndViewModel() {
        mApiService = DI.getRestaurantApiService();
        mViewModel = new ViewModelProvider(this).get(UserAndRestaurantViewModel.class);
        mViewModel.getRestaurantData().observe(getViewLifecycleOwner(), new Observer<Map<String, RestaurantFavorite>>() {
            @Override
            public void onChanged(Map<String, RestaurantFavorite> stringRestaurantFavoriteMap) {

            }
        });
        Map<String, RestaurantFavorite> mRestaurantDataMap = mViewModel.getRestaurantData().getValue();
        if (mRestaurantDataMap == null) {
            mRestaurantDataMap = new HashMap<>();
        }
    }

    private void searchById() {
        Intent mainActivityIntent = requireActivity().getIntent();
        String currentUserId = mViewModel.getCurrentUser().getUid();
        String CURRENT_RESTAURANT_ID = mainActivityIntent.getStringExtra("restaurantId");
        mCurrentRestaurant = mViewModel.getCurrentRestaurant(CURRENT_RESTAURANT_ID);
        mCurrentUser = mViewModel.getCurrentFirestoreUser(currentUserId);
        mCurrentRestaurantFavorite = mViewModel.getCurrentRestaurantData(CURRENT_RESTAURANT_ID);
        this.setImageAtStart();
    }

    private void setImageAtStart() {
        if (mCurrentRestaurantFavorite != null) {
            this.setFavoriteImage(true);
        }
        if (mCurrentUser.isRestaurantSelected() && mCurrentUser.getRestaurantId().equals(mCurrentRestaurant.getId())) {
            this.setSelectedImage(true);
        }
    }

    private void configureView() {
        if (mCurrentRestaurant.getPictureUrl() != null) {
            Glide.with(this)
                    .load(mCurrentRestaurant.getPictureUrl())
                    .centerCrop()
                    .into(mBinding.activityDetailImageHeader);
        } else {
            Glide.with(this)
                    .load(R.drawable.no_image)
                    .centerCrop()
                    .into(mBinding.activityDetailImageHeader);
        }
        mBinding.activityDetailRestaurantNameTv.setText(mApiService.formatRestaurantName(mCurrentRestaurant.getName()));
        mBinding.activityDetailRestaurantTypeAndAdressTv.setText(mCurrentRestaurant.getAddress());
        for (int index = 0; index < ratingStarsArray.length; index++) {
            ratingStarsArray[index].setImageResource(mApiService.setRatingStars(index, mCurrentRestaurant.getRating()));
        }
    }

    private void initRecyclerView() {
        mBinding.activityDetailRecyclerview.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
        mBinding.activityDetailRecyclerview.addItemDecoration(new DividerItemDecoration(requireActivity().getApplicationContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new DetailActivityAdapter();
        mBinding.activityDetailRecyclerview.setAdapter(mAdapter);
        mViewModel.getAllInterestedUsersAtCurrentRestaurant(mCurrentRestaurant.getId()).observe(getViewLifecycleOwner(), mAdapter::submitList);
    }

    private void configureListeners() {
        // Call Restaurant Button
        mBinding.activityDetailCallBtn.setOnClickListener(view -> permissionToCall());
        // Go to the website of the restaurant
        mBinding.activityDetailWebsiteBtn.setOnClickListener(view -> goToWebsite());
        // Make Restaurant as Favorite Button
        mBinding.activityDetailLikeBtn.setOnClickListener(view -> updateRestaurantFavorite());
        // Select Restaurant to lunch Button
        mBinding.activityDetailFab.setOnClickListener(view -> updateRestaurantSelected());
    }

    private void permissionToCall() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CODE);
        }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            callTheRestaurant();
        }
    }
    private void callTheRestaurant() {
        String phoneNumber = mCurrentRestaurant.getPhoneNumber();
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(phoneIntent);
    }

    private void goToWebsite() {
        String url =  mCurrentRestaurant.getUriWebsite();
        if (!url.equals("")) {
            Intent browse = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browse);
        } else {
            Toast toast = Toast.makeText(requireContext(),"No website found",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Check if the FavoriteRestaurant exist in the DB
     * if exist, update the corresponding boolean
     * if not exist, create it.
     */
    private void updateRestaurantSelected() {
        if (mCurrentUser.getRestaurantId().equals(mCurrentRestaurant.getId())) {
            mCurrentUser.setRestaurantSelected(false);
            mCurrentUser.setRestaurantName("");
            mCurrentUser.setRestaurantId("");
        } else {
            mCurrentUser.setRestaurantSelected(true);
            mCurrentUser.setRestaurantName(mCurrentRestaurant.getName());
            mCurrentUser.setRestaurantId(mCurrentRestaurant.getId());
        }
        setSelectedImage(mCurrentUser.isRestaurantSelected());
        mViewModel.updateUser(mCurrentUser);
    }

    private void updateRestaurantFavorite() {
        if (mCurrentRestaurantFavorite != null) {
            mViewModel.deleteRestaurantFavorite(mCurrentRestaurantFavorite);
            mCurrentRestaurantFavorite.setFavorite(false);
            mCurrentRestaurantFavorite = null;
            setFavoriteImage(false);
        } else {
            mViewModel.createRestaurantFavorite(createRestaurantFavorite());
        }
    }

    private RestaurantFavorite createRestaurantFavorite() {
        setFavoriteImage(true);
        RestaurantFavorite restaurantFavorite = new RestaurantFavorite(
                mCurrentRestaurant.getId(),
                true
        );
        mCurrentRestaurantFavorite = restaurantFavorite;
        return restaurantFavorite;
    }

    private void setFavoriteImage(boolean favorite) {
        mBinding.activityDetailLikeImg.setImageResource(mApiService.setFavoriteImage(favorite));
    }

    private void setSelectedImage(boolean selected) {
        mBinding.activityDetailFab.setImageResource(mApiService.setSelectedImage(selected));
        if (selected) {
            mBinding.activityDetailFab.setColorFilter(ContextCompat.getColor(requireContext(), R.color.map_marker_favorite));
        }
    }
}
