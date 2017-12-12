package com.clinicsanddoctors.ui.aboutUs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.ui.BaseClinicFragment;


public class AboutUsFragment extends BaseClinicFragment {


    public AboutUsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }

}
