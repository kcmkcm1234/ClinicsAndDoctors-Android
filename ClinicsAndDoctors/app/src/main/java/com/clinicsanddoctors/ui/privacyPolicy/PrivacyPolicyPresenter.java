package com.clinicsanddoctors.ui.privacyPolicy;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.SettingDescription;
import com.clinicsanddoctors.data.remote.ClinicServices;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Daro on 09/08/2017.
 */

public class PrivacyPolicyPresenter implements PrivacyPolicyContract.Presenter {

    private PrivacyPolicyContract.View mView;
    private Context mContext;

    public PrivacyPolicyPresenter(PrivacyPolicyContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getPrivacyPolicy() {
        mView.showProgressDialog();
        ClinicServices.getServiceClient().getPrivacyPolicy()
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
