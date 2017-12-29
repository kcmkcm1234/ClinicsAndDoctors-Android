package com.clinicsanddoctors.ui.clinicProfile;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Daro on 29/12/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ClinicProfileActivityTest {

    private int SLEEP_TIME = 3000;

    @Rule
    public ActivityTestRule<ClinicProfileActivity> mActivityRule = new ActivityTestRule<>(ClinicProfileActivity.class);

    @Test
    public void initClinicDataFragment() throws Exception {
        ClinicProfileActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            activity.setDataClinic(null);
            activity.setDataClinic(new Clinic());

            Clinic clinic = new Clinic();
            clinic.setName("Clinic Name").setCategory(new Category().setName("Category 1"));
            clinic.setRating("5");
            clinic.setPhoneNumber("123456");
            activity.setDataClinic(clinic);
        });
        Thread.sleep(SLEEP_TIME);
    }

    @Test
    public void initClinicTrueDataFragment() throws Exception {
        ClinicProfileActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            Clinic clinic = new Clinic();
            clinic.setId("11").setName("Central Medical Institute Sa").setCategory(new Category().setName("Ophthalmology").setId("4"));
            clinic.setRating("5");
            clinic.setPhoneNumber("123456");
            activity.setDataClinic(clinic);
        });
        Thread.sleep(SLEEP_TIME);
    }
}