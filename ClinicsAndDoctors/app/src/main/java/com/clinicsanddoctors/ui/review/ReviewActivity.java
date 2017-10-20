package com.clinicsanddoctors.ui.review;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.adapters.ReviewAdapter;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.entity.Review;
import com.clinicsanddoctors.ui.BaseClinicActivity;

import java.util.List;

/**
 * Created by Daro on 20/10/2017.
 */

public class ReviewActivity extends BaseClinicActivity implements ReviewContract.View {

    public static final String ARG_DOCTOR = "ARG_DOCTOR";
    private Doctor mDoctor;
    private ReviewPresenter mPresenter;

    private ImageView mPhoto;
    private TextView mDoctorName;
    private AppCompatRatingBar mRate;
    private RecyclerView mReviews;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        setupToolbar(getString(R.string.title_reviews));
        mDoctor = getIntent().getParcelableExtra(ARG_DOCTOR);
        mPresenter = new ReviewPresenter(this, this);

        mPhoto = (ImageView) findViewById(R.id.mPhotoClinic);
        mDoctorName = (TextView) findViewById(R.id.mDoctorName);
        mRate = (AppCompatRatingBar) findViewById(R.id.mRate);
        mReviews = (RecyclerView) findViewById(R.id.mReviews);

        if (mDoctor.getName() != null && !mDoctor.getName().isEmpty())
            mDoctorName.setText(mDoctor.getName());

        if (mDoctor.getPicture() != null && !mDoctor.getPicture().isEmpty())
            Glide.with(this).load(mDoctor.getPicture())
                    .dontAnimate().placeholder(R.drawable.placeholder_clinic).into(mPhoto);
        else
            mPhoto.setImageResource(R.drawable.placeholder_clinic);

        if (mDoctor.getRating() != null && !mDoctor.getRating().isEmpty())
            mRate.setRating(Float.parseFloat(mDoctor.getRating()));

        mPresenter.getReviews(mDoctor);
    }

    @Override
    public void showReviews(List<Review> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviews.setLayoutManager(linearLayoutManager);
        mReviews.setAdapter(new ReviewAdapter(list));
    }
}
