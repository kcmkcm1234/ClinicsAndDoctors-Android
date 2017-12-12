package com.clinicsanddoctors.ui.main;

import android.content.Context;
import android.os.Handler;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.LogoutRequest;
import com.clinicsanddoctors.data.remote.requests.SearchRequest;
import com.clinicsanddoctors.data.remote.respons.CategoryResponse;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Daro on 10/08/2017.
 */

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private Context mContext;
    private List<Category> categories;

    public MainPresenter(MainContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getCategory() {

        if (categories != null && !categories.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mView.showCategory(categories);
                }
            }, 500);
            return;
        }

        //mView.showProgressDialog();
        ClinicServices.getServiceClient().getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(categoryResponse -> categoryResponse)
                .map(this::getCategory)
                .toList()
                .subscribe(this::onSuccess, this::onError);
    }

    private void onSuccess(List<Category> categories) {
        mView.hideProgressDialog();
        if (categories == null) categories = new ArrayList<>();
        categories.add(0, new Category().setName("All").setId("0"));
        this.categories = categories;
        mView.showCategory(categories);
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

    private Category getCategory(CategoryResponse categoryResponse) {
        return new Category(categoryResponse);
    }

    @Override
    public void logout() {
        mView.showProgressDialog();
        LogoutRequest logoutRequest = new LogoutRequest();
        UserClient userClient = AppPreference.getUser(mContext);
        logoutRequest.setAccess_token(userClient.getAccessToken());
        logoutRequest.setUser_id("" + userClient.getId());

        ClinicServices.getServiceClient().logout(logoutRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onErrorLogout);
    }

    private void onSuccess(JSONObject jsonObject) {
        mView.hideProgressDialog();
        mView.onSuccessLogout();
    }

    private void onErrorLogout(Throwable throwable) {
        mView.hideProgressDialog();
        mView.onSuccessLogout();
    }
}
