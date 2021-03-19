package com.guzzler.go4lunch_p7.ui.restaurants_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;
import com.guzzler.go4lunch_p7.ui.BaseFragment;
import com.guzzler.go4lunch_p7.ui.MainActivity;
import com.guzzler.go4lunch_p7.ui.restaurant_details.Restaurant_Details;
import com.guzzler.go4lunch_p7.utils.ItemClickSupport;

import java.util.Collections;
import java.util.List;


public class RestaurantsList_Fragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private MainActivity mMainActivity;
    private RestaurantsList_RecyclerViewAdapter mViewAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        Context context = view.getContext();

        // LIVEDATA
        mMainActivity.mLiveData.observe(getViewLifecycleOwner(), resultDetails -> configureRecyclerView());

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        setHasOptionsMenu(true);
        configureOnClickRecyclerView();
        getActivity().setTitle(getString(R.string.Titre_Toolbar_hungry));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configureRecyclerView() {
        this.mViewAdapter = new RestaurantsList_RecyclerViewAdapter(mMainActivity.mLiveData.getValue(), mMainActivity.mShareViewModel.getCurrentUserPositionFormatted());
        List<ResultDetails> mResult = mMainActivity.mLiveData.getValue();
        Collections.sort(mResult);
        RestaurantsList_RecyclerViewAdapter mViewAdapter = new RestaurantsList_RecyclerViewAdapter(mResult, mMainActivity.mShareViewModel.getCurrentUserPositionFormatted());
        this.mRecyclerView.setAdapter(mViewAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_restaurant_item)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    ResultDetails result = mViewAdapter.getRestaurantDetails(position);
                    Intent intent = new Intent(getActivity(), Restaurant_Details.class);
                    intent.putExtra("PlaceDetailResult", result.getPlaceId());
                    startActivity(intent);
                });
    }
}