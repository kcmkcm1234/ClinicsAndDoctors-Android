package com.clinicsanddoctors.ui.clinicProfile;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.AddRemoveFavoriteRequest;
import com.clinicsanddoctors.data.remote.respons.CategoryResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Daro on 13/10/2017.
 */

public class ClinicProfilePresenter implements ClinicProfileContract.Presenter {

    private ClinicProfileContract.View mView;
    private Context mContext;

    public ClinicProfilePresenter(ClinicProfileContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getCategory() {

        mView.showProgressDialog();
        ClinicServices.getServiceClient().getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(categoryResponse -> categoryResponse)
                .map(this::getCategory)
                .toList()
                .subscribe(this::onSuccess, this::onError);
    }

    @Override
    public void addToFavorite(Clinic clinic) {

        AddRemoveFavoriteRequest addRemoveFavoriteRequest = new AddRemoveFavoriteRequest();
        addRemoveFavoriteRequest.setUser_id("" + AppPreference.getUser(mContext).getId());
        addRemoveFavoriteRequest.setClinic_id(clinic.getId());

        mView.showProgressDialog();
        ClinicServices.getServiceClient().addFavorite(addRemoveFavoriteRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessAddFavorite, this::onError);
    }

    private void onSuccessAddFavorite(JSONObject jsonObject) {
        mView.hideProgressDialog();
        mView.onSuccessAdd();
    }

    @Override
    public void removeFromFavorite(Clinic clinic) {
        AddRemoveFavoriteRequest addRemoveFavoriteRequest = new AddRemoveFavoriteRequest();
        addRemoveFavoriteRequest.setUser_id("" + AppPreference.getUser(mContext).getId());
        addRemoveFavoriteRequest.setClinic_id(clinic.getId());

        mView.showProgressDialog();
        ClinicServices.getServiceClient().removeFavorite(addRemoveFavoriteRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccessRemoveFavorite, this::onError);
    }

    private void onSuccessRemoveFavorite(JSONObject jsonObject) {
        mView.hideProgressDialog();
        mView.onSuccessRemove();
    }

    private void onSuccess(List<Category> categories) {
        if (categories == null) categories = new ArrayList<>();
        mView.hideProgressDialog();
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
}
