package com.clinicsanddoctors.ui.doctorProfile;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.remote.ClinicServices;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;


public class DoctorProfilePresenter implements DoctorProfileContract.Presenter {

    private DoctorProfileContract.View mView;
    private Context mContext;

    public DoctorProfilePresenter(DoctorProfileContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void rate(float rate, String comment, ClinicAndDoctor clinicAndDoctor) {

        mView.showProgressDialog();
        mView.onRateSuccess();
    }

    private void onSuccess(JSONObject storeResponse) {
        mView.hideProgressDialog();
        mView.onRateSuccess();
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
