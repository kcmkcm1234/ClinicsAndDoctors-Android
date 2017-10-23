package com.clinicsanddoctors.ui.favorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.adapters.FavoriteAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daro on 23/10/2017.
 */

public class FavoriteFragment extends Fragment {

    private View view;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
        mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.doctors));
        titles.add(getString(R.string.clinics));

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        mViewPager.setAdapter(new FavoriteAdapter(getContext(), getFragmentManager(), titles));
        mViewPager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }
}
