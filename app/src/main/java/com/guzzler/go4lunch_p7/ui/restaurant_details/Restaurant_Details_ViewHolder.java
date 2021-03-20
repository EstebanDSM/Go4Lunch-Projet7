package com.guzzler.go4lunch_p7.ui.restaurant_details;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.Workmate;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Restaurant_Details_ViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.detail_main_picture)
    ImageView mImageView;
    @BindView(R.id.detail_textview_username)
    TextView mTextView;

    public Restaurant_Details_ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(Workmate results) {
        RequestManager glide = Glide.with(itemView);
        if (!(results.getUrlPicture() == null)) {
            glide.load(results.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(mImageView);
        } else {
            glide.load(R.drawable.ic_anon_user_48dp).apply(RequestOptions.circleCropTransform()).into(mImageView);
        }
        this.mTextView.setText(itemView.getResources().getString(R.string.restaurant_recyclerview, results.getName()));
        this.changeTextColor();
    }

    private void changeTextColor() {
        int mColor = itemView.getContext().getResources().getColor(R.color.colorBlack);
        this.mTextView.setTextColor(mColor);
    }
}