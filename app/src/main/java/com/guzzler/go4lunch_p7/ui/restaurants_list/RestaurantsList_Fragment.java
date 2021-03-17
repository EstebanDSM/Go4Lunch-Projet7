package com.guzzler.go4lunch_p7.ui.restaurants_list;

import android.content.Context;
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
import com.guzzler.go4lunch_p7.ui.BaseFragment;
import com.guzzler.go4lunch_p7.ui.MainActivity;


public class RestaurantsList_Fragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private MainActivity mMainActivity;

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
        configureRecyclerView();
        getActivity().setTitle(getString(R.string.Titre_Toolbar_hungry));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configureRecyclerView() {
        RestaurantsList_RecyclerViewAdapter mViewAdapter = new RestaurantsList_RecyclerViewAdapter(mMainActivity.mLiveData.getValue(), mMainActivity.mShareViewModel.getCurrentUserPositionFormatted());
        this.mRecyclerView.setAdapter(mViewAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


}