package com.clinicsanddoctors.adapters;

import android.content.Context;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.ui.main.category.CategoryFragment;

import java.util.List;

public class CategoryAdapter extends FragmentStatePagerAdapter {

    private List<Category> mCategories;
    private Location mLocation;
    private List<Clinic> mClinicsCluster;
    private Category mCategory;
    private Context mContext;
    private boolean isFromCluster;
    private Clinic mClinic;

    public CategoryAdapter(boolean isFromCluster, Context context, FragmentManager fm, List<Category> categories, Location location, List<Clinic> clinicsCluster, Category category, Clinic clinic) {
        super(fm);
        mCategories = categories;
        mLocation = location;
        mClinicsCluster = clinicsCluster;
        mCategory = category;
        mContext = context;
        this.isFromCluster = isFromCluster;
        mClinic = clinic;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        if (mCategory != null && mCategory.getId().equalsIgnoreCase(mCategories.get(position).getId()) && isFromCluster)
            fragment = new CategoryFragment(mCategories.get(position), mLocation, mClinicsCluster, mClinic);
        else
            fragment = new CategoryFragment(mCategories.get(position), mLocation, null, mClinic);
        return fragment;
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
