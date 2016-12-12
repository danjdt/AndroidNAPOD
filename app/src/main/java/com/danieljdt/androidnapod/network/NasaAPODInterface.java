package com.danieljdt.androidnapod.network;

import com.danieljdt.androidnapod.models.Picture;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by danjdt on 08/12/2016.
 */

public interface NasaAPODInterface {

    @GET("/planetary/apod")
    Picture getAPOD(@Query("api_key") String key, @Query("date") String date);
}
