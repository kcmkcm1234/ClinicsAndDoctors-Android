package com.clinicsanddoctors.ui.signIn;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.LoginRequest;
import com.clinicsanddoctors.data.remote.respons.RegisterResponse;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SignInPresenter implements SignInContract.Presenter {

    private SignInContract.View mView;
    private Context mContext;

    public SignInPresenter(SignInContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void attendOnClickSigIn(String mobile, String password) {
        if (mobile == null || password == null || mobile.isEmpty() || password.isEmpty()) {
            mView.showErrorAlert(mContext.getString(R.string.complete_all_fields));
            return;
        }

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobile(mobile).setPassword(password);
        mView.showProgressDialog();
        ClinicServices.getServiceClient().login(loginRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respose -> onSuccess(respose, password), this::onError);
    }

    private void onSuccess(RegisterResponse registerResponse, String password) {
        mView.hideProgressDialog();
        UserClient userClient = new UserClient(registerResponse);
        userClient.setPassword(password);
        AppPreference.setUser(mContext, userClient);
        mView.onSuccessSignIn();
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
