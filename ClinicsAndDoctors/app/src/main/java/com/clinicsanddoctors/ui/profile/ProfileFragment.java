package com.clinicsanddoctors.ui.profile;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.tbruyelle.rxpermissions.RxPermissions;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Daro on 01/08/2017.
 */

public class ProfileFragment extends Fragment implements ProfileContract.View {

    private ProfilePresenter mPresenter;
    private View view;
    private SweetAlertDialog mSweetAlertDialog;

    private EditText mEmail, mName, mLastName, mPassword, mConfirmPassword, mMobile, mPrefix;
    private ImageView mPhoto;
    private TextView mSave, mPasswordLabel, mConfirmPasswordLabel;
    private boolean isEdited = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        mPresenter = new ProfilePresenter(this, ProfileFragment.this.getContext());

        mSave = (TextView) view.findViewById(R.id.mSave);
        mPasswordLabel = (TextView) view.findViewById(R.id.mPasswordLabel);
        mConfirmPasswordLabel = (TextView) view.findViewById(R.id.mConfirmPasswordLabel);
        mEmail = (EditText) view.findViewById(R.id.mEmail);
        mName = (EditText) view.findViewById(R.id.mName);
        mLastName = (EditText) view.findViewById(R.id.mLastName);
        mPassword = (EditText) view.findViewById(R.id.mPassword);
        mConfirmPassword = (EditText) view.findViewById(R.id.mConfirmPassword);
        mMobile = (EditText) view.findViewById(R.id.mMobile);
        mPrefix = (EditText) view.findViewById(R.id.mPrefix);
        mPhoto = (ImageView) view.findViewById(R.id.mPhoto);

        mPhoto.setEnabled(false);
        mPhoto.setOnClickListener(v -> onClickPhoto());
        mSave.setOnClickListener(v -> mPresenter.editProfile(mName.getText().toString(),
                mLastName.getText().toString(),
                mPrefix.getText().toString() + " " + mMobile.getText().toString(),
                mPassword.getText().toString(),
                mConfirmPassword.getText().toString()));
        mPresenter.getProfile();

        checkFacebookUser();
        return view;
    }

    public void showEditView() {
        mPhoto.setEnabled(true);
        mConfirmPasswordLabel.setVisibility(View.VISIBLE);
        mConfirmPassword.setVisibility(View.VISIBLE);
        mSave.setVisibility(View.VISIBLE);

        view.findViewById(R.id.mPlusPhoto).setVisibility(View.VISIBLE);
        setFieldEnable(mName, true);
        setFieldEnable(mLastName, true);
        setFieldEnable(mPassword, true);
        setFieldEnable(mConfirmPassword, true);
        setFieldEnable(mMobile, true);
        setFieldEnable(mPrefix, true);
        checkFacebookUser();
    }

    private void setFieldEnable(EditText editText, boolean enable) {
        editText.setEnabled(enable);
    }

    private void checkFacebookUser() {
        UserClient userClient = AppPreference.getUser(getContext());
        if (userClient.isFacebookUer()) {
            mPasswordLabel.setVisibility(View.GONE);
            mPassword.setVisibility(View.GONE);
            mConfirmPasswordLabel.setVisibility(View.GONE);
            mConfirmPassword.setVisibility(View.GONE);
        }
    }

    private void onClickPhoto() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) mPresenter.takePicture();
                    else showErrorAlert(getString(R.string.error_permission));
                });
    }

    @Override
    public void showProfile(UserClient userClient) {
        mEmail.setText(userClient.getEmail());
        mName.setText(userClient.getFirstName());
        mLastName.setText(userClient.getLastName());
        mPassword.setText(userClient.getPassword());
        mMobile.setText(userClient.getMobile());
        if (userClient.getMobile() != null && userClient.getMobile().contains(" ")) {
            mPrefix.setText(userClient.getMobile().split(" ")[0]);
            mMobile.setText(userClient.getMobile().split(" ")[1]);
        }

        if (userClient.getUrl() != null && !userClient.getUrl().isEmpty()) {
            Glide.with(getContext()).load(userClient.getUrl())
                    .placeholder(R.drawable.placeholder_clinic)
                    .dontAnimate()
                    .into(mPhoto);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProfileContract.Presenter.PICTURE_REQUEST_CODE)
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

    @Override
    public void onEditSuccess() {
        isEdited = true;
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
    }

    public boolean mustUpdate() {
        if (isEdited) {
            isEdited = false;
            return true;
        }
        return false;
    }

    @Override
    public void showProgressDialog() {
        mSweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(getString(R.string.app_name));
        mSweetAlertDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        mSweetAlertDialog.hide();
    }

    @Override
    public void showErrorAlert(String message) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.alert_title_error));
        sweetAlertDialog.setContentText(message);
        sweetAlertDialog.show();
    }
}
