package com.clinicsanddoctors.ui.main;

import android.location.Location;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.AdvertisingPopOver;
import com.clinicsanddoctors.data.entity.Category;

import java.util.List;

/**
 * Created by Daro on 10/08/2017.
 */

public interface MainContract {

    interface View extends LoaderView {
        void showCategory(List<Category> categories);

        void showAdvertising(AdvertisingPopOver advertisingPopOver);

        void onSuccessLogout();
    }

    interface Presenter {
        void getCategory();

        void getAdvertising(Location location);

        void logout();
    }
}
