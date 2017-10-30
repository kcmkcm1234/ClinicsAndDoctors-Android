package com.clinicsanddoctors.ui.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.UserClient;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.RegisterFacebookRequest;
import com.clinicsanddoctors.data.remote.respons.RegisterResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Daro on 07/07/2017.
 */

public class StartPresenter implements StartContract.Presenter {

    private StartContract.View mView;
    private Context mContext;
    private CallbackManager callbackManager;
    public final static String HEADER_PHOTO_BASE_64 = "data:image/jpeg;base64,";


    public StartPresenter(StartContract.View view, Context context) {
        mView = view;
        mContext = context;
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void attendOnClickFacebook(Activity activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile, email, user_friends"));
        FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null)
                    requestFacebookData();
            }

            @Override
            public void onCancel() {
                if (AccessToken.getCurrentAccessToken() != null)
                    requestFacebookData();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(mContext, mContext.getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
            }
        };
        LoginManager.getInstance().registerCallback(callbackManager, facebookCallback);
    }

    private void requestFacebookData() {

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                (object, response) -> {
                    mView.showProgressDialog();
                    RegisterFacebookRequest user = getUserFromFacebook(object);
                    if (user != null) {
                        ClinicServices.getServiceClient().registerFacebook(user)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(res -> onSuccess(res, user.getFb_id()), StartPresenter.this::onError);
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,name,first_name,last_name,gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void onSuccess(RegisterResponse registerResponse, String facebookId) {
        UserClient userClient = new UserClient(registerResponse);
        userClient.setFacebookId(facebookId);
        AppPreference.setUser(mContext, userClient);
        mView.hideProgressDialog();
        mView.onFacebookConnected();
    }

    private void onError(Throwable throwable) {
        mView.hideProgressDialog();
        mView.showErrorAlert(mContext.getString(R.string.error_generic));
    }

    private RegisterFacebookRequest getUserFromFacebook(JSONObject object) {
        try {
            final String userFbId = object.getString("id");
            final String userFbEmail = object.getString("email");
            final String userFbFirstName = object.getString("first_name");
            final String userFbLastName = object.getString("last_name");
            final String userFbPhoto = ImageRequest.getProfilePictureUri(object.optString("id"), 300, 300).toString();
            final String token = AccessToken.getCurrentAccessToken().getToken();

            return new RegisterFacebookRequest(userFbId, token);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void attendFacebookResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null) {
            if (resultCode == ((Activity) mContext).RESULT_OK)
                callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
}
