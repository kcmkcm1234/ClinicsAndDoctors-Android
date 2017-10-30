package com.clinicsanddoctors.ui.rate;

import com.clinicsanddoctors.contracts.LoaderView;

/**
 * Created by Daro on 30/10/2017.
 */

public interface RateContract {

    interface View extends LoaderView {
        void onSuccessRating();
    }

    interface Presenter {

        void sendRate(String clinicID, String doctorId, int value, String reason, String comment);
    }
}
