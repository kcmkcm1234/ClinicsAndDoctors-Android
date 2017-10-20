package com.clinicsanddoctors.ui.privacyPolicy;


import com.clinicsanddoctors.contracts.LoaderView;

/**
 * Created by Daro on 09/08/2017.
 */

public interface PrivacyPolicyContract {
    interface View extends LoaderView {
        void showData(String description);
    }

    interface Presenter {
        void getPrivacyPolicy();
    }
}
