package com.guzzler.go4lunch_p7.ui.restaurants_list;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.guzzler.go4lunch_p7.BuildConfig;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RestaurantsList_ViewHolder extends RecyclerView.ViewHolder {
    public final String BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    public final int MAX_WIDTH = 200;
    public final int MAX_HEIGHT = 200;

    @BindView(R.id.name_restaurant)
    public TextView mNameRestaurant;
    @BindView(R.id.adress_restaurant)
    public TextView mAdressRestaurant;
    @BindView(R.id.item_avatar_restaurant)
    public ImageView mAvatarRestaurant;


    public RestaurantsList_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(ResultDetails resultDetails, String location) {
        RequestManager glide = Glide.with(itemView);

        this.mNameRestaurant.setText(resultDetails.getName());
        this.mAdressRestaurant.setText(resultDetails.getVicinity());

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
}
