package com.clinicsanddoctors.ui.review;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.adapters.ReviewAdapter;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.entity.Review;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.rate.RateActivity;
import com.clinicsanddoctors.ui.start.StartActivity;
import com.clinicsanddoctors.utils.MyDividerItemDecoration;

import org.parceler.Parcels;

import java.util.List;

import static com.clinicsanddoctors.ui.rate.RateActivity.ARG_CLINIC_DOCTOR;
import static com.clinicsanddoctors.ui.rate.RateActivity.ARG_REVIEW;

/**
 * Created by Daro on 20/10/2017.
 */

public class ReviewActivity extends BaseClinicActivity implements ReviewContract.View {

    public static final String ARG_DOCTOR = "ARG_DOCTOR";
//    private Doctor mDoctor;
    private ClinicAndDoctor clinicAndDoctor;
    private ReviewPresenter mPresenter;

    private ImageView mPhoto;
    private TextView mDoctorName, mRateButton;
    private AppCompatRatingBar mRate;
    private RecyclerView mReviews;

    Review mMyReview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        setupToolbar(getString(R.string.title_reviews));
        clinicAndDoctor = getIntent().getParcelableExtra(ARG_CLINIC_DOCTOR);
        mPresenter = new ReviewPresenter(this, this);

        mPhoto = (ImageView) findViewById(R.id.mPhotoClinic);
        mDoctorName = (TextView) findViewById(R.id.mDoctorName);
        mRate = (AppCompatRatingBar) findViewById(R.id.mRate);
        mReviews = (RecyclerView) findViewById(R.id.mReviews);
        mRateButton = (TextView) findViewById(R.id.mRateButton);

        if (clinicAndDoctor.getName() != null && !clinicAndDoctor.getName().isEmpty())
            mDoctorName.setText(clinicAndDoctor.getName());

        if (clinicAndDoctor.getPicture() != null && !clinicAndDoctor.getPicture().isEmpty())
            Glide.with(this).load(clinicAndDoctor.getPicture())
                    .dontAnimate().placeholder(R.drawable.placeholder_clinic).into(mPhoto);
        else
            mPhoto.setImageResource(R.drawable.placeholder_clinic);

        if (clinicAndDoctor.getRating() != null && !clinicAndDoctor.getRating().isEmpty())
            mRate.setRating(Float.parseFloat(clinicAndDoctor.getRating()));

        if(clinicAndDoctor instanceof Doctor)
            mRateButton.setText(getString(R.string.rate_doctor));
        else
            mRateButton.setText(getString(R.string.rate_clinic));

        if(clinicAndDoctor.isRated())
            mRateButton.setText(R.string.edit_rate);

        findViewById(R.id.mRateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserClient userClient = AppPreference.getUser(ReviewActivity.this);
                if (userClient != null) {
                    Intent intent = new Intent(ReviewActivity.this, RateActivity.class);
                    intent.putExtra(ARG_CLINIC_DOCTOR, clinicAndDoctor);
                    intent.putExtra(ARG_REVIEW, Parcels.wrap(Review.class,mMyReview));
                    startActivityForResult(intent, RateActivity.REQUEST_CODE);
                } else
                    startActivity(new Intent(ReviewActivity.this, StartActivity.class));
            }
        });

        mPresenter.getReviews(clinicAndDoctor);
    }

    @Override
    public void showReviews(List<Review> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviews.setLayoutManager(linearLayoutManager);
        mReviews.setAdapter(new ReviewAdapter(list));
        mReviews.addItemDecoration(new MyDividerItemDecoration());

        if(AppPreference.getUser(this)!=null) {
            for (Review item: list) {
                if(Integer.parseInt(item.getUserClient().getId())== AppPreference.getUser(this).getId()){
                    mMyReview = item;
                    return;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RateActivity.REQUEST_CODE) {
            if (data != null) {
                boolean mustRefresh = data.getBooleanExtra("UPDATE", false);
                if (mustRefresh) {
                    mRateButton.setText(R.string.edit_rate);
                    mPresenter.getReviews(clinicAndDoctor);
                }
            }
        }
    }
}
