package com.clinicsanddoctors.ui.signIn;


import com.clinicsanddoctors.contracts.LoaderView;

public interface SignInContract {
    interface View extends LoaderView {
        void onSuccessSignIn();
    }

    interface Presenter {
        void attendOnClickSigIn(String userName, String password);
    }
}
