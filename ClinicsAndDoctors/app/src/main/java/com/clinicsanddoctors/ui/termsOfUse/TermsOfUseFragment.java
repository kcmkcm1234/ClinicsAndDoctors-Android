package com.clinicsanddoctors.ui.termsOfUse;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.ui.BaseClinicFragment;

/**
 * Created by Daro on 09/08/2017.
 */

public class TermsOfUseFragment extends BaseClinicFragment implements TermsOfUseContract.View {

    private View view;
    private TextView mDescription;
    private TermsOfUsePresenter mPresenter;

    public TermsOfUseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        mPresenter = new TermsOfUsePresenter(this, getContext());
        mDescription = (TextView) view.findViewById(R.id.mDescription);

        mPresenter.getTermsOfUse();
        return view;
    }

    @Override
    public void showData(String description) {
        mDescription.setText(Html.fromHtml(description));
    }
}
