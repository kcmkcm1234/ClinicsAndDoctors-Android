package com.clinicsanddoctors.ui.rate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.entity.Review;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.utils.ManagerAnimation;

import org.parceler.Parcels;

/**
 * Created by Daro on 20/10/2017.
 */

public class RateActivity extends BaseClinicActivity implements RateContract.View {

    public static final int REQUEST_CODE = 10;
    public static final String ARG_CLINIC_DOCTOR = "ARG_CLINIC_DOCTOR";
    public static final String ARG_REVIEW = "ARG_REVIEW";
    private ClinicAndDoctor mClinicAndDoctor;
    private Review review;
    private RatePresenter mPresenter;

    private ImageView mPhotoClinic;
    private TextView mNameClinic;

    private RadioButton optionSuggested1, optionSuggested2, optionSuggested3, optionSuggested4;
    private RadioButton mRate1, mRate2, mRate3, mRate4, mRate5;
    private RadioGroup mOptionsReasons;
    private TextView mTitleOption;
    private EditText mComment;

    private String sOptionSuggested;
    private boolean isGood = true;
    private boolean mustRefresh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        mPresenter = new RatePresenter(this, this);
        setupToolbar(getString(R.string.title_rating));
        mClinicAndDoctor = getIntent().getParcelableExtra(ARG_CLINIC_DOCTOR);
        review = Parcels.unwrap(getIntent().getParcelableExtra(ARG_REVIEW));
        mPhotoClinic = (ImageView) findViewById(R.id.mPhotoClinic);
        mNameClinic = (TextView) findViewById(R.id.mNameClinic);
        mTitleOption = (TextView) findViewById(R.id.mTitleOption);

        mComment = (EditText) findViewById(R.id.mComment);
        mOptionsReasons = (RadioGroup) findViewById(R.id.mOptionsReasons);
        optionSuggested1 = (RadioButton) findViewById(R.id.optionSuggested1);
        optionSuggested2 = (RadioButton) findViewById(R.id.optionSuggested2);
        optionSuggested3 = (RadioButton) findViewById(R.id.optionSuggested3);
        optionSuggested4 = (RadioButton) findViewById(R.id.optionSuggested4);

        mRate1 = (RadioButton) findViewById(R.id.mRate1);
        mRate2 = (RadioButton) findViewById(R.id.mRate2);
        mRate3 = (RadioButton) findViewById(R.id.mRate3);
        mRate4 = (RadioButton) findViewById(R.id.mRate4);
        mRate5 = (RadioButton) findViewById(R.id.mRate5);

        if (mClinicAndDoctor != null) {
            mNameClinic.setText(mClinicAndDoctor.getName());
            if (mClinicAndDoctor.getPicture() != null && !mClinicAndDoctor.getPicture().isEmpty())
                Glide.with(this).load(mClinicAndDoctor.getPicture())
                        .dontAnimate().placeholder(R.drawable.placeholder_clinic).into(mPhotoClinic);
            else
                mPhotoClinic.setImageResource(R.drawable.placeholder_clinic);
        }

        mRate1.setOnClickListener(v -> changeToBad());
        mRate2.setOnClickListener(v -> changeToBad());
        mRate3.setOnClickListener(v -> changeToBad());
        mRate4.setOnClickListener(v -> changeToGood());
        mRate5.setOnClickListener(v -> changeToGood());

        findViewById(R.id.mSubmit).setOnClickListener(v -> {
            String clinicId = null, doctorId = null;
            if (mClinicAndDoctor instanceof Doctor)
                doctorId = mClinicAndDoctor.getId();
            else
                clinicId = mClinicAndDoctor.getId();
            mPresenter.sendRate(clinicId, doctorId, getRatingValue(), getReason(), mComment.getText().toString());
        });

        if(review!=null) {
            mComment.setText(review.getComment());
            mComment.setSelection(mComment.getText().length());
            setRating(review.getRating());
            if(review.getRating()>3)
                changeToGood();
            else
                changeToBad();
        }
    }

    private void setRating(int rating) {
        switch (rating) {
            case 1: mRate1.setChecked(true);break;
            case 2: mRate2.setChecked(true);break;
            case 3: mRate3.setChecked(true);break;
            case 4: mRate4.setChecked(true);break;
            case 5: mRate5.setChecked(true);break;
        }
    }

    private int getRatingValue() {
        if (mRate1.isChecked()) return 1;
        if (mRate2.isChecked()) return 2;
        if (mRate3.isChecked()) return 3;
        if (mRate4.isChecked()) return 4;
        if (mRate5.isChecked()) return 5;

        return 0;
    }

    private String getReason() {
        if (optionSuggested1.isChecked()) return optionSuggested1.getText().toString();
        if (optionSuggested2.isChecked()) return optionSuggested2.getText().toString();
        if (optionSuggested3.isChecked()) return optionSuggested3.getText().toString();
        if (optionSuggested4.isChecked()) return optionSuggested4.getText().toString();

        return "";
    }

    private void changeToBad() {
        if (!isGood) return;
        isGood = false;
        ManagerAnimation.translateToRight(mOptionsReasons, new ManagerAnimation.CallbackAnimation() {
            @Override
            public void onFinish(View view) {
                mOptionsReasons.clearAnimation();
                setBadOptions();
                ManagerAnimation.translateFromLeft(mOptionsReasons, null);
            }
        });
    }

    private void changeToGood() {
        if (isGood) return;
        isGood = true;
        ManagerAnimation.translateToLeft(mOptionsReasons, new ManagerAnimation.CallbackAnimation() {
            @Override
            public void onFinish(View view) {
                mOptionsReasons.clearAnimation();
                setGoodOptions();
                ManagerAnimation.translateFromRight(mOptionsReasons, null);
            }
        });
    }

    private void setGoodOptions() {
        mTitleOption.setText(getString(R.string.tell_us_good));
        optionSuggested1.setText(getString(R.string.rate_good_comment1));
        optionSuggested2.setText(getString(R.string.rate_good_comment2));
        optionSuggested3.setText(getString(R.string.rate_good_comment3));
        optionSuggested4.setText(getString(R.string.rate_good_comment4));
    }

    private void setBadOptions() {
        mTitleOption.setText(getString(R.string.tell_us_bad));
        optionSuggested1.setText(getString(R.string.rate_bad_comment1));
        optionSuggested2.setText(getString(R.string.rate_bad_comment2));
        optionSuggested3.setText(getString(R.string.rate_bad_comment3));
        optionSuggested4.setText(getString(R.string.rate_bad_comment4));
    }

    @Override
    public void onSuccessRating() {
        Toast.makeText(this, "Rated", Toast.LENGTH_SHORT).show();
        mustRefresh = true;
        onBackPressed();
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
