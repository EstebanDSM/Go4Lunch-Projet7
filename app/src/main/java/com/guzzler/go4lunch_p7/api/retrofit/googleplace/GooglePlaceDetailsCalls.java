package com.guzzler.go4lunch_p7.api.retrofit.googleplace;

import androidx.annotation.Nullable;

import com.guzzler.go4lunch_p7.models.googleplaces_gson.PlaceDetails;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.guzzler.go4lunch_p7.utils.Constants.APIKEY;

// classe exécutant notre appel vers l'API googlePlaceDetailsService en arrière plan.
public class GooglePlaceDetailsCalls {

    // 2 - Public method to start fetching nearby restaurants
    public static void fetchPlaceDetails(Callbacks callbacks, String place_id) {

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        GooglePlaceDetailsService googlePlaceDetailsService = GooglePlaceDetailsService.retrofit.create(GooglePlaceDetailsService.class);

        // 2.3 - Create the call on googlePlaceSearchService
        Call<PlaceDetails> call = googlePlaceDetailsService.getDetails(place_id, APIKEY);

        // 2.4 - Start the call
        call.enqueue(new Callback<PlaceDetails>() {

            @Override
            public void onResponse(Call<PlaceDetails> call, Response<PlaceDetails> response) {

                // 2.5 - Call the proper callback used in controller
                if (callbacksWeakReference.get() != null) {
                    PlaceDetails placeDetails = response.body();
                    ResultDetails resultDetails = placeDetails.getResultDetails();
                    callbacksWeakReference.get().onResponse(resultDetails);
                }
            }

            @Override
            public void onFailure(Call<PlaceDetails> call, Throwable t) {

                // 2.5 - Call the proper callback used in controller
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable ResultDetails resultDetails);

        void onFailure();
    }
}