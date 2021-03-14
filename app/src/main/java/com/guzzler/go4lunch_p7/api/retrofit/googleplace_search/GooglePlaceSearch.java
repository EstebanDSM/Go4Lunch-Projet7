package com.guzzler.go4lunch_p7.api.retrofit.googleplace_search;

import com.guzzler.go4lunch_p7.models.googleplaces_gson.SearchPlace;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface GooglePlaceSearch {
    String BASE_URL = "https://maps.googleapis.com";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/maps/api/place/nearbysearch/json")
    Call<SearchPlace> getNearbyRestaurants(@Query("location") String location,
                                           @Query("distance_ranking") String distanceRanking,
                                           @Query("type") String type,
                                           @Query("key") String key);
}