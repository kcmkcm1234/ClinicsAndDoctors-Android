package com.clinicsanddoctors.ui;

import android.support.v4.app.Fragment;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.ui.dialog.LoaderDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Daro on 03/11/2017.
 */

public class BaseClinicFragment extends Fragment implements LoaderView {

    //private SweetAlertDialog mSweetAlertDialog;
    private LoaderDialog loaderDialog;

    @Override
    public void showProgressDialog() {
        /*
        mSweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(getString(R.string.app_name));
        mSweetAlertDialog.show();
        */
        if (!isAdded()) return;
        hideProgressDialog();
        loaderDialog = new LoaderDialog(getContext(), R.style.NewDialog);
        loaderDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        /*
        if (mSweetAlertDialog != null && mSweetAlertDialog.isShowing())
            mSweetAlertDialog.hide();
            */
        if (!isAdded()) return;
        if (loaderDialog != null && loaderDialog.isShowing())
            loaderDialog.hide();
    }

    @Override
    public void showErrorAlert(String message) {
        if (!isAdded()) return;
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.alert_title_error));
        sweetAlertDialog.setContentText(message);
        sweetAlertDialog.show();
    }
}
