package com.clinicsanddoctors.ui.doctorProfile;


import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;

/**
 * Created by Daro on 07/08/2017.
 */

public interface DoctorProfileContract {
    interface View extends LoaderView {
        void onSuccessAdd();

        void onSuccessRemove();
    }

    interface Presenter {
        void addToFavorite(Doctor doctor);

        void removeFromFavorite(Doctor doctor);
    }
}
