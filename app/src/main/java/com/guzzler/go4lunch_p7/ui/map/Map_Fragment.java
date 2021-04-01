package com.guzzler.go4lunch_p7.ui.map;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Task;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.databinding.FragmentMapBinding;
import com.guzzler.go4lunch_p7.ui.BaseFragment;
import com.guzzler.go4lunch_p7.ui.MainActivity;
import com.guzzler.go4lunch_p7.ui.restaurant_details.Restaurant_Details;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.content.ContentValues.TAG;
import static com.guzzler.go4lunch_p7.utils.Constants.DEFAULT_ZOOM;
import static com.guzzler.go4lunch_p7.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.guzzler.go4lunch_p7.utils.ShowToastSnack.showToast;
import static com.guzzler.go4lunch_p7.utils.UpdateMarkers.updateMarkers;

public class Map_Fragment extends BaseFragment implements OnMapReadyCallback, LocationListener {

    private LatLng defaultLocation = new LatLng(44.8333, -0.5667);
    private String mLocation;
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    //    private boolean locationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider
    private Location lastKnownLocation;
    private MainActivity mMainActivity;

    private FragmentMapBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMapBinding.inflate(getLayoutInflater());

        mMainActivity = (MainActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = binding.getRoot();


        binding.fragmentMapFloatingActionBtn.setOnClickListener(v -> {
            if (getLocationPermission()) {
                getDeviceLocation();
            }
        });

        // LIVEDATA
        mMainActivity.mLiveData.observe(getViewLifecycleOwner(), resultDetails -> getDeviceLocation());

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Construct a FusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Build the map
        SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mMapFragment != null;
        mMapFragment.getMapAsync(this);

        requireActivity().setTitle(getString(R.string.Titre_Toolbar_hungry));
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (getLocationPermission()) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        if (lastKnownLocation != null) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();

                            mLocation = lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();

                            Log.e("TAG", "DeviceLocation " + mLocation);

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            Log.e("TAG", "DeviceLocation " + mLocation);
                        } else {
                            if (mMainActivity.mShareViewModel.getCurrentUserPosition() != null) {
                                defaultLocation = mMainActivity.mShareViewModel.getCurrentUserPosition();
                            }


                            Log.e(TAG, "LastLocation is null. Using current from mShareViewmodel.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);

                            // UPDATE MARKERS
                            updateMarkers(map, mMainActivity);
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        map.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation();
                updateLocationUI();
            }
        }
    }

    private boolean getLocationPermission() {

//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.

        if (ContextCompat.checkSelfPermission(requireActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
    }

    private boolean onClickMarker(Marker marker) {
        if (marker.getTag() != null) {
            Log.e(TAG, "onClickMarker: " + marker.getTag());
            Intent intent = new Intent(getActivity(), Restaurant_Details.class);
            intent.putExtra("PlaceDetailResult", marker.getTag().toString());
            startActivity(intent);
            return true;
        } else {
            Log.e(TAG, "onClickMarker: ERROR NO TAG");
            return false;
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (getLocationPermission()) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireActivity().getApplicationContext(), R.raw.style_json));
                map.getUiSettings().setMapToolbarEnabled(false);
                map.setOnMarkerClickListener(this::onClickMarker);

            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        if (getLocationPermission()) {
            getDeviceLocation();
            updateLocationUI();
            Log.e("test_onMapReady", "onMapReady");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        mMainActivity.mShareViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.activity_main_appbar, menu);
        SearchView mSearchView = new SearchView(Objects.requireNonNull(((MainActivity) requireContext()).getSupportActionBar()).getThemedContext());
        MenuItem item = menu.findItem(R.id.menu_activity_main_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(mSearchView);
        mSearchView.setQueryHint(getResources().getString(R.string.search_hint));
        SearchManager mSearchManager = (SearchManager) requireContext().getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(mSearchManager.getSearchableInfo(((MainActivity) requireContext()).getComponentName()));
        mSearchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2) {
                    mMainActivity.googleAutoCompleteSearch(query);
                    mSearchView.clearFocus();
                } else {
                    showToast(getContext(), getResources().getString(R.string.search_too_short), 1);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.length() > 2) {
                    mMainActivity.googleAutoCompleteSearch(query);
                } else if (query.length() == 0) {
                    mMainActivity.searchByCurrentPosition();
                }
                return false;
            }
        });
    }
}