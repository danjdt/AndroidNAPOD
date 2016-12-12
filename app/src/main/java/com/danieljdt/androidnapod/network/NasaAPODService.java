package com.danieljdt.androidnapod.network;

import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by danjdt on 08/12/2016.
 */

public class NasaAPODService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = "https://api.nasa.gov";


    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(NasaAPODInterface.class);
    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }
}
