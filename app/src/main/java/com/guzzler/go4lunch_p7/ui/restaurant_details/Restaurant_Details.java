package com.guzzler.go4lunch_p7.ui.restaurant_details;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.guzzler.go4lunch_p7.BuildConfig;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.RestaurantsHelper;
import com.guzzler.go4lunch_p7.api.firebase.UserHelper;
import com.guzzler.go4lunch_p7.api.retrofit.googleplace.GooglePlaceDetailsCalls;
import com.guzzler.go4lunch_p7.models.Workmate;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.guzzler.go4lunch_p7.utils.DisplayRating.displayRating;
import static com.guzzler.go4lunch_p7.utils.GetTodayDate.getTodayDate;


public class Restaurant_Details extends AppCompatActivity implements View.OnClickListener, GooglePlaceDetailsCalls.Callbacks {

    @Nullable
    @BindView(R.id.restaurant_name)
    TextView mRestaurantName;
    @Nullable
    @BindView(R.id.restaurant_address)
    TextView mRestaurantAddress;
    @Nullable
    @BindView(R.id.imageView)
    ImageView mImageView;
    @Nullable
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    @Nullable
    @BindView(R.id.restaurant_item_call)
    Button mButtonCall;
    @Nullable
    @BindView(R.id.restaurant_item_like)
    Button mButtonLike;
    @Nullable
    @BindView(R.id.restaurant_item_website)
    Button mButtonWebsite;
    @Nullable
    @BindView(R.id.item_ratingBar)
    RatingBar mRatingBar;
    @Nullable
    @BindView(R.id.restaurantRecyclerView)
    RecyclerView mRestaurantRecyclerView;

    private List<Workmate> mWorkmates = new ArrayList<>();
    private ResultDetails requestResult;
    private Restaurant_Details_RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);

        configureButtonClickListener();
        configureRecyclerView();
        requestRetrofit();
        setFloatingListener();
    }

    private void setFloatingListener() {
        mFloatingActionButton.setOnClickListener(view -> bookThisRestaurant());
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
                                Toast.makeText(this, getResources().getString(R.string.cancel_booking), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
                            if (tryingToBook) {
                                Booking_Firebase(userId, restaurantId, restaurantName, restaurant.getId(), false, true, false);
                                Toast.makeText(this, getResources().getString(R.string.modify_booking), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
                    if (tryingToBook) {
                        Booking_Firebase(userId, restaurantId, restaurantName, null, true, false, false);
                        Toast.makeText(this, getResources().getString(R.string.new_booking), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void displayFloating(int icon, int color) {
        Drawable mDrawable = ContextCompat.getDrawable(getBaseContext(), icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        mFloatingActionButton.setImageDrawable(mDrawable);
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
                    mButtonLike.setText(getResources().getString(R.string.like));
                } else {
                    for (DocumentSnapshot restaurant : task.getResult()) {
                        if (restaurant.getId().equals(requestResult.getPlaceId())) {
                            mButtonLike.setText(getResources().getString(R.string.unlike));
                            break;
                        } else {
                            mButtonLike.setText(getResources().getString(R.string.like));
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
                    Toast.makeText(this, getResources().getString(R.string.no_phone), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.restaurant_item_like:
                if (mButtonLike.getText().equals(getResources().getString(R.string.like))) {
                    likeRestaurant();
                } else {
                    dislikeRestaurant();
                }
                break;
            case R.id.restaurant_item_website:
                if (requestResult.getWebsite() != null) {
                    Intent intent = new Intent(this, WebView_Activity.class);
                    intent.putExtra("Website", requestResult.getWebsite());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.no_website), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void configureRecyclerView() {
        this.mAdapter = new Restaurant_Details_RecyclerViewAdapter(mWorkmates);
        this.mRestaurantRecyclerView.setAdapter(mAdapter);
        this.mRestaurantRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void requestRetrofit() {
        String result = getIntent().getStringExtra("PlaceDetailResult");
        Log.e("TAG", "retrieveObject: " + result);
        GooglePlaceDetailsCalls.fetchPlaceDetails(this, result);
    }


    private void configureButtonClickListener() {
        mButtonCall.setOnClickListener(this);
        mButtonLike.setOnClickListener(this);
        mButtonWebsite.setOnClickListener(this);
    }


    private void updateUI(ResultDetails resultDetails) {

        if (getCurrentUser() != null) {
            checkBooked(getCurrentUser().getUid(), requestResult.getPlaceId(), requestResult.getName(), false);
            checkLiked();
        } else {
            mButtonLike.setText(R.string.like);
            Toast.makeText(this, getResources().getString(R.string.restaurant_error), Toast.LENGTH_LONG).show();
            displayFloating((R.drawable.ic_check_circle_black_24dp), getResources().getColor(R.color.colorGreen));
        }

        // Chargement de la photo dans le détail des restaus
        if (resultDetails.getPhotos() != null) {
            String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
            int MAX_WIDTH = 400;
            int MAX_HEIGHT = 400;

            Picasso.get().load(BASE_URL + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + resultDetails.getPhotos().get(0)
                    .getPhotoReference() + "&key=" + BuildConfig.api_key).into(mImageView);


        } else {
            Glide.with(mImageView).load(R.drawable.ic_no_image_available)
                    .apply(new RequestOptions()

                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .into(mImageView);
        }


        mRestaurantName.setText(resultDetails.getName());
        mRestaurantAddress.setText(resultDetails.getVicinity());
        Update_Booking_RecyclerView(requestResult.getPlaceId());
        displayRating(resultDetails, mRatingBar);

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


    private void likeRestaurant() {
        if (requestResult != null && getCurrentUser() != null) {
            RestaurantsHelper.createLike(requestResult.getPlaceId(), getCurrentUser().getUid()).addOnCompleteListener(likeTask -> {
                if (likeTask.isSuccessful()) {
                    Toast.makeText(this, getResources().getString(R.string.rest_like), Toast.LENGTH_SHORT).show();
                    mButtonLike.setText(getResources().getString(R.string.unlike));
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.rest_like), Toast.LENGTH_SHORT).show();
        }
    }

    private void dislikeRestaurant() {
        if (requestResult != null && getCurrentUser() != null) {
            RestaurantsHelper.deleteLike(requestResult.getPlaceId(), getCurrentUser().getUid());
            mButtonLike.setText(getResources().getString(R.string.like));
            Toast.makeText(this, getResources().getString(R.string.rest_dislike), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.rest_like), Toast.LENGTH_SHORT).show();
        }
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
        Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
    }
}