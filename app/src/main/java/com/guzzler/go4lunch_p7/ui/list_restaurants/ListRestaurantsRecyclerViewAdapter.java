package com.guzzler.go4lunch_p7.ui.list_restaurants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultSearch;

import java.util.List;


public class ListRestaurantsRecyclerViewAdapter extends RecyclerView.Adapter<ListRestaurantsViewHolder> {
    private List<ResultSearch> mResultSearch;
    private String mLocation;

    public ListRestaurantsRecyclerViewAdapter(List<ResultSearch> items, String location) {
        mResultSearch = items;
        mLocation = location;
    }

    @Override
    public ListRestaurantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_restaurant_item, parent, false);
        return new ListRestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListRestaurantsViewHolder holder, int position) {
        holder.updateWithData(this.mResultSearch.get(position), this.mLocation);
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mResultSearch != null) itemCount = mResultSearch.size();
        return itemCount;
    }
}