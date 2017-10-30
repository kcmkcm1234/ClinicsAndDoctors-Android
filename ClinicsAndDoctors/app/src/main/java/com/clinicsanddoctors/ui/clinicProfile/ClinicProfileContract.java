package com.clinicsanddoctors.ui.clinicProfile;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.Doctor;

import java.util.List;

/**
 * Created by Daro on 13/10/2017.
 */

public interface ClinicProfileContract {

    interface View extends LoaderView {
        void showCategory(List<Category> categories);

        void onSuccessAdd();

        void onSuccessRemove();
    }

    interface Presenter {
        void getCategory();

        void addToFavorite(Clinic clinic);

        void removeFromFavorite(Clinic clinic);
    }
}
