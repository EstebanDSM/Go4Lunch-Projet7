package com.guzzler.go4lunch_p7.ui.restaurant_details;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.guzzler.go4lunch_p7.BuildConfig;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.RestaurantsHelper;
import com.guzzler.go4lunch_p7.api.firebase.UserHelper;
import com.guzzler.go4lunch_p7.api.retrofit.googleplace.GooglePlaceDetailsCalls;
import com.guzzler.go4lunch_p7.databinding.ActivityRestaurantDetailsBinding;
import com.guzzler.go4lunch_p7.models.Workmate;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;
import com.guzzler.go4lunch_p7.utils.Constants;
import com.guzzler.go4lunch_p7.utils.LikeButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.guzzler.go4lunch_p7.utils.Constants.MAX_HEIGHT_LARGE;
import static com.guzzler.go4lunch_p7.utils.Constants.MAX_WIDTH_LARGE;
import static com.guzzler.go4lunch_p7.utils.DisplayRating.displayRating;
import static com.guzzler.go4lunch_p7.utils.GetTodayDate.getTodayDate;
import static com.guzzler.go4lunch_p7.utils.ShowToastSnack.showToast;


public class Restaurant_Details extends AppCompatActivity implements View.OnClickListener, GooglePlaceDetailsCalls.Callbacks {

    private final List<Workmate> mWorkmates = new ArrayList<>();

    private ResultDetails requestResult;
    private Restaurant_Details_RecyclerViewAdapter mAdapter;

