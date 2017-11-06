package com.clinicsanddoctors.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.contracts.LoaderView;
import com.clinicsanddoctors.ui.dialog.LoaderDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Daro on 05/10/2017.
 */

public class BaseClinicActivity extends AppCompatActivity implements LoaderView {

    //private SweetAlertDialog mSweetAlertDialog;
    private LoaderDialog loaderDialog;

    protected void setupToolbar(String title) {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.mTitle);
        mTitle.setText(title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void showProgressDialog() {
        /*
        mSweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(getString(R.string.app_name));
        mSweetAlertDialog.show();
        */
        loaderDialog = new LoaderDialog(this, R.style.NewDialog);
        loaderDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        /*
        if (mSweetAlertDialog!=null && mSweetAlertDialog.isShowing())
        mSweetAlertDialog.hide();
        */
        if (loaderDialog != null && loaderDialog.isShowing())
            loaderDialog.hide();
    }

    @Override
    public void showErrorAlert(String message) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.alert_title_error));
        sweetAlertDialog.setContentText(message);
        sweetAlertDialog.show();
    }
}
