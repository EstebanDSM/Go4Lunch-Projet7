package com.guzzler.go4lunch_p7.ui.restaurants_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class RestaurantsList_RecyclerViewAdapter extends RecyclerView.Adapter<RestaurantsList_ViewHolder> {
    private final List<ResultDetails> mResultDetails;
    private final String mLocation;


    public RestaurantsList_RecyclerViewAdapter(List<ResultDetails> items, String location) {
        mResultDetails = items;
        mLocation = location;
    }

    @NotNull
    @Override
    public RestaurantsList_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_item, parent, false);

        return new RestaurantsList_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsList_ViewHolder holder, int position) {
        holder.updateWithData(this.mResultDetails.get(position), this.mLocation);
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mResultDetails != null) itemCount = mResultDetails.size();
        return itemCount;
    }

    public ResultDetails getRestaurantDetails(int position) {
        return this.mResultDetails.get(position);
    }
}