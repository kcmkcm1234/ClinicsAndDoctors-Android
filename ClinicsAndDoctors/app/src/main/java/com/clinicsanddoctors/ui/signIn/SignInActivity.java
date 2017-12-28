package com.clinicsanddoctors.ui.signIn;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.TextView;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.forgetPassword.ForgetPasswordActivity;
import com.clinicsanddoctors.ui.main.MainActivity;


public class SignInActivity extends BaseClinicActivity implements SignInContract.View {

    private Toolbar mToolbar;
    private SignInPresenter mPresenter;
    private EditText mPassword, mMobile;
    private TextInputLayout mLabelMobile, mLabelPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mPresenter = new SignInPresenter(this, this);
        setupToolbar(getString(R.string.title_login));

        mMobile = (EditText) findViewById(R.id.mMobile);
        mPassword = (EditText) findViewById(R.id.mPassword);
        mPassword.setTransformationMethod(new PasswordTransformationMethod());
        mLabelMobile = (TextInputLayout) findViewById(R.id.mLabelMobile);
        mLabelPassword = (TextInputLayout) findViewById(R.id.mLabelPassword);

        findViewById(R.id.mForgotPassword).setOnClickListener(v -> forgotPassword());
        findViewById(R.id.mSignIn).setOnClickListener(v ->
                mPresenter.attendOnClickSigIn(mMobile.getText().toString(), mPassword.getText().toString()));

        final Typeface tf = Typeface.createFromAsset(getAssets(), getString(R.string.font_compact_avenir_bold));
        mLabelMobile.setTypeface(tf);
        mLabelPassword.setTypeface(tf);
    }

    private void forgotPassword() {
        startActivity(new Intent(this, ForgetPasswordActivity.class));
    }


    @Override
    public void onSuccessSignIn() {
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public SignInPresenter getPresenter() {
        return mPresenter;
    }
}
