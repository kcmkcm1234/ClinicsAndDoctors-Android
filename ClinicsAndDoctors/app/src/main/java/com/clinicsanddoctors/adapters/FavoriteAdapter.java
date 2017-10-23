package com.clinicsanddoctors.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clinicsanddoctors.ui.favorites.favoriteDoctor.FavoriteClinicAndDoctorFragment;

import java.util.List;

public class FavoriteAdapter extends FragmentStatePagerAdapter {

    private List<String> mTitles;
    private Context mContext;

    public FavoriteAdapter(Context context, FragmentManager fm, List<String> titles) {
        super(fm);
        mContext = context;
        mTitles = titles;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle bundle = new Bundle();
        if (position == 0)
            bundle.putBoolean(FavoriteClinicAndDoctorFragment.ARG_IS_DOCTOR, true);
        else
            bundle.putBoolean(FavoriteClinicAndDoctorFragment.ARG_IS_DOCTOR, false);

        fragment = new FavoriteClinicAndDoctorFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
