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

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.guzzler.go4lunch_p7.BuildConfig;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.RestaurantsHelper;
import com.guzzler.go4lunch_p7.api.retrofit.googleplace.GooglePlaceDetailsCalls;
import com.guzzler.go4lunch_p7.models.Workmate;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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

    private ResultDetails requestResult;
    private List<Workmate> mWorkmates = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        ButterKnife.bind(this);


        this.configureButtonClickListener();
        this.requestRetrofit();
    }

    private void checkIfUserLikeThisRestaurant() {
        RestaurantsHelper.getAllLikeByUserId(getCurrentUser().getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e("TAG", "checkIfUserLikeThisRestaurant: " + task.getResult().getDocuments());
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
                    this.likeRestaurant();
                } else {
                    this.dislikeRestaurant();
                }
                break;
        }
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


    private void displayRating(ResultDetails results) {
        if (results.getRating() != null) {
            double googleRating = results.getRating();
            double MAX_RATING = 5;
            double MAX_STAR = 3;
            double rating = googleRating / MAX_RATING * MAX_STAR;
            this.mRatingBar.setRating((float) rating);
            this.mRatingBar.setVisibility(View.VISIBLE);
        } else {
            this.mRatingBar.setVisibility(View.GONE);
        }
    }

    private void updateUI(ResultDetails resultDetails) {
        RequestManager glide = Glide.with(this);

        if (getCurrentUser() != null) {
            this.checkIfUserLikeThisRestaurant();
        } else {
            mButtonLike.setText(R.string.like);
            Toast.makeText(this, getResources().getString(R.string.restaurant_error), Toast.LENGTH_LONG).show();
        }

        // Chargement de la photo dans le détail des restaus
        if (resultDetails.getPhotos() != null) {
            String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
            int MAX_WIDTH = 200;
            int MAX_HEIGHT = 200;
            glide.load(BASE_URL + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + resultDetails.getPhotos().get(0)
                    .getPhotoReference() + "&key=" + BuildConfig.api_key)
                    .apply(RequestOptions.centerCropTransform()).into(mImageView);


        } else {
            Glide.with(mImageView).load(R.drawable.ic_no_image_available)
                    .apply(new RequestOptions()

                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .override(Target.SIZE_ORIGINAL))
                    .into(mImageView);
        }


        mRestaurantName.setText(resultDetails.getName());
        mRestaurantAddress.setText(resultDetails.getVicinity());
        this.displayRating(resultDetails);
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

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        this.requestResult = resultDetails;
        updateUI(resultDetails);
    }

    @Override
    public void onFailure() {
    }


}