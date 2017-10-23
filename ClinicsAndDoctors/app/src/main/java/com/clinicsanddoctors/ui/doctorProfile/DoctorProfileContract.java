package com.clinicsanddoctors.ui.doctorProfile;


import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;

/**
 * Created by Daro on 07/08/2017.
 */

public interface DoctorProfileContract {
    interface View extends LoaderView {
        void onSuccessAdd();

        void onSuccessRemove();
    }

    interface Presenter {
        void addToFavorite();

        void removeFromFavorite();
    }
}
