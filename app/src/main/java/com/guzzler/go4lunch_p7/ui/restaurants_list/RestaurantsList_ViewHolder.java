package com.guzzler.go4lunch_p7.ui.restaurants_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.guzzler.go4lunch_p7.BuildConfig;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.Location;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestaurantsList_ViewHolder extends RecyclerView.ViewHolder {
    private static float[] distanceResults = new float[3];
    public final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public final int MAX_WIDTH = 200;
    public final int MAX_HEIGHT = 200;
    @BindView(R.id.name_restaurant)
    public TextView mNameRestaurant;
    @BindView(R.id.adress_restaurant)
    public TextView mAdressRestaurant;
    @BindView(R.id.item_avatar_restaurant)
    public ImageView mAvatarRestaurant;
    @BindView(R.id.distance_restaurant)
    public TextView mDistance;
    @BindView(R.id.star_restaurant)
    public RatingBar mStar;


    public RestaurantsList_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(ResultDetails resultDetails, String location) {
        RequestManager glide = Glide.with(itemView);

        //NAME
        this.mNameRestaurant.setText(resultDetails.getName());

        //ADRESS
        this.mAdressRestaurant.setText(resultDetails.getFormattedAddress());

        /* TODO : les distances ne sont pas triées dans la recyclerview */
        //DISTANCE
        this.mDistance.setText(itemView.getResources().getString(R.string.unit_distance, getDistance(location, resultDetails.getGeometry().getLocation())));

        //RATING
        displayRating(resultDetails);


        //Chargement de la photo dans la liste des restaus
        if (!(resultDetails.getPhotos() == null)) {
            if (!(resultDetails.getPhotos().isEmpty())) {
                glide.load(BASE_URL + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + resultDetails.getPhotos().get(0)
                        .getPhotoReference() + "&key=" + BuildConfig.api_key)
                        .apply(RequestOptions.centerCropTransform()).into(mAvatarRestaurant);
            }
        } else {
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.centerCropTransform()).into(mAvatarRestaurant);
        }
    }

    private void displayRating(ResultDetails resultDetails) {
        if (resultDetails.getRating() != null) {
            double googleRating = resultDetails.getRating();
            double rating = googleRating / 5 * 3;
            this.mStar.setRating((float) rating);
            this.mStar.setVisibility(View.VISIBLE);
        } else {
            this.mStar.setVisibility(View.GONE);
        }
    }

    private String getDistance(String startLocation, Location endLocation) {
        String[] separatedStart = startLocation.split(",");
        double startLatitude = Double.parseDouble(separatedStart[0]);
        double startLongitude = Double.parseDouble(separatedStart[1]);
        LatLng location = new LatLng(startLatitude, startLongitude);
        return String.valueOf(Math.round(SphericalUtil.computeDistanceBetween(location, new LatLng(endLocation.getLat(), endLocation.getLng()))));
    }
}
