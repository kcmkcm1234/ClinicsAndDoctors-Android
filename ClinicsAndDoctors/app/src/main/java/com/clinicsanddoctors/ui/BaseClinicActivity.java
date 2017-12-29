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
    static boolean isActive = false;

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
        if (isActive && !isFinishing()) {
            hideProgressDialog();
            loaderDialog = new LoaderDialog(this, R.style.NewDialog);
            loaderDialog.show();
        }
    }

    @Override
    public void hideProgressDialog() {
        /*
        if (mSweetAlertDialog!=null && mSweetAlertDialog.isShowing())
        mSweetAlertDialog.hide();
        */
        if (isActive && !isFinishing()) {
            if (loaderDialog != null && loaderDialog.isShowing())
                loaderDialog.hide();
        }
    }

    @Override
    public void showErrorAlert(String message) {
        if (isActive && !isFinishing()) {
            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            sweetAlertDialog.setTitleText(getString(R.string.alert_title_error));
            sweetAlertDialog.setContentText(message);
            if (isActive && !isFinishing())
                if (!isFinishing())
                    sweetAlertDialog.show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }

}
