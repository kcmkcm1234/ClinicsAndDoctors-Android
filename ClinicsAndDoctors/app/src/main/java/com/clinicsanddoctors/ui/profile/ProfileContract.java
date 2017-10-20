package com.clinicsanddoctors.ui.profile;

import android.content.Intent;
import android.net.Uri;

import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.data.entity.UserClient;


public interface ProfileContract {

    interface View extends LoaderView {
        void showProfile(UserClient userClient);

        void showProfilePhoto(Uri uriPhoto);

        void promptUserForPhoto(Intent photoIntent, int requestCode);

        void onEditSuccess();
    }

    interface Presenter {
        void getProfile();

        int PICTURE_REQUEST_CODE = 101;

        void takePicture();

        void attendOnClickPhoto(int resultCode, Intent data);

        void editProfile(String name, String lastName, String mobile, String password, String confirmPassword);

    }
}
