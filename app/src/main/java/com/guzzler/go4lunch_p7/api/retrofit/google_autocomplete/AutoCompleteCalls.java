package com.guzzler.go4lunch_p7.api.retrofit.google_autocomplete;

import androidx.annotation.Nullable;

import com.guzzler.go4lunch_p7.models.google_autocomplete_gson.AutoCompleteResult;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.guzzler.go4lunch_p7.utils.Constants.API_KEY;
import static com.guzzler.go4lunch_p7.utils.Constants.RADIUS;


public class AutoCompleteCalls {
    private static final String types = "establishment";
    static String language = "fr";

    public static void fetchAutoCompleteResult(AutoCompleteCalls.Callbacks callbacks, String input, String location) {

        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<>(callbacks);

        AutoCompleteService googleAutoComplete = AutoCompleteService.retrofit.create(AutoCompleteService.class);

        Call<AutoCompleteResult> call = googleAutoComplete.getAutoComplete(input, types, language, location, RADIUS, true, API_KEY);
        call.enqueue(new Callback<AutoCompleteResult>() {

            @Override
            public void onResponse(Call<AutoCompleteResult> call, Response<AutoCompleteResult> response) {
                if (callbacksWeakReference.get() != null)
                    callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<AutoCompleteResult> call, Throwable t) {
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }

    public interface Callbacks {
        void onResponse(@Nullable AutoCompleteResult autoCompleteResult);

        void onFailure();
    }
}