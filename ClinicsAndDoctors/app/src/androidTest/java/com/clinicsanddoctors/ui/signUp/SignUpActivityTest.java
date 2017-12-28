package com.clinicsanddoctors.ui.signUp;

import android.net.Uri;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Daro on 28/12/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> mActivityRule = new ActivityTestRule<>(SignUpActivity.class);

    @Test
    public void signUpTest() throws Exception {
        SignUpActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            activity.getPresenter().validateData("+541122112211", "admin@admin.com", "Admin User", "123456", "123456");
            activity.getPresenter().validateData(null, null, null, null, null);
            activity.getPresenter().validateData("", "", "", "", "");
            //short password
            activity.getPresenter().validateData("+541122112211", "admin@admin.com", "Admin User", "12", "12");
            //different password
            activity.getPresenter().validateData("+541122112211", "admin@admin.com", "Admin User", "123456", "12345678");
            //invalid email
            activity.getPresenter().validateData("+541122112211", "test123", "Admin User", "123456", "123456");
            //invalid mobile
            activity.getPresenter().validateData("test123", "admin@admin.com", "Admin User", "123456", "123456");
        });
    }

    @Test
    public void showProfilePhotoTest() throws Exception {
        SignUpActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            activity.showProfilePhoto(null);
            activity.showProfilePhoto(Uri.parse(""));
        });
    }

}