package com.clinicsanddoctors.ui.review;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.entity.Review;
import com.clinicsanddoctors.data.remote.ClinicServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by Daro on 20/10/2017.
 */

public class ReviewPresenter implements ReviewContract.Presenter {
    private ReviewContract.View mView;
    private Context mContext;

    public ReviewPresenter(ReviewContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getReviews(Doctor doctor) {
        List<Review> reviewList = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            reviewList.add(new Review());
        mView.showReviews(reviewList);
    }

    private void onError(Throwable throwable) {
        mView.hideProgressDialog();
        if (throwable != null) {
            if (throwable instanceof HttpException)
                mView.showErrorAlert(ClinicServices.attendError((HttpException) throwable).getError());
            else if (throwable instanceof IOException)
                mView.showErrorAlert(mContext.getString(R.string.error_internet));
            else
                mView.showErrorAlert(mContext.getString(R.string.error_generic));
        }
    }
}
