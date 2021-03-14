package com.guzzler.go4lunch_p7.ui.list_restaurants;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultSearch;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListRestaurantsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name_restaurant)
    public TextView mNameRestaurant;


    public ListRestaurantsViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(ResultSearch resultSearch, String location) {
        this.mNameRestaurant.setText(resultSearch.getName());
    }
}
