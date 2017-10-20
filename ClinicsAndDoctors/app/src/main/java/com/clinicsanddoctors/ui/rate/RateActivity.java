package com.clinicsanddoctors.ui.rate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.utils.ManagerAnimation;

/**
 * Created by Daro on 20/10/2017.
 */

public class RateActivity extends BaseClinicActivity {

    public static final String ARG_CLINIC_DOCTOR = "ARG_CLINIC_DOCTOR";
    private ClinicAndDoctor mClinicAndDoctor;

    private ImageView mPhotoClinic;
    private TextView mNameClinic;

    private RadioButton optionSuggested1, optionSuggested2, optionSuggested3, optionSuggested4;
    private RadioButton mRate1, mRate2, mRate3, mRate4, mRate5;
    private RadioGroup mOptionsReasons;
    private TextView mTitleOption;
    private EditText mComment;

    private String sOptionSuggested;
    private boolean isGood = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        setupToolbar(getString(R.string.title_rating));
        mClinicAndDoctor = getIntent().getParcelableExtra(ARG_CLINIC_DOCTOR);
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

        optionSuggested(optionSuggested1);
        optionSuggested(optionSuggested2);
        optionSuggested(optionSuggested3);
        optionSuggested(optionSuggested4);

        mRate1.setOnClickListener(v -> changeToBad());
        mRate2.setOnClickListener(v -> changeToBad());
        mRate3.setOnClickListener(v -> changeToBad());
        mRate4.setOnClickListener(v -> changeToGood());
        mRate5.setOnClickListener(v -> changeToGood());
    }

    private void optionSuggested(RadioButton radioButton) {
        radioButton.setOnClickListener(v -> sOptionSuggested = radioButton.getText().toString());
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
}
