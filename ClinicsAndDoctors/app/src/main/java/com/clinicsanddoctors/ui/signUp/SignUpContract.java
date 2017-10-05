package com.clinicsanddoctors.ui.signUp;

import android.content.Intent;
import android.net.Uri;

import com.clinicsanddoctors.contracts.LoaderView;

public interface SignUpContract {

    interface View extends LoaderView {
        void onSuccessSignUp();

        void showProfilePhoto(Uri uriPhoto);

        void promptUserForPhoto(Intent photoIntent, int requestCode);
    }

    interface Presenter {
        int PICTURE_REQUEST_CODE = 101;

        void validateData(String mobile, String email, String completeName, String password, String confirmPassword);

        void takePicture();

        void attendOnClickPhoto(int resultCode, Intent data);
    }
}
