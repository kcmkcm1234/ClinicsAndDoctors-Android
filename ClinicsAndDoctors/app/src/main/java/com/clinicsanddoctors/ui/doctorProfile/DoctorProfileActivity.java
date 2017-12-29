package com.clinicsanddoctors.ui.doctorProfile;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.ClinicsApplication;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.clinicProfile.ClinicProfileActivity;
import com.clinicsanddoctors.ui.rate.RateActivity;
import com.clinicsanddoctors.ui.review.ReviewActivity;
import com.clinicsanddoctors.ui.start.StartActivity;

import java.util.Locale;

import static com.clinicsanddoctors.ui.rate.RateActivity.ARG_CLINIC_DOCTOR;

/**
 * Created by Daro on 07/08/2017.
 */

public class DoctorProfileActivity extends BaseClinicActivity implements DoctorProfileContract.View {

    private DoctorProfilePresenter mPresenter;
    public static final int REQUEST_CODE = 10;

    public static final String ARG_DOCTOR = "ARG_DOCTOR";

    private ClinicAndDoctor mClinicAndDoctor;

    private ImageView mPhoto, mIconCategory, mAddFavorite;
    private TextView mDoctorName, mProfession, mNationality, mAddress, mNameClinic, mCall, mDistance;
    private AppCompatRatingBar mRate;
    private boolean mMustRefresh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        mPresenter = new DoctorProfilePresenter(this, this);
        setupToolbar(getString(R.string.title_detail));
        mClinicAndDoctor = getIntent().getParcelableExtra(ARG_DOCTOR);

        mPhoto = (ImageView) findViewById(R.id.mPhotoClinic);
        mIconCategory = (ImageView) findViewById(R.id.mIconCategory);
        mDoctorName = (TextView) findViewById(R.id.mDoctorName);
        mNationality = (TextView) findViewById(R.id.mNationality);
        mProfession = (TextView) findViewById(R.id.mProfession);
        mAddress = (TextView) findViewById(R.id.mAddress);
        mNameClinic = (TextView) findViewById(R.id.mNameClinic);
        mDistance = (TextView) findViewById(R.id.mDistance);
//        mCall = (TextView) findViewById(R.id.mCall);
        mAddFavorite = (ImageView) findViewById(R.id.mAddFavorite);
        mRate = (AppCompatRatingBar) findViewById(R.id.mRate);

