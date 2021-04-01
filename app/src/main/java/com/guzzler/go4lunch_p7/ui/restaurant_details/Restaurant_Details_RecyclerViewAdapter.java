package com.guzzler.go4lunch_p7.ui.restaurant_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.guzzler.go4lunch_p7.databinding.ActivityRestaurantDetailsItemBinding;
import com.guzzler.go4lunch_p7.models.Workmate;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Restaurant_Details_RecyclerViewAdapter extends RecyclerView.Adapter<Restaurant_Details_ViewHolder> {
    private final List<Workmate> mWorkmates;


    public Restaurant_Details_RecyclerViewAdapter(List<Workmate> result) {
        mWorkmates = result;
    }

    @NotNull
    @Override
    public Restaurant_Details_ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        ActivityRestaurantDetailsItemBinding view = ActivityRestaurantDetailsItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Restaurant_Details_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Restaurant_Details_ViewHolder viewHolder, int position) {
        viewHolder.updateWithData(this.mWorkmates.get(position));
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mWorkmates != null) itemCount = mWorkmates.size();
        return itemCount;
    }
}