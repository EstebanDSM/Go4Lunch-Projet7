package com.guzzler.go4lunch_p7.ui.workmates;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.Workmate;

import java.util.List;


public class WorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {
    private List<Workmate> mWorkmates;


    public WorkmatesRecyclerViewAdapter(List<Workmate> items) {
        mWorkmates = items;
    }


    @Override
    public WorkmatesViewHolder onCreateViewHolder(ViewGroup parent, int viewtype) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_workmate_item, parent, false);
        return new WorkmatesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        holder.updateWithData(this.mWorkmates.get(position));
    }


    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (mWorkmates != null) itemCount = mWorkmates.size();
        return itemCount;
    }

    public Workmate getWorkmates(int position) {
        return this.mWorkmates.get(position);
    }
}
