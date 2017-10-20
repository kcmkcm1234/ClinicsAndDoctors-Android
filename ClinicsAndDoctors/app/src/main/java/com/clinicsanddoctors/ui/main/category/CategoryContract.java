package com.clinicsanddoctors.ui.main.category;

import android.location.Location;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.entity.Doctor;

import java.util.List;

/**
 * Created by Daro on 09/08/2017.
 */

public interface CategoryContract {

    interface View extends LoaderView {
        void showDoctors(List<Doctor> doctors);

    }

    interface Presenter {
        void getDoctors(Category category, Location location, int radius);
        void getDoctorsFromClinic(Clinic clinic);
    }
}
