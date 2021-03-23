package com.guzzler.go4lunch_p7.ui.restaurants_list;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.guzzler.go4lunch_p7.BuildConfig;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.RestaurantsHelper;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;
import com.guzzler.go4lunch_p7.utils.DisplayRating;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.guzzler.go4lunch_p7.utils.FormatTime.formatTime;
import static com.guzzler.go4lunch_p7.utils.GetTodayDate.getTodayDate;


public class RestaurantsList_ViewHolder extends RecyclerView.ViewHolder {
    private static final String OPEN = "OPEN";
    private static final String CLOSED = "CLOSED";
    private static final String CLOSING_SOON = "CLOSING_SOON";
    private static final String OPENING_HOURS_NOT_KNOW = "OPENING_HOURS_NOT_KNOW";
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
    @BindView(R.id.Open_hour)
    public TextView mOpenHour;


    public RestaurantsList_ViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(ResultDetails resultDetails, String mLocation) {
        RequestManager glide = Glide.with(itemView);

        // NAME
        mNameRestaurant.setText(resultDetails.getName());

        // ADDRESS
        mAdressRestaurant.setText(resultDetails.getFormattedAddress());

        // DISTANCE
        mDistance.setText(itemView.getResources().getString(R.string.unit_distance, String.valueOf(resultDetails.getDistance())));

        // RATING
        DisplayRating.displayRating(resultDetails, mStar);

        // PHOTO RESTAURANT
        if (!(resultDetails.getPhotos() == null)) {
            if (!(resultDetails.getPhotos().isEmpty())) {
                glide.load(BASE_URL + "?maxwidth=" + MAX_WIDTH + "&maxheight=" + MAX_HEIGHT + "&photoreference=" + resultDetails.getPhotos().get(0)
                        .getPhotoReference() + "&key=" + BuildConfig.api_key)
                        .apply(RequestOptions.centerCropTransform()).into(mAvatarRestaurant);
            }
        } else {
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.centerCropTransform()).into(mAvatarRestaurant);
        }

        // OPENING HOURS
        if (resultDetails.getOpeningHours() != null) {
            if (resultDetails.getOpeningHours().getOpenNow().toString().equals("false")) {
                displayOpeningHour(CLOSED, null);
            } else {
                getOpeningHoursInfo(resultDetails);
            }
        } else {
            displayOpeningHour(OPENING_HOURS_NOT_KNOW, null);
        }

        // delete past booking
        RestaurantsHelper.deleteNotTodayBooking(getTodayDate());
    }


    private void getOpeningHoursInfo(ResultDetails resultDetails) {
        int[] daysArray = {0, 1, 2, 3, 4, 5, 6};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minOfDay = calendar.get(Calendar.MINUTE);
        if (minOfDay < 10) {
            minOfDay = '0' + minOfDay;
        }
        String currentHourString = hourOfDay + String.valueOf(minOfDay);
        int currentHour = Integer.parseInt(currentHourString);

        for (int i = 0; i < resultDetails.getOpeningHours().getPeriods().size(); i++) {
            if (resultDetails.getOpeningHours().getPeriods().get(i).getOpen().getDay() == daysArray[day] && resultDetails.getOpeningHours().getPeriods().get(i).getClose() != null) {
                String closeHour = resultDetails.getOpeningHours().getPeriods().get(i).getClose().getTime();
                if (currentHour < Integer.parseInt(closeHour) || daysArray[day] < resultDetails.getOpeningHours().getPeriods().get(i).getClose().getDay()) {
                    int timeDifference = Integer.parseInt(closeHour) - currentHour;
                    if (timeDifference <= 30 && daysArray[day] == resultDetails.getOpeningHours().getPeriods().get(i).getClose().getDay()) {
                        displayOpeningHour(CLOSING_SOON, closeHour);
                    } else {
                        displayOpeningHour(OPEN, resultDetails.getOpeningHours().getPeriods().get(i).getClose().getTime());
                    }
                }
                break;
            }
        }
    }


    private void displayOpeningHour(String type, String hour) {
        switch (type) {
            case OPEN:
                this.mOpenHour.setText(itemView.getResources().getString(R.string.open_until, formatTime(itemView.getContext(), hour)));
                this.mOpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorGreen));
                this.mOpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case CLOSED:
                this.mOpenHour.setText(R.string.restaurant_closed);
                this.mOpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorError));
                this.mOpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case CLOSING_SOON:
                this.mOpenHour.setText(itemView.getResources().getString(R.string.closing_soon, formatTime(itemView.getContext(), hour)));
                this.mOpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorCloseSoon));
                this.mOpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case OPENING_HOURS_NOT_KNOW:
                this.mOpenHour.setText(R.string.restaurant_opening_not_know);
                this.mOpenHour.setTextColor(itemView.getContext().getResources().getColor(R.color.colorCloseSoon));
                this.mOpenHour.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
        }
    }


}
