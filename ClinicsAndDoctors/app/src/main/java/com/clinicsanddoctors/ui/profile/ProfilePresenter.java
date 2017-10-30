package com.clinicsanddoctors.ui.profile;

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
import com.clinicsanddoctors.data.remote.requests.EditProfileRequest;
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

public class ProfilePresenter implements ProfileContract.Presenter {

    private ProfileContract.View mView;
    private Context mContext;

    final static String HEADER_PHOTO_BASE_64 = "data:image/jpeg;base64,";
    private Uri mSelectedImageUri;
    private Uri mOutputFileUri;
    protected String mImageBase64 = "";

    public ProfilePresenter(ProfileContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getProfile() {
        UserClient userClient = AppPreference.getUser(mContext);
        //api call
        mView.showProgressDialog();

        mView.hideProgressDialog();
        mView.showProfile(userClient);
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

    @Override
    public void editProfile(String fullName, String mobile, String email, String password, String confirmPassword) {

        if (fullName.isEmpty()) {
            mView.showErrorAlert("You must write your name");
            return;
        }

        if (mobile.trim().isEmpty()) {
            mView.showErrorAlert("You must enter a mobile number");
            return;
        }

        EditProfileRequest editProfileRequest = null;
        UserClient userClient = AppPreference.getUser(mContext);
        if (!userClient.isFacebookUer() && !confirmPassword.isEmpty()) {
            if (password.length() < 5) {
                mView.showErrorAlert(mContext.getString(R.string.error_password_length));
                return;
            }

            if (!password.equalsIgnoreCase(confirmPassword)) {
                mView.showErrorAlert(mContext.getString(R.string.password_not_match));
                return;
            }

            if (!isValidEmail(email)) {
                mView.showErrorAlert(mContext.getString(R.string.error_invalid_email));
                return;
            }

            editProfileRequest = new EditProfileRequest();
            editProfileRequest.setPassword(password);
        }
        if (editProfileRequest == null)
            editProfileRequest = new EditProfileRequest();

        editProfileRequest.setEmail(email);
        editProfileRequest.setMobile(mobile);
        editProfileRequest.setFull_name(fullName);
        editProfileRequest.setUser_id("" + AppPreference.getUser(mContext).getId());

        if (mImageBase64 != null && !mImageBase64.isEmpty())
            editProfileRequest.setProfile_picture(mImageBase64);

        mView.showProgressDialog();
        ClinicServices.getServiceClient().editProfile(editProfileRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onSuccessEdit(response, fullName, password, mobile, email), this::onError);
    }

    private void onSuccessEdit(EditProfileRequest editProfileRequest, String fullName, String password, String mobile, String email) {
        mView.hideProgressDialog();
        UserClient userClient = AppPreference.getUser(mContext);
        userClient.setPassword(password);
        userClient.setEmail(email);
        userClient.setMobile(mobile);
        userClient.setFullName(fullName);
        userClient.setUrl(editProfileRequest.getPicture());
        /*
        if (userClient.getUrl() == null
                || userClient.getUrl().isEmpty()
                || !userClient.getUrl().contains("" + userClient.getId())) {
            String urlImage = "http://tattoou.xanthops.com//uploads/user_pictures/XX/XX.jpg";
            urlImage = urlImage.replaceAll("XX", "" + userClient.getId());
            userClient.setUrl(urlImage);
        }
        */

        AppPreference.setUser(mContext, userClient);
        mView.onEditSuccess();
    }

    private void onError(Throwable throwable) {
        mView.hideProgressDialog();
        if (throwable != null) {
            if (throwable.getMessage().equalsIgnoreCase("HTTP 404 Not Found"))
                mView.showErrorAlert(mContext.getString(R.string.error_generic));
            else {
                if (throwable instanceof HttpException)
                    mView.showErrorAlert(ClinicServices.attendError((HttpException) throwable).getError());
                else if (throwable instanceof IOException)
                    mView.showErrorAlert(mContext.getString(R.string.error_internet));
                else
                    mView.showErrorAlert(mContext.getString(R.string.error_generic));
            }
        }
    }

    private boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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
}