        findViewById(R.id.mSeeReview).setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewActivity.class);
            intent.putExtra(ARG_CLINIC_DOCTOR, mClinicAndDoctor);
            startActivity(intent);
        });

        mDoctorName.setText("-");
        mNationality.setText("-");
        mProfession.setText("-");
        mNameClinic.setText("-");
        mAddress.setText("-");
        mDistance.setText("-");

        /*
        if (mClinicAndDoctor.getName() != null && !mClinicAndDoctor.getName().isEmpty())
            mDoctorName.setText(mClinicAndDoctor.getName());

        if (mClinicAndDoctor.isFavorite())
            mAddFavorite.setImageResource(R.drawable.ic_favorite_profile);
        else
            mAddFavorite.setImageResource(R.drawable.ic_no_favorite);

        String sNationality = ((Doctor) mClinicAndDoctor).getNationality();
        if (sNationality != null && !sNationality.isEmpty())
            mNationality.setText(sNationality);

        if (mClinicAndDoctor.getCategory() != null) {
            if (mClinicAndDoctor.getType() != null) {
                mProfession.setText(mClinicAndDoctor.getType() + " - " + mClinicAndDoctor.getCategory().getName());
            } else
                mProfession.setText(mClinicAndDoctor.getCategory().getName());
            if (mClinicAndDoctor.getCategory().getIcon() != null && !mClinicAndDoctor.getCategory().getIcon().isEmpty())
                Glide.with(this).load(mClinicAndDoctor.getCategory().getIcon())
                        .dontAnimate().placeholder(R.drawable.ic_category_profile).into(mIconCategory);
        }

        Clinic clinic = ((Doctor) mClinicAndDoctor).getClinic();
        if (clinic != null) {
            mNameClinic.setText(clinic.getName());
            if (clinic.getAddress() != null && !clinic.getAddress().isEmpty())
                mAddress.setText(clinic.getAddress());
            else
                mAddress.setText(clinic.getCity() + ", " + clinic.getCountry());
            setDistance(clinic);
        }
        */

        mAddFavorite.setOnClickListener(v -> {
            UserClient userClient = AppPreference.getUser(this);
            if (userClient != null) {
                if (!mClinicAndDoctor.isFavorite())
                    mPresenter.addToFavorite((Doctor) mClinicAndDoctor);
                else
                    mPresenter.removeFromFavorite((Doctor) mClinicAndDoctor);
            } else
                startActivity(new Intent(this, StartActivity.class));
        });

        findViewById(R.id.mShare).setOnClickListener(v -> {
            String shareBody = mClinicAndDoctor.getName() + " - " + mClinicAndDoctor.getCategory().getName() + " - " + mClinicAndDoctor.getPhoneNumber();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Compartir con"));
        });

        findViewById(R.id.mNameClinic).setOnClickListener(v -> {
            Clinic clinic = ((Doctor) mClinicAndDoctor).getClinic();
            if (clinic != null) {
                Intent intent = new Intent(this, ClinicProfileActivity.class);
                intent.putExtra(ClinicProfileActivity.ARG_CLINIC, clinic);
                startActivity(intent);
            }
        });

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

        setDataDoctor((Doctor) mClinicAndDoctor);
    }

    public void setDataDoctor(Doctor dataDoctor) {

        if (dataDoctor == null) return;

        if (mClinicAndDoctor == null)
            mClinicAndDoctor = dataDoctor;

        if (dataDoctor.getRating() != null && !dataDoctor.getRating().isEmpty())
            mRate.setRating(Float.parseFloat(dataDoctor.getRating()));

        if (dataDoctor.getPicture() != null && !dataDoctor.getPicture().isEmpty())
            Glide.with(this).load(dataDoctor.getPicture())
                    .dontAnimate().placeholder(R.drawable.placeholder_clinic).into(mPhoto);
        else
            mPhoto.setImageResource(R.drawable.placeholder_clinic);

        if (dataDoctor.getName() != null && !dataDoctor.getName().isEmpty())
            mDoctorName.setText(dataDoctor.getName());

        if (dataDoctor.isFavorite())
            mAddFavorite.setImageResource(R.drawable.ic_favorite_profile);
        else
            mAddFavorite.setImageResource(R.drawable.ic_no_favorite);

        String sNationality = dataDoctor.getNationality();
        if (sNationality != null && !sNationality.isEmpty())
            mNationality.setText(sNationality);

        if (dataDoctor.getCategory() != null) {
            if (dataDoctor.getType() != null) {
                mProfession.setText(dataDoctor.getType() + " - " + dataDoctor.getCategory().getName());
            } else
                mProfession.setText(dataDoctor.getCategory().getName());
            if (dataDoctor.getCategory().getIcon() != null && !dataDoctor.getCategory().getIcon().isEmpty())
                Glide.with(this).load(dataDoctor.getCategory().getIcon())
                        .dontAnimate().placeholder(R.drawable.ic_category_profile).into(mIconCategory);
        }

        Clinic clinic = dataDoctor.getClinic();
        if (clinic != null) {
            mNameClinic.setText(clinic.getName());
            if (clinic.getAddress() != null && !clinic.getAddress().isEmpty())
                mAddress.setText(clinic.getAddress());
            else
                mAddress.setText(clinic.getCity() + ", " + clinic.getCountry());
            setDistance(clinic);
        }
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
            float kilometers = meters / 1000;

            if (kilometers < 1) {
                distanceResult = String.format(Locale.US, "%.2f", meters);
                sbDistance = distanceResult + " " + mDistance.getContext().getString(R.string.mtrs_away);
            } else {
                distanceResult = String.format(Locale.US, "%.2f", kilometers);
                sbDistance = distanceResult + " " + mDistance.getContext().getString(R.string.km_away);
            }
            mDistance.setText(sbDistance);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_rate, menu);
//
//        if (mClinicAndDoctor.isRated())
//            menu.getItem(1).setVisible(false);
//        else
//            menu.getItem(1).setVisible(true);
//        return true;
//    }

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
            UserClient userClient = AppPreference.getUser(this);
            if (userClient != null) {
                Intent intent = new Intent(this, RateActivity.class);
                intent.putExtra(ARG_CLINIC_DOCTOR, mClinicAndDoctor);
                startActivityForResult(intent, RateActivity.REQUEST_CODE);
            } else
                startActivity(new Intent(this, StartActivity.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccessAdd() {
        mMustRefresh = true;
        mAddFavorite.setImageResource(R.drawable.ic_favorite_profile);
    }

    @Override
    public void onSuccessRemove() {
        mMustRefresh = true;
        mAddFavorite.setImageResource(R.drawable.ic_no_favorite);
    }

    @Override
    public void onBackPressed() {
        if (mMustRefresh) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("UPDATE", true);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else
            super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RateActivity.REQUEST_CODE) {
            if (data != null) {
                boolean mustRefresh = data.getBooleanExtra("UPDATE", false);
                if (mustRefresh) {
                    mMustRefresh = true;
                }
            }
        }
    }
}
