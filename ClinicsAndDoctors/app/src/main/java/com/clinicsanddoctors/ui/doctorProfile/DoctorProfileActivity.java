package com.clinicsanddoctors.ui.doctorProfile;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.ClinicsApplication;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.clinicProfile.ClinicProfileActivity;
import com.clinicsanddoctors.ui.rate.RateActivity;
import com.clinicsanddoctors.ui.review.ReviewActivity;

import java.util.Locale;

/**
 * Created by Daro on 07/08/2017.
 */

public class DoctorProfileActivity extends BaseClinicActivity implements DoctorProfileContract.View {

    private DoctorProfilePresenter mPresenter;
    public static final int REQUEST_CODE = 10;

    public static final String ARG_DOCTOR = "ARG_DOCTOR";

    private ClinicAndDoctor mClinicAndDoctor;

    private ImageView mPhoto;
    private TextView mDoctorName, mProfession, mNationality, mAddress, mNameClinic, mCall, mDistance, mAddFavorite;
    private AppCompatRatingBar mRate;
    private boolean mustRefresh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        mPresenter = new DoctorProfilePresenter(this, this);
        setupToolbar(getString(R.string.title_detail));
        mClinicAndDoctor = getIntent().getParcelableExtra(ARG_DOCTOR);

        mPhoto = (ImageView) findViewById(R.id.mPhotoClinic);
        mDoctorName = (TextView) findViewById(R.id.mDoctorName);
        mNationality = (TextView) findViewById(R.id.mNationality);
        mProfession = (TextView) findViewById(R.id.mProfession);
        mAddress = (TextView) findViewById(R.id.mAddress);
        mNameClinic = (TextView) findViewById(R.id.mNameClinic);
        mDistance = (TextView) findViewById(R.id.mDistance);
        mCall = (TextView) findViewById(R.id.mCall);
        mAddFavorite = (TextView) findViewById(R.id.mAddFavorite);
        mRate = (AppCompatRatingBar) findViewById(R.id.mRate);

        findViewById(R.id.mSeeReview).setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra(ReviewActivity.ARG_DOCTOR, mClinicAndDoctor);
            startActivity(intent);
        });

        if (mClinicAndDoctor.getRating() != null && !mClinicAndDoctor.getRating().isEmpty())
            mRate.setRating(Float.parseFloat(mClinicAndDoctor.getRating()));

        if (mClinicAndDoctor.getPicture() != null && !mClinicAndDoctor.getPicture().isEmpty())
            Glide.with(this).load(mClinicAndDoctor.getPicture())
                    .dontAnimate().placeholder(R.drawable.placeholder_clinic).into(mPhoto);
        else
            mPhoto.setImageResource(R.drawable.placeholder_clinic);

        mDoctorName.setText("-");
        mNationality.setText("-");
        mProfession.setText("-");
        mNameClinic.setText("-");
        mAddress.setText("-");
        mDistance.setText("-");

        if (mClinicAndDoctor.getName() != null && !mClinicAndDoctor.getName().isEmpty())
            mDoctorName.setText(mClinicAndDoctor.getName());

        if (mClinicAndDoctor.isFavorite())
            mAddFavorite.setText(getString(R.string.remove_from_favorite));
        else
            mAddFavorite.setText(getString(R.string.add_to_favorite));

        mAddFavorite.setOnClickListener(v -> {
            if (mAddFavorite.getText().toString().equalsIgnoreCase(getString(R.string.add_to_favorite)))
                mPresenter.addToFavorite();
            else
                mPresenter.removeFromFavorite();
        });

        String sNationality = ((Doctor) mClinicAndDoctor).getNationality();
        if (sNationality != null && !sNationality.isEmpty())
            mNationality.setText(sNationality);

        if (mClinicAndDoctor.getCategory() != null)
            mProfession.setText(mClinicAndDoctor.getCategory().getName());

        Clinic clinic = ((Doctor) mClinicAndDoctor).getClinic();
        if (clinic != null) {
            mNameClinic.setText(clinic.getName());
            if (clinic.getAddress() != null && !clinic.getAddress().isEmpty())
                mAddress.setText(clinic.getAddress());
            else
                mAddress.setText(clinic.getCity() + ", " + clinic.getCountry());
            setDistance(clinic);
        }

        if (mClinicAndDoctor.getPhoneNumber() != null && !mClinicAndDoctor.getPhoneNumber().isEmpty()) {
            mCall.setText(mClinicAndDoctor.getPhoneNumber());
            findViewById(R.id.mContainerCall).setEnabled(true);
            findViewById(R.id.mContainerCall).setBackground(getResources().getDrawable(R.drawable.bg_rectangle_rounded_green));
        } else {
            mCall.setText("-");
            findViewById(R.id.mContainerCall).setEnabled(false);
            findViewById(R.id.mContainerCall).setBackground(getResources().getDrawable(R.drawable.bg_rectangle_rounded_gray));
        }

        findViewById(R.id.mContainerCall).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mClinicAndDoctor.getPhoneNumber()));
            startActivity(intent);
        });

        findViewById(R.id.mHowArrive).setOnClickListener(v -> {
            String latitude = "" + mClinicAndDoctor.getLatitude();
            String longitude = "" + mClinicAndDoctor.getLongitude();
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
            startActivity(intent);
        });
    }

    private void setDistance(Clinic clinic) {
        Location currentLocation = ClinicsApplication.getInstance().getCurrentLocation();
        if (currentLocation != null) {
            Location locationProvider = new Location("");
            locationProvider.setLongitude(clinic.getLongitude());
            locationProvider.setLatitude(clinic.getLatitude());

            String distanceResult;
            String sbDistance = "";

            float distance = currentLocation.distanceTo(locationProvider);
            float meters = Float.parseFloat(String.format(Locale.US, "%.2f", distance));
            float miles = meters * 0.000621371f;
            float feets = meters * 3.28084f;

            if (miles < 0.1) {
                distanceResult = String.format(Locale.US, "%.2f", feets);
                sbDistance = distanceResult + " ft Away";
            } else {
                distanceResult = String.format(Locale.US, "%.2f", miles);
                sbDistance = distanceResult + " mi Away";
            }
            mDistance.setText(sbDistance);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.mGoToClinic) {
            Clinic clinic = ((Doctor) mClinicAndDoctor).getClinic();
            if (clinic != null) {
                Intent intent = new Intent(this, ClinicProfileActivity.class);
                intent.putExtra(ClinicProfileActivity.ARG_CLINIC, clinic);
                startActivity(intent);
                return true;
            }
        }
        if (id == R.id.mRateDoctor) {
            Intent intent = new Intent(this, RateActivity.class);
            intent.putExtra(RateActivity.ARG_CLINIC_DOCTOR, mClinicAndDoctor);
            startActivity(new Intent(intent));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccessAdd() {
        mustRefresh = true;
        mAddFavorite.setText(getString(R.string.remove_from_favorite));
    }

    @Override
    public void onSuccessRemove() {
        mustRefresh = true;
        mAddFavorite.setText(getString(R.string.add_to_favorite));
    }

    @Override
    public void onBackPressed() {
        if (mustRefresh) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("UPDATE", true);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else
            super.onBackPressed();

    }
}
