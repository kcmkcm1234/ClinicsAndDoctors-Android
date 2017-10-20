package com.clinicsanddoctors.ui.termsOfUse;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.SettingDescription;
import com.clinicsanddoctors.data.remote.ClinicServices;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class TermsOfUsePresenter implements TermsOfUseContract.Presenter {

    private TermsOfUseContract.View mView;
    private Context mContext;

    public TermsOfUsePresenter(TermsOfUseContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getTermsOfUse() {
        mView.showProgressDialog();
        ClinicServices.getServiceClient().getTermsOfUse()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(SettingDescription settingDescription) {
        mView.hideProgressDialog();
        mView.showData(settingDescription.getDescription());
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
