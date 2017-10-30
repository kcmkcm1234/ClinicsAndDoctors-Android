package com.clinicsanddoctors.ui.rate;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.RateRequest;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Daro on 30/10/2017.
 */

public class RatePresenter implements RateContract.Presenter {

    private RateContract.View mView;
    private Context mContext;

    public RatePresenter(RateContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void sendRate(String clinicId, String doctorId, int value, String reason, String comment) {

        mView.showProgressDialog();
        RateRequest rateRequest = new RateRequest()
                .setUser_id("" + AppPreference.getUser(mContext).getId())
                .setValue(value)
                .setReason(reason.trim())
                .setComment(comment.trim());

        if (clinicId != null && !clinicId.isEmpty())
            rateRequest.setClinic_id(clinicId);

        if (doctorId != null && !doctorId.isEmpty())
            rateRequest.setDoctor_id(doctorId);

        ClinicServices.getServiceClient().sendRate(rateRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(JSONObject jsonObject) {
        mView.hideProgressDialog();
        mView.onSuccessRating();
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
