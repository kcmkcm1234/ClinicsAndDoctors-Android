package com.clinicsanddoctors.ui.privacyPolicy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinicsanddoctors.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Daro on 09/08/2017.
 */

public class PrivacyPolicyFragment extends Fragment implements PrivacyPolicyContract.View {

    private View view;
    private SweetAlertDialog mSweetAlertDialog;
    private PrivacyPolicyPresenter mPresenter;
    private TextView mDescription;

    public PrivacyPolicyFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        mPresenter = new PrivacyPolicyPresenter(this, getContext());
        mDescription = (TextView) view.findViewById(R.id.mDescription);

        mPresenter.getPrivacyPolicy();
        return view;
    }

    @Override
    public void showProgressDialog() {
        mSweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(getString(R.string.app_name));
        mSweetAlertDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        mSweetAlertDialog.hide();
    }

    @Override
    public void showErrorAlert(String message) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.alert_title_error));
        sweetAlertDialog.setContentText(message);
        sweetAlertDialog.show();
    }

    @Override
    public void showData(String description) {
        mDescription.setText(Html.fromHtml(description));
    }
}
