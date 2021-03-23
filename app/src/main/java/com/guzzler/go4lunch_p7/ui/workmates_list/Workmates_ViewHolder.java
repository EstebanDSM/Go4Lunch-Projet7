package com.guzzler.go4lunch_p7.ui.workmates_list;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.RestaurantsHelper;
import com.guzzler.go4lunch_p7.models.Workmate;
import com.guzzler.go4lunch_p7.utils.ChangeColorWorkmate;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.guzzler.go4lunch_p7.utils.GetTodayDate.getTodayDate;

public class Workmates_ViewHolder extends RecyclerView.ViewHolder {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.item_list_avatar)
    public ImageView mWorkmateAvatar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.item_list_name)
    public TextView mWorkmateName;


    public Workmates_ViewHolder(View itemview) {
        super(itemview);

        ButterKnife.bind(this, itemview);
    }


    public void updateWithData(Workmate result) {

        RequestManager glide = Glide.with(itemView);
        if (result.getUrlPicture() != null) {
            glide.load(result.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mWorkmateAvatar);
        } else {
            glide.load(R.drawable.ic_anon_user_48dp).apply(RequestOptions.circleCropTransform()).into(mWorkmateAvatar);
        }
        RestaurantsHelper.getBooking(result.getUid(), getTodayDate()).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                if (restaurantTask.getResult().size() == 1) { // This user has made his (only) choice
                    for (QueryDocumentSnapshot restaurant : restaurantTask.getResult()) {
                        this.mWorkmateName.setText(itemView.getResources().getString(R.string.eating_at, result.getName(), restaurant.getData().get("restaurantName")));//
                        mWorkmateName.setTypeface(mWorkmateName.getTypeface(), Typeface.BOLD);
                    }

                    //  This user hasn't decided today
                } else {
                    mWorkmateName.setText(itemView.getResources().getString(R.string.hasnt_decided, result.getName()));
                    ChangeColorWorkmate.changeTextColor(R.color.colorGrey1, mWorkmateName);
                    mWorkmateName.setTypeface(mWorkmateName.getTypeface(), Typeface.ITALIC);
                }
            }
        });
    }
}
