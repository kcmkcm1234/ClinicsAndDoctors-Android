package com.clinicsanddoctors.ui.clinicProfile;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.Category;

import java.util.List;

/**
 * Created by Daro on 13/10/2017.
 */

public interface ClinicProfileContract {

    interface View extends LoaderView {
        void showCategory(List<Category> categories);
    }

    interface Presenter {
        void getCategory();
    }
}
