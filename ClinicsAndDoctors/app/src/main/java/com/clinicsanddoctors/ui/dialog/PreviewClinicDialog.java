package com.clinicsanddoctors.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.ClinicsApplication;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;

import java.util.Locale;

/**
 * Created by Daro on 27/07/2017.
 */

public class PreviewClinicDialog extends Dialog {

    private TextView mProviderName, mCantDoctors, mDistance;
    private ImageView mPhoto;
    private AppCompatRatingBar mRate;
    private Context mContext;
    private ClinicAndDoctor clinicAndDoctor;
    private CallbackDialog mCallbackDialog;

    public PreviewClinicDialog(@NonNull Context context, CallbackDialog callbackDialog) {
        super(context);
        mCallbackDialog = callbackDialog;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        initView(mContext);
    }

    private void initView(Context context) {
        setContentView(R.layout.dialog_preview_provider);

        mProviderName = (TextView) findViewById(R.id.mProviderName);
        mCantDoctors = (TextView) findViewById(R.id.mCantDoctors);
        mDistance = (TextView) findViewById(R.id.mDistance);
        mPhoto = (ImageView) findViewById(R.id.mPhoto);
        mRate = (AppCompatRatingBar) findViewById(R.id.mRate);

        if (clinicAndDoctor != null) {
            setDistance();
            mRate.setRating(Float.parseFloat(clinicAndDoctor.getRating()));
            mCantDoctors.setText(((Clinic) clinicAndDoctor).getCantDoctors() + " doctors");
            mProviderName.setText(clinicAndDoctor.getName());
            if (clinicAndDoctor.getPicture() != null && !clinicAndDoctor.getPicture().isEmpty()) {

                int idDrawablePin = R.drawable.placeholder_heart;
                Glide.with(context)
                        .load(clinicAndDoctor.getPicture())
                        .override(100, 100) // resizes the image to these dimensions (in pixel)
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(idDrawablePin).into(mPhoto);
            }
        }

        findViewById(R.id.mCall).setOnClickListener(v -> mCallbackDialog.onGo(this, clinicAndDoctor));
        findViewById(R.id.mInfo).setOnClickListener(v -> mCallbackDialog.onInfo(this, clinicAndDoctor));
    }

    private void setDistance() {
        Location currentLocation = ClinicsApplication.getInstance().getCurrentLocation();
        if (currentLocation != null) {
            Location locationProvider = new Location("");
            locationProvider.setLongitude(clinicAndDoctor.getLongitude());
            locationProvider.setLatitude(clinicAndDoctor.getLatitude());

            String distanceResult;
            String sbDistance;

            float distance = currentLocation.distanceTo(locationProvider);
            float meters = Float.parseFloat(String.format(Locale.US, "%.2f", distance));
            float kilometers = meters / 1000;

            if (kilometers < 1) {
                distanceResult = String.format(Locale.US, "%.2f", meters);
                sbDistance = distanceResult + " " + mDistance.getContext().getString(R.string.mtrs_away);
            } else {
                distanceResult = String.format(Locale.US, "%.2f", kilometers);
                sbDistance = distanceResult + " " + mDistance.getContext().getString(R.string.km_away);
            }
            mDistance.setText(sbDistance);
        } else
            mDistance.setText("-");
    }

    public void setClinicAndDoctor(ClinicAndDoctor clinicAndDoctor) {
        this.clinicAndDoctor = clinicAndDoctor;
    }

    public interface CallbackDialog {
        void onGo(Dialog dialog, ClinicAndDoctor clinicAndDoctor);

        void onInfo(Dialog dialog, ClinicAndDoctor clinicAndDoctor);

        void onHowToArrive(Dialog dialog, ClinicAndDoctor clinicAndDoctor);
    }
}
