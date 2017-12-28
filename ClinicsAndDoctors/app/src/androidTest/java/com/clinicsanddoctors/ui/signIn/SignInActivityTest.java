package com.clinicsanddoctors.ui.signIn;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by Daro on 28/12/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInActivityTest {

    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(SignInActivity.class);

    @Test
    public void loginTest() {

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
    }
}