    private ActivityRestaurantDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        configureButtonClickListener();
        configureRecyclerView();
        requestRetrofit();
        setFloatingListener();
    }

    private void setFloatingListener() {
        binding.floatingActionButton.setOnClickListener(view -> bookThisRestaurant());
    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void bookThisRestaurant() {
        String userId = getCurrentUser().getUid();
        String restaurantId = requestResult.getPlaceId();
        String restaurantName = requestResult.getName();
        checkBooked(userId, restaurantId, restaurantName, true);
    }

    private void checkBooked(String userId, String restaurantId, String restaurantName, Boolean tryingToBook) {
        RestaurantsHelper.getBooking(userId, getTodayDate()).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                if (restaurantTask.getResult().size() == 1) {
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        if (restaurant.getData().get("restaurantName").equals(restaurantName)) {
                            displayFloating((R.drawable.ic_clear_black_24dp), getResources().getColor(R.color.colorError));
                            if (tryingToBook) {
                                Booking_Firebase(userId, restaurantId, restaurantName, restaurant.getId(), false, false, true);
                                showToast(this, getResources().getString(R.string.cancel_booking), Toast.LENGTH_SHORT);
                            }
                        } else {
                            displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
                            if (tryingToBook) {
                                Booking_Firebase(userId, restaurantId, restaurantName, restaurant.getId(), false, true, false);
                                showToast(this, getResources().getString(R.string.modify_booking), Toast.LENGTH_SHORT);
                            }
                        }
                    }
                } else {
                    displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
                    if (tryingToBook) {
                        Booking_Firebase(userId, restaurantId, restaurantName, null, true, false, false);
                        showToast(this, getResources().getString(R.string.new_booking), Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }


    // TODO : raffraichir le fragment workmates si modif de reservation dans detail resto et que l'on fait retour
    private void Booking_Firebase(String userId, String restaurantId, String restaurantName, @Nullable String bookingId, boolean toCreate, boolean toUpdate, boolean toDelete) {
        if (toUpdate) {
            RestaurantsHelper.deleteBooking(bookingId);
            RestaurantsHelper.createBooking(getTodayDate(), userId, restaurantId, restaurantName).addOnFailureListener(onFailureListener());
            displayFloating((R.drawable.ic_clear_black_24dp), getResources().getColor(R.color.colorError));

        } else {
            if (toCreate) {
                RestaurantsHelper.createBooking(getTodayDate(), userId, restaurantId, restaurantName).addOnFailureListener(onFailureListener());
                displayFloating((R.drawable.ic_clear_black_24dp), getResources().getColor(R.color.colorError));
            } else if (toDelete) {
                RestaurantsHelper.deleteBooking(bookingId);
                displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
            }
        }
        Update_Booking_RecyclerView(requestResult.getPlaceId());
    }


    private void checkLiked() {
        RestaurantsHelper.getAllLikeByUserId(getCurrentUser().getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e("TAG", "checkIfLiked: " + task.getResult().getDocuments());
                if (task.getResult().isEmpty()) {
                    binding.restaurantItemLike.setText(getResources().getString(R.string.like));
                } else {
                    for (DocumentSnapshot restaurant : task.getResult()) {
                        if (restaurant.getId().equals(requestResult.getPlaceId())) {
                            binding.restaurantItemLike.setText(getResources().getString(R.string.unlike));
                            break;
                        } else {
                            binding.restaurantItemLike.setText(getResources().getString(R.string.like));
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restaurant_item_call:
                if (requestResult.getFormattedPhoneNumber() != null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + requestResult.getFormattedPhoneNumber()));
                    startActivity(intent);
                } else {
                    showToast(this, getResources().getString(R.string.no_phone), Toast.LENGTH_SHORT);
                }
                break;
            case R.id.restaurant_item_like:
                if (binding.restaurantItemLike.getText().equals(getResources().getString(R.string.like))) {

                    LikeButton.likeRestaurant(requestResult, this, binding.restaurantItemLike, getResources().getString(R.string.unlike), getResources().getString(R.string.rest_like));
                } else {
                    LikeButton.dislikeRestaurant(requestResult, this, binding.restaurantItemLike, getResources().getString(R.string.like), getResources().getString(R.string.rest_dislike), getResources().getString(R.string.rest_like));
                }
                break;
            case R.id.restaurant_item_website:
                if (requestResult.getWebsite() != null) {
                    Intent intent = new Intent(this, WebView_Activity.class);
                    intent.putExtra("Website", requestResult.getWebsite());
                    startActivity(intent);
                } else {
                    showToast(this, getResources().getString(R.string.no_website), Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    private void configureRecyclerView() {
        this.mAdapter = new Restaurant_Details_RecyclerViewAdapter(mWorkmates);
        this.binding.restaurantRecyclerView.setAdapter(mAdapter);
        this.binding.restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void requestRetrofit() {
        String result = getIntent().getStringExtra("PlaceDetailResult");
        Log.e("TAG", "retrieveObject: " + result);
        GooglePlaceDetailsCalls.fetchPlaceDetails(this, result);
    }

    private void configureButtonClickListener() {
        binding.restaurantItemCall.setOnClickListener(this);
        binding.restaurantItemLike.setOnClickListener(this);
        binding.restaurantItemWebsite.setOnClickListener(this);
    }

    private void updateUI(ResultDetails resultDetails) {

        if (getCurrentUser() != null) {
            checkBooked(getCurrentUser().getUid(), requestResult.getPlaceId(), requestResult.getName(), false);
            checkLiked();
        } else {
            binding.restaurantItemLike.setText(R.string.like);
            showToast(this, getResources().getString(R.string.restaurant_error), Toast.LENGTH_LONG);
            displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
        }

        // Chargement de la photo dans le détail des restaus
        if (resultDetails.getPhotos() != null) {
            Picasso.get().load(Constants.BASE_URL_PLACE_PHOTO + "?maxwidth=" + MAX_WIDTH_LARGE + "&maxheight=" + MAX_HEIGHT_LARGE + "&photoreference=" + resultDetails.getPhotos().get(0)
                    .getPhotoReference() + "&key=" + BuildConfig.api_key).into(binding.imageView);


        } else {
            Glide.with(binding.imageView).load(R.drawable.ic_no_image_available)
                    .apply(new RequestOptions()

                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .into(binding.imageView);
        }


        binding.restaurantName.setText(resultDetails.getName());
        binding.restaurantAddress.setText(resultDetails.getVicinity());
        Update_Booking_RecyclerView(requestResult.getPlaceId());
        displayRating(resultDetails, binding.itemRatingBar);

    }

    private void Update_Booking_RecyclerView(String placeId) {
        mWorkmates.clear();
        RestaurantsHelper.getTodayBooking(placeId, getTodayDate()).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                if (restaurantTask.getResult().isEmpty()) {
                    mAdapter.notifyDataSetChanged();
                } else {
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        UserHelper.getWorkmate(restaurant.getData().get("userId").toString()).addOnCompleteListener(workmateTask -> {
                            if (workmateTask.isSuccessful()) {
                                String name = workmateTask.getResult().getData().get("name").toString();
                                String uid = workmateTask.getResult().getData().get("uid").toString();
                                String urlPicture = workmateTask.getResult().getData().get("urlPicture").toString();
                                Workmate workmateToAdd = new Workmate(uid, urlPicture, name);
                                mWorkmates.add(workmateToAdd);
                            }
                            mAdapter.notifyDataSetChanged();
                        });
                    }
                }
            }
        });
    }

    private void displayFloating(int icon, int color) {
        Drawable mDrawable = ContextCompat.getDrawable(getBaseContext(), icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        binding.floatingActionButton.setImageDrawable(mDrawable);
    }

    protected OnFailureListener onFailureListener() {
        return e -> Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        requestResult = resultDetails;
        updateUI(resultDetails);
    }

    @Override
    public void onFailure() {
        showToast(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG);
    }
}