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


public class ListRestaurantsRecyclerViewAdapter extends RecyclerView.Adapter<ListRestaurantsViewHolder> {
    private final List<ResultDetails> mResultDetails;
    private final String mLocation;

    public ListRestaurantsRecyclerViewAdapter(List<ResultDetails> items, String location) {
        mResultDetails = items;
        mLocation = location;
    }

    @NotNull
    @Override
    public ListRestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_item, parent, false);
        return new ListRestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRestaurantsViewHolder holder, int position) {
        holder.updateWithData(this.mResultDetails.get(position), this.mLocation);
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mResultDetails != null) itemCount = mResultDetails.size();
        return itemCount;
    }
}