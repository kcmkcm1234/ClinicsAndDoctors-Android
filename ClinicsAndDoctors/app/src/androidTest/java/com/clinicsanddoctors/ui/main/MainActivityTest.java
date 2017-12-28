package com.clinicsanddoctors.ui.main;

import android.os.Handler;
import android.support.test.rule.ActivityTestRule;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.ui.main.map.MapFragment;
import com.clinicsanddoctors.ui.profile.ProfileFragment;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daro on 28/12/2017.
 */
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void showCategoryTest() throws Exception {
        MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            activity.showCategory(null);
            activity.showCategory(new ArrayList<>());

            ArrayList arrayList = new ArrayList<>();
            arrayList.add(new Category().setName("Category 1").setIcon("").setId(""));
            arrayList.add(new Category().setName("Category 2").setIcon(null).setId(""));
            arrayList.add(new Category().setName("Category 3").setIcon("").setId(null));
            arrayList.add(new Category().setName(null).setIcon("").setId(""));
            arrayList.add(null);
            arrayList.add(new Category());
            activity.showCategory(arrayList);
        });
    }


    @Test
    public void checkMapFragment() {
        MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            MapFragment mapFragment = new MapFragment();
            activity.changeFragmentSection(activity.getString(R.string.app_name), mapFragment);

            mapFragment.showClinics(new ArrayList<>());
            mapFragment.showClinics(null);
            List<Clinic> clinicAndDoctors = new ArrayList<Clinic>();
            clinicAndDoctors.add(new Clinic());
            mapFragment.showClinics(clinicAndDoctors);
        });
    }

    @Test
    public void checkProfileFragment() {
        MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            ProfileFragment profileFragment = new ProfileFragment();
            activity.changeFragmentSection(activity.getString(R.string.menu_title_profile), profileFragment);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    profileFragment.getPresenter().editProfile("Admin User", "+541122112211", "admin@admin.com", "123456", "123456");
                    profileFragment.getPresenter().editProfile(null, null, null, null, null);
                    profileFragment.getPresenter().editProfile("", "", "", "", "");
                    //short password
                    profileFragment.getPresenter().editProfile("Admin User", "+541122112211", "admin@admin.com", "12", "12");
                    //different password
                    profileFragment.getPresenter().editProfile("Admin User", "+541122112211", "admin@admin.com", "123456", "12345678");
                    //invalid email
                    profileFragment.getPresenter().editProfile("Admin User", "+541122112211", "test123", "123456", "123456");
                    //invalid mobile
                    profileFragment.getPresenter().editProfile("Admin User", "test123", "admin@admin.com", "123456", "123456");
                }
            }, 1000);

        });
    }

    @Test
    public void checkListModeFragment() {
        MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            activity.showListMode(false);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    activity.getListFragment().showCategory(null);
                    activity.getListFragment().showCategory(new ArrayList<>());

                    ArrayList arrayList = new ArrayList<>();
                    arrayList.add(new Category().setName("Category 1").setIcon("").setId(""));
                    arrayList.add(new Category().setName("Category 2").setIcon(null).setId(""));
                    arrayList.add(new Category().setName("Category 3").setIcon("").setId(null));
                    arrayList.add(new Category().setName(null).setIcon("").setId(""));
                    arrayList.add(null);
                    arrayList.add(new Category());
                    activity.getListFragment().showCategory(arrayList);
                }
            }, 5000);
        });
    }
}