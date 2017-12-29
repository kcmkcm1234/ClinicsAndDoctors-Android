package com.clinicsanddoctors.ui.main;

import android.os.Handler;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.ui.favorites.FavoriteFragment;
import com.clinicsanddoctors.ui.main.list.ListFragment;
import com.clinicsanddoctors.ui.main.map.MapFragment;
import com.clinicsanddoctors.ui.profile.ProfileFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daro on 28/12/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private int SLEEP_TIME = 3000;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void showCategory() throws Exception {
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
        Thread.sleep(SLEEP_TIME);
    }


    @Test
    public void checkMapFragment() throws Exception {
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
        Thread.sleep(SLEEP_TIME);
    }

    @Test
    public void checkProfileFragmentEditTest() throws Exception {
        MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {
            ProfileFragment profileFragment = new ProfileFragment();
            activity.changeFragmentSection(activity.getString(R.string.menu_title_profile), profileFragment);

            new Handler().postDelayed(() -> {
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

            }, 1000);

        });
        Thread.sleep(SLEEP_TIME);
    }

    @Test
    public void checkProfileFragmentShowDataTest() throws Exception {
        MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {


            new Handler().postDelayed(() -> {
                ProfileFragment profileFragment = new ProfileFragment();
                activity.changeFragmentSection(activity.getString(R.string.menu_title_profile), profileFragment);

                new Handler().postDelayed(() -> {
                    UserClient userClient = new UserClient();
                    //show data of user
                    profileFragment.showProfile(null);
                    profileFragment.showProfile(userClient);

                    userClient.setEmail("");
                    userClient.setMobile("");
                    userClient.setFullName("");
                    userClient.setUrl("");
                    profileFragment.showProfile(userClient);

                    userClient.setEmail(null);
                    userClient.setMobile(null);
                    userClient.setFullName(null);
                    userClient.setUrl(null);
                    profileFragment.showProfile(userClient);

                    userClient.setEmail("admin@admin.com");
                    userClient.setMobile("12345678");
                    userClient.setFullName("Admin User");
                    userClient.setUrl("fake url image");
                    profileFragment.showProfile(userClient);

                }, 1000);

            }, 1000);

        });
        Thread.sleep(SLEEP_TIME);
    }

    @Test
    public void checkListModeFragment() throws Exception {
        MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {

            new Handler().postDelayed(() -> {
                ListFragment mListFragment = new ListFragment();
                activity.changeFragmentSection(activity.getString(R.string.app_name), mListFragment);

                new Handler().postDelayed(() -> {

                    mListFragment.showCategory(null);
                    mListFragment.showCategory(new ArrayList<>());

                    ArrayList arrayList = new ArrayList<>();
                    arrayList.add(new Category().setName("Category 1").setIcon("").setId(""));
                    arrayList.add(new Category().setName("Category 2").setIcon(null).setId(""));
                    arrayList.add(new Category().setName("Category 3").setIcon("").setId(null));
                    arrayList.add(new Category().setName(null).setIcon("").setId(""));
                    arrayList.add(null);
                    arrayList.add(new Category());
                    mListFragment.showCategory(arrayList);
                }, 1000);

            }, 1000);

        });
        Thread.sleep(SLEEP_TIME);
    }

    @Test
    public void checkFavoriteFragment() throws Exception {
        MainActivity activity = mActivityRule.getActivity();
        activity.runOnUiThread(() -> {

            new Handler().postDelayed(() -> {
                FavoriteFragment fragment = new FavoriteFragment();
                activity.changeFragmentSection(activity.getString(R.string.menu_title_favorite), fragment);
            }, 1000);
        });
        Thread.sleep(SLEEP_TIME);
    }
}