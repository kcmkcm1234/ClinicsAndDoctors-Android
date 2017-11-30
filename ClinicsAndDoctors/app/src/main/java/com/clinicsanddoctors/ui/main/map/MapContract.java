package com.clinicsanddoctors.ui.main.map;

import android.graphics.Bitmap;
import android.location.Location;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;

import java.util.List;

import rx.Observable;
import rx.Subscription;

/**
 * Created by Daro on 27/07/2017.
 */

public interface MapContract {

    interface View extends LoaderView {
        void showClinics(List<Clinic> clinics);
        void loadResults(List<ClinicAndDoctorResponse> clinicAndDoctorResponses);
    }

    interface Presenter {

        void addSubscription(Subscription subscription);

        Observable<Bitmap> getCustomMarker(android.view.View marker, ClinicAndDoctor clinicAndDoctor);

        void getClinics(Category category, Location location, int radius, boolean isFromTab);
        void search(String query);
    }
}

