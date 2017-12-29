package com.clinicsanddoctors.ui.doctorProfile;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Doctor;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Daro on 29/12/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DoctorProfileActivityTest {

    private int SLEEP_TIME = 3000;
    @Rule
    public ActivityTestRule<DoctorProfileActivity> mActivityRule = new ActivityTestRule<>(DoctorProfileActivity.class);

    @Test
    public void initDoctorDataFragment() throws Exception {
        DoctorProfileActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            activity.setDataDoctor(null);
            activity.setDataDoctor(new Doctor());
            activity.setDataDoctor(new Doctor(new ClinicAndDoctorResponse(), null));
            activity.setDataDoctor(new Doctor(new ClinicAndDoctorResponse(), new Category()));

            Doctor doctor =  new Doctor();
            doctor.setName("Doctor Name").setCategory(new Category().setName("Category 1"));
            doctor.setRating("5");
            doctor.setPhoneNumber("123456");
            activity.setDataDoctor(doctor);
        });
        Thread.sleep(SLEEP_TIME);
    }
}