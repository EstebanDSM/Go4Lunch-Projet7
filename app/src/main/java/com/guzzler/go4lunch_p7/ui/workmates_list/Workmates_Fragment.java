package com.guzzler.go4lunch_p7.ui.workmates_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.RestaurantsHelper;
import com.guzzler.go4lunch_p7.api.firebase.UserHelper;
import com.guzzler.go4lunch_p7.models.Workmate;
import com.guzzler.go4lunch_p7.ui.BaseFragment;
import com.guzzler.go4lunch_p7.ui.restaurant_details.Restaurant_Details;
import com.guzzler.go4lunch_p7.utils.ItemClickSupport;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static com.firebase.ui.auth.ui.email.EmailLinkFragment.TAG;
import static com.guzzler.go4lunch_p7.utils.GetTodayDate.getTodayDate;
import static com.guzzler.go4lunch_p7.utils.ShowToastSnack.showToast;

public class Workmates_Fragment extends BaseFragment {
    private final List<Workmate> mWorkmates = new ArrayList<>();
    private Workmates_RecyclerViewAdapter mViewAdapter;
    private RecyclerView mRecyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workmates_list, container, false);
        Context context = view.getContext();
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        configureOnClickRecyclerView();
        this.mViewAdapter = new Workmates_RecyclerViewAdapter(this.mWorkmates);
        this.mRecyclerView.setAdapter(this.mViewAdapter);
        requireActivity().setTitle(getString(R.string.Titre_Toolbar_workmates));
        return view;
    }

    private void initList() {
        UserHelper.getWorkmatesCollection()
                .get()
                .addOnCompleteListener(task -> {
                    mWorkmates.clear();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mWorkmates.add(document.toObject(Workmate.class));
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                    Collections.sort(mWorkmates, new Comparator<Workmate>() {
                        public int compare(Workmate obj1, Workmate obj2) {
                            return obj1.getUid().compareToIgnoreCase(obj2.getUid());
                        }
                    });
                    mRecyclerView.setAdapter(new Workmates_RecyclerViewAdapter(mWorkmates));
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }

    private void configureOnClickRecyclerView() {
        ItemClickSupport.addTo(mRecyclerView, R.layout.fragment_workmates_list)
                .setOnItemClickListener((recyclerView, position, v) -> {
                    Workmate result = mViewAdapter.getWorkmates(position);
                    checkIfWorkmateHasBooking(result);
                });
    }

    private void startRestaurantDetails(String placeId) {
        Intent intent = new Intent(getActivity(), Restaurant_Details.class);
        intent.putExtra("PlaceDetailResult", placeId);
        startActivity(intent);
    }

    private void checkIfWorkmateHasBooking(Workmate workmate) {
        RestaurantsHelper.getBooking(workmate.getUid(), getTodayDate()).addOnCompleteListener(bookingTask -> {
            if (bookingTask.isSuccessful()) {
                if (!(Objects.requireNonNull(bookingTask.getResult()).isEmpty())) {
                    for (QueryDocumentSnapshot booking : bookingTask.getResult()) {
                        startRestaurantDetails(Objects.requireNonNull(booking.getData().get("restaurantId")).toString());
                    }
                } else {
                    showToast(getContext(), getResources().getString(R.string.hasnt_decided, workmate.getName()), Toast.LENGTH_SHORT);
                }
            }
        });
    }
}