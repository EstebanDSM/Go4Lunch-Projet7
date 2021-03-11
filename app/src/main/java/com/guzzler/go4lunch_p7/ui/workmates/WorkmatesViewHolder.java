package com.guzzler.go4lunch_p7.ui.workmates;

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

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.item_list_avatar)
    public ImageView mWorkmateAvatar;
    @BindView(R.id.item_list_name)
    public TextView mWorkmateName;


    public WorkmatesViewHolder(View itemview) {
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

        mWorkmateName.setText(result.getName());

    }

}