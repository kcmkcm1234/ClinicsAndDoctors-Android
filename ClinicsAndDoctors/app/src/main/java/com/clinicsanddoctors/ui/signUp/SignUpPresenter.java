package com.clinicsanddoctors.ui.signUp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.RegisterRequest;
import com.clinicsanddoctors.data.remote.respons.RegisterResponse;
import com.clinicsanddoctors.utils.BitmapUtils;
import com.clinicsanddoctors.utils.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpPresenter implements SignUpContract.Presenter {
    private Context mContext;
    private SignUpContract.View mView;
    final static String HEADER_PHOTO_BASE_64 = "data:image/jpeg;base64,";
    private Uri mSelectedImageUri;
    private Uri mOutputFileUri;
    protected String mImageBase64 = "";

    public SignUpPresenter(SignUpContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void validateData(String mobile, String email, String completeName, String password, String confirmPassword) {

        if (completeName.isEmpty() || mobile.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            mView.showErrorAlert(mContext.getString(R.string.complete_all_fields));
            return;
        }

        if (email != null && !email.isEmpty() && !isValidEmail(email)) {
            mView.showErrorAlert(mContext.getString(R.string.error_invalid_email));
            return;
        }

        if (password.length() < 5) {
            mView.showErrorAlert(mContext.getString(R.string.error_password_length));
            return;
        }

        if (!password.equalsIgnoreCase(confirmPassword)) {
            mView.showErrorAlert(mContext.getString(R.string.password_not_match));
            return;
        }
        signUp(mobile, completeName, email, password);
    }

    @Override
    public void takePicture() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + mContext.getString(R.string.app_name) + File.separator);
        if (!(root.mkdirs() || root.isDirectory())) {
            mView.showErrorAlert("Can't save photo");
            return;
        }
        final String fname = "TMP_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        mOutputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = mContext.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputFileUri);
            cameraIntents.add(intent);
        }
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        mView.promptUserForPhoto(chooserIntent, PICTURE_REQUEST_CODE);
    }

    @Override
    public void attendOnClickPhoto(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            boolean isCamera;
            if (data == null || data.getData() == null) {
                isCamera = true;
            } else {
                final String action = data.getAction();
                isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
            }
            if (isCamera) {
                mSelectedImageUri = mOutputFileUri;
            } else {
                mSelectedImageUri = data.getData();
            }
            mView.showProfilePhoto(mSelectedImageUri);

            getBitmap(mSelectedImageUri.toString())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(this::toBase64)
                    .subscribe(this::base);
        }
    }

    public void base(String imageBase64) {
        mImageBase64 = imageBase64;
    }

    private Observable<Bitmap> getBitmap(String url) {
        return Observable.from(Glide.with(mContext).load(url)
                .asBitmap()
                .into(-1, -1))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public String toBase64(Bitmap image) {
        image = BitmapUtils.getResizedBitmap(image);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, Constants.PHOTO_QUALITY, output);
        byte[] bytes = output.toByteArray();
        return HEADER_PHOTO_BASE_64 + Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void signUp(String mobile, String completeName, String email, String password) {

        mView.showProgressDialog();
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword(password)
                .setEmail(email)
                .setFull_name(completeName)
                .setMobile_phone(mobile)
                .setProfile_picture(mImageBase64);

        ClinicServices.getServiceClient().register(registerRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(respose -> onSuccess(respose, password), this::onError);
    }

    private void onSuccess(RegisterResponse registerResponse, String password) {

        UserClient userClient = new UserClient(registerResponse);
        userClient.setPassword(password);
        AppPreference.setUser(mContext, userClient);

        mView.hideProgressDialog();
        mView.onSuccessSignUp();

    }

    private void onError(Throwable throwable) {
        mView.hideProgressDialog();
        if (throwable != null) {
            if (throwable instanceof HttpException)
                mView.showErrorAlert(ClinicServices.attendError((HttpException) throwable).getError());
            else if (throwable instanceof IOException)
                mView.showErrorAlert(mContext.getString(R.string.error_internet));
            else
                mView.showErrorAlert(mContext.getString(R.string.error_generic));
        }
    }

    private boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
