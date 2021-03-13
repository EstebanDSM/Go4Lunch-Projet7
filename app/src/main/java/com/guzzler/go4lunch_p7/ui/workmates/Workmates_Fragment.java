package com.guzzler.go4lunch_p7.ui.workmates;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.UserHelper;
import com.guzzler.go4lunch_p7.models.Workmate;
import com.guzzler.go4lunch_p7.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;

import static com.firebase.ui.auth.ui.email.EmailLinkFragment.TAG;

public class Workmates_Fragment extends BaseFragment {
    private List<Workmate> mWorkmates = new ArrayList<>();
    private WorkmatesRecyclerViewAdapter mViewAdapter;
    private RecyclerView mRecyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workmates, container, false);
        ButterKnife.bind(this, view);
        Context context = view.getContext();
        setHasOptionsMenu(true);
        initList();
        mRecyclerView = (RecyclerView) view;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        this.mViewAdapter = new WorkmatesRecyclerViewAdapter(this.mWorkmates);
        this.mRecyclerView.setAdapter(this.mViewAdapter);
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
                    mRecyclerView.setAdapter(new WorkmatesRecyclerViewAdapter(mWorkmates));
                });
    }
}