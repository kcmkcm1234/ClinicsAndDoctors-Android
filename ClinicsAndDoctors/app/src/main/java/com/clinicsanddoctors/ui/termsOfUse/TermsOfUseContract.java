package com.clinicsanddoctors.ui.termsOfUse;


import com.clinicsanddoctors.contracts.LoaderView;

/**
 * Created by Daro on 09/08/2017.
 */

public interface TermsOfUseContract {
    interface View extends LoaderView {
        void showData(String description);
    }

    interface Presenter {
        void getTermsOfUse();
    }
}
