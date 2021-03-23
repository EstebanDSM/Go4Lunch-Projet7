package com.guzzler.go4lunch_p7.api.retrofit.googleplace;

import com.guzzler.go4lunch_p7.models.googleplaces_gson.PlaceDetails;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.guzzler.go4lunch_p7.utils.Constants.BASE_URL_GOOGLE_API;


public interface GooglePlaceDetailsService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL_GOOGLE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/maps/api/place/details/json")
    Call<PlaceDetails> getDetails(@Query("place_id") String place_id,
                                  @Query("key") String key);
}
