package com.clinicsanddoctors.ui.doctorProfile;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.AddRemoveFavoriteRequest;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DoctorProfilePresenter implements DoctorProfileContract.Presenter {

    private DoctorProfileContract.View mView;
    private Context mContext;

    public DoctorProfilePresenter(DoctorProfileContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void addToFavorite(Doctor doctor) {

        AddRemoveFavoriteRequest addRemoveFavoriteRequest = new AddRemoveFavoriteRequest();
        addRemoveFavoriteRequest.setUser_id("" + AppPreference.getUser(mContext).getId());
        addRemoveFavoriteRequest.setDoctor_id(doctor.getId());

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
    public void removeFromFavorite(Doctor doctor) {
        AddRemoveFavoriteRequest addRemoveFavoriteRequest = new AddRemoveFavoriteRequest();
        addRemoveFavoriteRequest.setUser_id("" + AppPreference.getUser(mContext).getId());
        addRemoveFavoriteRequest.setDoctor_id(doctor.getId());

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
