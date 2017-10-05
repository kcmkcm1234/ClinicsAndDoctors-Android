package com.clinicsanddoctors.ui.forgetPassword;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.ForgotPasswordRequest;
import com.clinicsanddoctors.ui.BaseClinicActivity;

import org.json.JSONObject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ForgetPasswordActivity extends BaseClinicActivity {

    private EditText mEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setupToolbar(getString(R.string.title_forgot_apssword));

        mEmail = (EditText) findViewById(R.id.mEmail);
        findViewById(R.id.mRecoverPassword).setOnClickListener(v -> sendEmail(mEmail.getText().toString()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void sendEmail(String email) {
        if (email.isEmpty()) {
            Toast.makeText(this, "You must enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            showErrorAlert(this.getString(R.string.error_invalid_email));
            return;
        }

        showProgressDialog();
        ClinicServices.getServiceClient().forgotPassword(new ForgotPasswordRequest(email))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
    }

    private boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void onSuccess(JSONObject jsonObject) {
        hideProgressDialog();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEmail.getWindowToken(), 0);
        Toast.makeText(this, "You will receive an email", Toast.LENGTH_LONG).show();
        finish();
    }

    private void onError(Throwable throwable) {
        hideProgressDialog();
        showErrorAlert(this.getString(R.string.error_user_nor_found));
    }
}
