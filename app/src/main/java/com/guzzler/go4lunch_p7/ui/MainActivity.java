package com.guzzler.go4lunch_p7.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.retrofit.googleplace.GooglePlaceDetailsCalls;
import com.guzzler.go4lunch_p7.api.retrofit.googleplace.GooglePlaceSearchCalls;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GooglePlaceSearchCalls.Callbacks, GooglePlaceDetailsCalls.Callbacks, LocationListener {
    private static final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public List<ResultDetails> mResultDetailsList = new ArrayList<>();


    //VIEWMODEL
    public SharedViewModel mShareViewModel;

    //LIVEDATA
    public MutableLiveData<List<ResultDetails>> mLiveData = new MutableLiveData<>();


    //FOR DESIGN
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView imageProfileView;
    TextView emailUser;
    TextView nameUser;
    ImageView backgroundView;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        // VIEWMODEL
        mShareViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        checkLocationPermission();

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                double currentLatitude = location.getLatitude();
                double currentLongitude = location.getLongitude();
                mShareViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
                GooglePlaceSearchCalls.fetchNearbyRestaurants(this, mShareViewModel.getCurrentUserPositionFormatted());
            }
        });


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        // Configure Toolbar
        this.configureToolBar();

        // Configure Navigation Drawer
        this.configureDrawerLayout();
        this.configureNavigationView();

        if (this.getCurrentUser() != null) {
            startSignInActivity();
        }

        this.updateView();
    }

    @Override
    protected void onResume() {
        this.updateView();
        super.onResume();
    }

    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        final View headerLayout = navigationView.getHeaderView(0);
        imageProfileView = headerLayout.findViewById(R.id.imageProfileView);
        emailUser = headerLayout.findViewById(R.id.emailUser);
        nameUser = headerLayout.findViewById(R.id.nameUser);
        backgroundView = headerLayout.findViewById(R.id.background_header_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    private void startSignInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Gestion de la navigation au click sur la Navigation Drawer
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            this.signOutFirebase();
        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOutFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.startLogin_AfterSignOut());
    }

    private OnSuccessListener<Void> startLogin_AfterSignOut() {
        return aVoid -> {
            startSignInActivity();
        };
    }

    // On récupère la toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    //On récupère le DrawerLayout, avec lui et la toolbar on créé le menu hamburger
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_appbar, menu);
        return true;
    }

    private void updateView() {
        //Chargement infos user dans Navigation Drawer
        if (this.getCurrentUser() != null) {
            Glide.with(this)
                    .load(Objects.requireNonNull(this.getCurrentUser()).getPhotoUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageProfileView);
            String email = TextUtils.isEmpty(this.getCurrentUser().getEmail()) ? getString(R.string.email_not_found) : this.getCurrentUser().getEmail();
            String username = TextUtils.isEmpty(this.getCurrentUser().getDisplayName()) ? getString(R.string.username_not_found) : this.getCurrentUser().getDisplayName();
            this.nameUser.setText(username);
            this.emailUser.setText(email);

            //Chargement fond d'écran en haut navigation drawer
            Glide.with(this)
                    .load(R.drawable.background_nav_header)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(30)))
                    .into(backgroundView);
        }
    }

    @Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {
        mResultDetailsList.clear();
        for (int i = 0; i < resultSearchList.size(); i++) {
            GooglePlaceDetailsCalls.fetchPlaceDetails(this, resultSearchList.get(i).getPlaceId());
        }
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        if (resultDetails.getTypes().contains("restaurant")) {
            mResultDetailsList.add(resultDetails);
            mLiveData.setValue(mResultDetailsList);
        }
    }

    @Override
    public void onFailure() {
        Toast.makeText(this.getApplicationContext(), getResources().getString(R.string.no_restaurant_found), Toast.LENGTH_SHORT).show();
    }

    /**
     * @param location
     */
    @Override
    public void onLocationChanged(@NonNull Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        mShareViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
    }

    /**
     * @return boolean
     */
    public boolean checkLocationPermission() {
        return EasyPermissions.hasPermissions(getApplicationContext(), permissions);
    }
}