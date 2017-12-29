package com.clinicsanddoctors.ui.signIn;

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
public class SignInActivityTest {

    private int SLEEP_TIME = 3000;

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void login() throws Exception {

        SignInActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.getPresenter().attendOnClickSigIn("admin@admin.com", "123456");
                activity.getPresenter().attendOnClickSigIn("", "");
                activity.getPresenter().attendOnClickSigIn("", null);
                activity.getPresenter().attendOnClickSigIn(null, "");
                activity.getPresenter().attendOnClickSigIn(null, null);
            }
        });
        Thread.sleep(SLEEP_TIME);
    }
}