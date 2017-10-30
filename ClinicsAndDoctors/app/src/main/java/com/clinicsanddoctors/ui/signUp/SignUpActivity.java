package com.clinicsanddoctors.ui.signUp;

import android.Manifest;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.ui.BaseClinicActivity;
import com.clinicsanddoctors.ui.main.MainActivity;
import com.tbruyelle.rxpermissions.RxPermissions;


public class SignUpActivity extends BaseClinicActivity implements SignUpContract.View {

    private SignUpPresenter mPresenter;

    private EditText mEmail, mMobile, mFullName, mPassword, mConfirmPassword;
    private ImageView mPhoto;
    private TextInputLayout mLabelEmail, mLabelMobile, mLabelFullName, mLabelPassword, mLabelConfirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setupToolbar(getString(R.string.title_create_account));
        mPresenter = new SignUpPresenter(this, this);

        mPhoto = (ImageView) findViewById(R.id.mPhoto);
        mEmail = (EditText) findViewById(R.id.mEmail);
        mMobile = (EditText) findViewById(R.id.mMobile);
        mFullName = (EditText) findViewById(R.id.mFullName);
        mPassword = (EditText) findViewById(R.id.mPassword);
        mConfirmPassword = (EditText) findViewById(R.id.mConfirmPassword);
        mPassword.setTransformationMethod(new PasswordTransformationMethod());
        mConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());

        mLabelEmail = (TextInputLayout) findViewById(R.id.mLabelEmail);
        mLabelMobile = (TextInputLayout) findViewById(R.id.mLabelMobile);
        mLabelFullName = (TextInputLayout) findViewById(R.id.mLabelFullName);
        mLabelPassword = (TextInputLayout) findViewById(R.id.mLabelPassword);
        mLabelConfirmPassword = (TextInputLayout) findViewById(R.id.mLabelConfirmPassword);

        final Typeface tf = Typeface.createFromAsset(getAssets(), getString(R.string.font_compact_avenir_bold));
        mLabelEmail.setTypeface(tf);
        mLabelMobile.setTypeface(tf);
        mLabelFullName.setTypeface(tf);
        mLabelPassword.setTypeface(tf);
        mLabelConfirmPassword.setTypeface(tf);

        mPhoto.setOnClickListener(v -> onClickPhoto());
        /*
        findViewById(R.id.mSignUp).setOnClickListener(v -> {
                    String mobile = mMobile.getText().toString();
                    mPresenter.validateData(mobile, mEmail.getText().toString(),
                            mFullName.getText().toString(),
                            mPassword.getText().toString(), mConfirmPassword.getText().toString());
                }
        );
        */
        findViewById(R.id.mSignUp).setOnClickListener(v -> {
            String mobile = mMobile.getText().toString();
            mPresenter.validateData(mobile, mEmail.getText().toString(),
                    mFullName.getText().toString(),
                    mPassword.getText().toString(), mConfirmPassword.getText().toString());
        });

    }

    void onClickPhoto() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) mPresenter.takePicture();
                    else showErrorAlert(getString(R.string.error_permission));
                });
    }

    @Override
    public void onSuccessSignUp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SignUpContract.Presenter.PICTURE_REQUEST_CODE)
            mPresenter.attendOnClickPhoto(resultCode, data);
    }

    @Override
    public void showProfilePhoto(Uri uriPhoto) {
        Glide.with(this)
                .load(uriPhoto)
                .dontAnimate()
                .into(mPhoto);
    }

    @Override
    public void promptUserForPhoto(Intent photoIntent, int requestCode) {
        startActivityForResult(photoIntent, requestCode);
    }
}
