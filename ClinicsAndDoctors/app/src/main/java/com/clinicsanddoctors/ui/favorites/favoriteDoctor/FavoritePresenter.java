package com.clinicsanddoctors.ui.favorites.favoriteDoctor;

import android.content.Context;
import android.location.Location;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.FavoriteRequest;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;
import com.clinicsanddoctors.data.remote.respons.DoctorResponse;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Daro on 23/10/2017.
 */

public class FavoritePresenter implements FavoriteContract.Presenter {

    private FavoriteContract.View mView;
    private Context mContext;

    public FavoritePresenter(FavoriteContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getDoctorFavorite() {

        if (AppPreference.getUser(mContext) == null) return;

        mView.showProgressDialog();

        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUser_id("" + AppPreference.getUser(mContext).getId());
        favoriteRequest.setType(ClinicServices.FavoriteType.DOCTOR);

        ClinicServices.getServiceClient().getDoctorsFavorites(favoriteRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(equineResponse -> equineResponse)
                .map(this::newDoctor)
                .toList()
                .subscribe(this::onSuccessDoctors, this::onError);
    }

    private Doctor newDoctor(DoctorResponse doctorResponse) {
        return new Doctor(doctorResponse, null);
    }

    private void onSuccessDoctors(List<Doctor> doctorList) {
        mView.hideProgressDialog();
        mView.showDoctorFavorite(doctorList);
    }

    @Override
    public void getClinicFavorite() {
        if (AppPreference.getUser(mContext) == null) return;
        mView.showProgressDialog();
        FavoriteRequest favoriteRequest = new FavoriteRequest();
        favoriteRequest.setUser_id("" + AppPreference.getUser(mContext).getId());
        favoriteRequest.setType(ClinicServices.FavoriteType.CLINIC);

        ClinicServices.getServiceClient().getClinicsFavorites(favoriteRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(equineResponse -> equineResponse)
                .map(this::getNewClinic)
                .toList()
                .subscribe(this::onSuccessClinics, this::onError);
    }

    private void onSuccessClinics(List<Clinic> clinics) {
        mView.hideProgressDialog();
        mView.showClinicFavorite(clinics);
    }

    private Clinic getNewClinic(ClinicAndDoctorResponse storeResponse) {
        return new Clinic(storeResponse, null);
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

    private LatLng getRandomLocation(LatLng point) {

        int radius = 1000;
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for (int i = 0; i < 10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;
            Random random = new Random();
            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));
        }
        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);
    }
}
