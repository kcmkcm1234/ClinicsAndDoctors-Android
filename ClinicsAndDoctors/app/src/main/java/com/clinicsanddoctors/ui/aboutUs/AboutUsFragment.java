package com.clinicsanddoctors.ui.aboutUs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.ui.BaseClinicFragment;


public class AboutUsFragment extends BaseClinicFragment {


    public AboutUsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.mAboutUsText);

        String language = getResources().getConfiguration().locale.getLanguage();
        if(language.equalsIgnoreCase("ar"))
            image.setImageResource(R.drawable.about_us_arabe);
        else
            image.setImageResource(R.drawable.about_us_eng);

        return view;
    }

}
