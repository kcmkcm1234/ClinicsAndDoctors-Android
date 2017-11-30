package com.clinicsanddoctors.ui.review;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.entity.Review;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.ReviewClinicRequest;
import com.clinicsanddoctors.data.remote.requests.ReviewDoctorRequest;

import java.io.IOException;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public void getReviews(ClinicAndDoctor clinicAndDoctor) {
        mView.showProgressDialog();
        if(clinicAndDoctor instanceof Doctor) {
            ClinicServices.getServiceClient().getReviews(new ReviewDoctorRequest(Integer.parseInt(clinicAndDoctor.getId())))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::onError);
        } else  {
            ClinicServices.getServiceClient().getReviews(new ReviewClinicRequest(Integer.parseInt(clinicAndDoctor.getId())))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::onError);
        }
    }

    private void onSuccess(List<Review> reviews) {
        mView.hideProgressDialog();
        mView.showReviews(reviews);
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
