package com.danieljdt.androidnapod.network;

import com.danieljdt.androidnapod.models.Picture;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by danjdt on 08/12/2016.
 */

public class NasaAPODRequest extends RetrofitSpiceRequest<Picture, NasaAPODInterface> {

    private final static String API_KEY = "DEMO_KEY";
    private String date = "";

    public NasaAPODRequest() {
        super(Picture.class, NasaAPODInterface.class);
    }

    public NasaAPODRequest(String date) {
        this();
        this.date = date;
    }

    @Override
    public Picture loadDataFromNetwork() {
        return getService().getAPOD(API_KEY, date);
    }

}
