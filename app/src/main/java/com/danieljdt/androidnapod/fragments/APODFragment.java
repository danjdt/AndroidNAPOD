package com.danieljdt.androidnapod.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danieljdt.androidnapod.R;
import com.danieljdt.androidnapod.models.Picture;
import com.danieljdt.androidnapod.network.NasaAPODListener;
import com.danieljdt.androidnapod.network.NasaAPODRequest;
import com.danieljdt.androidnapod.network.NasaAPODService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class APODFragment extends Fragment implements NasaAPODListener.OnPostAPODRequestInteractionListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    @BindView(R.id.tvTitle)
    TextView mTextViewTitle;
    @BindView(R.id.tvCopyright)
    TextView mTextViewCopyright;
    @BindView(R.id.tvExplanation)
    TextView mTextViewExplanation;
    @BindView(R.id.imageViewAPOD)
    ImageView mImageViewAPOD;
    @BindView(R.id.buttonExplanation)
    ImageButton mButtonExplanation;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Context context;
    private Picture picture;
    private SpiceManager spiceManager = new SpiceManager(NasaAPODService.class);
    private boolean hideExplanation;
    private GregorianCalendar pictureDate;

    public APODFragment() {

    }

    public static APODFragment newInstance(int sectionNumber) {
        APODFragment fragment = new APODFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_apod, container, false);
        ButterKnife.bind(this, rootView);
        setPictureDate();
        requestNAPOD();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        spiceManager.start(getContext());
    }

    @Override
    public void onDetach() {
        spiceManager.shouldStop();
        super.onDetach();
    }

    private void requestNAPOD() {
        NasaAPODRequest request = new NasaAPODRequest(formatDate(pictureDate));
        NasaAPODListener listener = new NasaAPODListener(context, this);
        spiceManager.execute(request, "NasaAPOD" + getArguments().getInt(ARG_SECTION_NUMBER), DurationInMillis.ONE_MINUTE, listener);
    }

    @OnClick(R.id.buttonDownload)
    public void onClickDownloadButton() {
        if (picture.getMedia_type().equals("image")) {
            Picasso.with(context)
                    .load(picture.getUrl())
                    .into(getTarget());
        } else {
            Toast.makeText(context, "There is no image to download", Toast.LENGTH_SHORT).show();
        }
    }

    private Target getTarget() {
        return new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                FileOutputStream out = null;
                try {
                    File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "NAPOD");
                    if (!myDir.exists()) {
                        myDir.mkdir();
                    }
                    String name = picture.getTitle() + ".jpg";
                    File file = new File(myDir, name);
                    out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    viewDownloadedImage(file);
                } catch (Exception e) {
                    Toast.makeText(context, "Error on image download", Toast.LENGTH_SHORT).show();
                } finally {
                    try {
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        Log.e("AndroidNAPOD Error", e.getLocalizedMessage());
                    }
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Toast.makeText(context, "Download Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
    }

    @OnClick(R.id.buttonExplanation)
    public void onClickExplanationButton() {
        if (hideExplanation) {
            mButtonExplanation.setImageResource(R.drawable.ic_expand_more_white_24dp);
            mTextViewExplanation.setVisibility(View.VISIBLE);
        } else {
            mButtonExplanation.setImageResource(R.drawable.ic_expand_less_white_24dp);
            mTextViewExplanation.setVisibility(View.GONE);
        }

        hideExplanation = !hideExplanation;
    }

    public void setPictureDate() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.DAY_OF_YEAR, -(getArguments().getInt(ARG_SECTION_NUMBER)) - 1);
        pictureDate = calendar;
    }

    public String formatDate(GregorianCalendar date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date.getTime());
    }

    @Override
    public void onPostAPODRequestInteraction(Picture item) {
        picture = item;
        mTextViewTitle.setText(item.getTitle());
        mTextViewExplanation.setText(item.getExplanation());
        if (item.getCopyright() != null)
            mTextViewCopyright.setText(item.getCopyright());
        if (item.getMedia_type().equals("image")) {
            try {
                Picasso.with(context).load(item.getUrl()).into(mImageViewAPOD);
                progressBar.setVisibility(ProgressBar.GONE);
            } catch (Exception e) {
                Toast.makeText(context, "Could not load the image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Image not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewDownloadedImage(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "image/*");
        startActivity(intent);
    }

}

