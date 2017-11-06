package com.clinicsanddoctors.ui.main.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.adapters.CategoryAdapter;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.ui.BaseClinicFragment;
import com.clinicsanddoctors.ui.clinicProfile.ClinicProfileActivity;
import com.clinicsanddoctors.ui.main.MainActivity;

import java.util.List;

/**
 * Created by Daro on 28/07/2017.
 */

public class ListFragment extends BaseClinicFragment implements ListContract.View {

    private View view;
    private List<Clinic> mClinicCluster;
    private ListPresenter mPresenter;
    private ViewPager mViewPager;
    private ImageView mAdBanner;
    private TabLayout mTabLayout;

    private Location mLocationMap = new Location("");
    private Category mCurrentCategory;
    private boolean isFromCluster;
    private Clinic mClinic;

    public ListFragment() {
    }

    @SuppressLint("ValidFragment")
    public ListFragment(List<Clinic> clinicCluster, Location location, Category category, boolean isFromCluster, Clinic clinic) {
        mClinicCluster = clinicCluster;
        mLocationMap = location;
        mClinic = clinic;
        mCurrentCategory = category;
        this.isFromCluster = isFromCluster;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        mPresenter = new ListPresenter(this, getContext());

        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        mAdBanner = (ImageView) view.findViewById(R.id.mAdBanner);
        mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);

        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).getCategories();
        else {
            showCategory(((ClinicProfileActivity) getActivity()).getCategories());
        }
        return view;
    }

    @Override
    public void showCategory(List<Category> categories) {

        if (categories != null && !categories.isEmpty()) {
            int position = 0;

            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                if (mCurrentCategory != null && category.getId().equalsIgnoreCase(mCurrentCategory.getId()))
                    position = i;
                mTabLayout.addTab(mTabLayout.newTab().setText(category.getName()));
            }

            mViewPager.setAdapter(new CategoryAdapter(isFromCluster, getContext(), getFragmentManager(),
                    categories, mLocationMap, mClinicCluster,
                    mCurrentCategory, mClinic));
            mViewPager.setOffscreenPageLimit(categories.size());
            mViewPager.setCurrentItem(position, false);
            mTabLayout.setupWithViewPager(mViewPager);

            for (int i = 0; i < categories.size(); i++) {
                Category category = categories.get(i);
                TextView tabSimple = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.simple_tab, null);
                tabSimple.setText(category.getName());
                mTabLayout.getTabAt(i).setCustomView(tabSimple);
            }
        }
    }

    private void openWebsite(String urlLink) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(urlLink));
        startActivity(i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelTimer();
    }
}
