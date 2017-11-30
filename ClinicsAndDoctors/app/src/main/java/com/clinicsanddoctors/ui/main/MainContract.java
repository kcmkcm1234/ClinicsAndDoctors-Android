package com.clinicsanddoctors.ui.main;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.Category;

import java.util.List;

/**
 * Created by Daro on 10/08/2017.
 */

public interface MainContract {

    interface View extends LoaderView {
        void showCategory(List<Category> categories);
        void onSuccessLogout();
    }

    interface Presenter {
        void getCategory();
        void logout();
    }
}
