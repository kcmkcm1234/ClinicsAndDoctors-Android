package com.clinicsanddoctors.ui.favorites.favoriteDoctor;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;

import java.util.List;

/**
 * Created by Daro on 23/10/2017.
 */

public interface FavoriteContract {

    interface View extends LoaderView {
        void showDoctorFavorite(List<Doctor> doctorList);

        void showClinicFavorite(List<Clinic> clinicList);
    }

    interface Presenter {
        void getDoctorFavorite();

        void getClinicFavorite();
    }
}
