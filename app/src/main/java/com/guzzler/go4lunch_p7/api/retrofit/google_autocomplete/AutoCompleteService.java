package com.guzzler.go4lunch_p7.api.retrofit.google_autocomplete;

import com.guzzler.go4lunch_p7.models.google_autocomplete_gson.AutoCompleteResult;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.guzzler.go4lunch_p7.utils.Constants.BASE_URL_GOOGLE_API;


public interface AutoCompleteService {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL_GOOGLE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("/maps/api/place/autocomplete/json")
    Call<AutoCompleteResult> getAutoComplete(@Query("input") String input,
                                             @Query("types") String types,
                                             @Query("language") String language,
                                             @Query("location") String location,
                                             @Query("radius") int radius,
                                             @Query("strictbounds") boolean strictbounds,
                                             @Query("key") String key);
}
