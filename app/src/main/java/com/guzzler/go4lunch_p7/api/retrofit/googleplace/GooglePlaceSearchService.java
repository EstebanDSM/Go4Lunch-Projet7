package com.guzzler.go4lunch_p7.api.retrofit.googleplace;

import com.guzzler.go4lunch_p7.models.googleplaces_gson.SearchPlace;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Création interface qui liste les différents appels (endpoints)

public interface GooglePlaceSearchService {

    // Variable retourne un objet Retrofit qui sera utilisé par la suite pour exécuter une requête réseau.
    Retrofit retrofit = new Retrofit.Builder()

            // URL Racine pour les endpoints
            .baseUrl("https://maps.googleapis.com")

            // Définition du sérialiseur/désérialiseur utilisé par RETROFIT (ici GSON)
            .addConverterFactory(GsonConverterFactory.create())

            .build();

    // Requête REST de type GET sur ce complément d'URL
    @GET("/maps/api/place/nearbysearch/json")

    /* Le type retourné par cette requête GET correspondant au JSON désérialisé.
    Dans ce cas-là, cette requête retourne une liste d'objet SearchPlace, objet que nous avons créé dans /models/googleplaces_gson/
    */
    Call<SearchPlace> getNearbyRestaurants(@Query("location") String location,
                                           @Query("rankby") String distanceRanking,
                                           @Query("type") String type,
                                           @Query("key") String key);


}