package com.guzzler.go4lunch_p7.ui.map;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.retrofit.googleplace.GooglePlaceSearchCalls;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultSearch;
import com.guzzler.go4lunch_p7.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class Map_Fragment extends BaseFragment implements OnMapReadyCallback, LocationSource.OnLocationChangedListener, GooglePlaceSearchCalls.Callbacks {


    private static final String TAG = Map_Fragment.class.getSimpleName();
    private static final int DEFAULT_ZOOM = 13;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static List<ResultSearch> mResultSearchList = new ArrayList<>();
    public static String mLocation;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng defaultLocation = new LatLng(44.8333, -0.5667);
    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean locationPermissionGranted;
    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Build the map.
        SupportMapFragment mMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        getDeviceLocation();
    }


    @OnClick(R.id.fragment_map_floating_action_btn)
    public void displayLocation() {
        getDeviceLocation();
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
        MenuItem item = menu.findItem(R.id.menu_activity_main_search);
    }


    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        // TODO : la premiere fois qu'on lance l'appli on est pas géolocalisé, si on recharge le fragment map en naviguant dans les onglets c'est bon  >>> à corriger
        // TODO : si pas de GPS erreur >>>> à corriger

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.getResult();
                        mLocation = lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude();
                        Log.e("TAG", "DeviceLocation " + mLocation);
                        if (lastKnownLocation != null) {
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            // RETROFIT
                            GooglePlaceSearchCalls.fetchNearbyRestaurants(this, mLocation);

                            Log.e("TAG", "DeviceLocation " + mLocation);
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


    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                map.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity().getApplicationContext(), R.raw.style_json));
                map.getUiSettings().setMapToolbarEnabled(false);
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
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        getDeviceLocation();
        updateLocationUI();
    }


    private void getLocationPermission() {

//         * Request location permission, so that we can get the location of the
//         * device. The result of the permission request is handled by a callback,
//         * onRequestPermissionsResult.

        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        getLocationPermission();
        getDeviceLocation();
        updateLocationUI();
        Log.e("test_onMapReady", "onMapReady");
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.e("test_LocationChanged", "onLocationChanged" + location);
    }

    @Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {
        map.clear();       //on reset la carte pour effacer marqueur ?
        mResultSearchList = resultSearchList;
        if (mResultSearchList.size() > 0) {
            for (int i = 0; i < mResultSearchList.size(); i++) {
                Double lat = mResultSearchList.get(i).getGeometry().getLocation().getLat();
                Double lng = mResultSearchList.get(i).getGeometry().getLocation().getLng();
                String title = mResultSearchList.get(i).getName();

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                markerOptions.position(new LatLng(lat, lng));
                markerOptions.title(title);
                Marker marker = map.addMarker(markerOptions);
                marker.setTag(mResultSearchList.get(i).getPlaceId());
            }
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.no_restaurant), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure() {
    }
}