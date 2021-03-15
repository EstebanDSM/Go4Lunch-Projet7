package com.guzzler.go4lunch_p7.ui.list_restaurants;

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
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultSearch;
import com.guzzler.go4lunch_p7.ui.BaseFragment;
import com.guzzler.go4lunch_p7.ui.map.Map_Fragment;

import java.util.List;


public class ListRestaurants_Fragment extends BaseFragment {
    List<ResultSearch> resultSearchList = Map_Fragment.mResultSearchList;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        Context context = view.getContext();

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        setHasOptionsMenu(true);
        configureRecyclerView();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void configureRecyclerView() {
        ListRestaurantsRecyclerViewAdapter mViewAdapter = new ListRestaurantsRecyclerViewAdapter(this.resultSearchList, Map_Fragment.mLocation);
        this.mRecyclerView.setAdapter(mViewAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}