package com.danieljdt.androidnapod.network;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.danieljdt.androidnapod.models.Picture;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by danjdt on 08/12/2016.
 */

public class NasaAPODListener implements RequestListener<Picture> {

    private Context context;
    private Fragment fragment;

    public NasaAPODListener(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        Toast.makeText(context, "Request Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestSuccess(Picture picture) {
        if (picture != null) {
            OnPostAPODRequestInteractionListener requestInteractionListener;
            try {
                requestInteractionListener = (OnPostAPODRequestInteractionListener) fragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(" must implement OnHeadlineSelectedListener");
            }
            //Toast.makeText(context,"Request Success " + picture.getTitle() , Toast.LENGTH_LONG).show();
            requestInteractionListener.onPostAPODRequestInteraction(picture);
        } else {
            Toast.makeText(context, "Request Success??", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnPostAPODRequestInteractionListener {
        void onPostAPODRequestInteraction(Picture item);
    }
}
