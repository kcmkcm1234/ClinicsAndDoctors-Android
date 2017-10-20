package com.clinicsanddoctors.data.remote;

import com.clinicsanddoctors.data.entity.ErrorApi;
import com.clinicsanddoctors.data.entity.SettingDescription;
import com.clinicsanddoctors.data.remote.requests.AdvertisingRequest;
import com.clinicsanddoctors.data.remote.requests.DeleteRequest;
import com.clinicsanddoctors.data.remote.requests.EditProfileRequest;
import com.clinicsanddoctors.data.remote.requests.ForgotPasswordRequest;
import com.clinicsanddoctors.data.remote.requests.LoginRequest;
import com.clinicsanddoctors.data.remote.requests.LogoutRequest;
import com.clinicsanddoctors.data.remote.requests.MyRequestServiceRequest;
import com.clinicsanddoctors.data.remote.requests.PlanRequest;
import com.clinicsanddoctors.data.remote.requests.PostRequest;
import com.clinicsanddoctors.data.remote.requests.PostSaveRemoveRequest;
import com.clinicsanddoctors.data.remote.requests.RateRequest;
import com.clinicsanddoctors.data.remote.requests.RegisterFacebookRequest;
import com.clinicsanddoctors.data.remote.requests.RegisterRequest;
import com.clinicsanddoctors.data.remote.requests.ReportRequest;
import com.clinicsanddoctors.data.remote.requests.ServiceProviderIdRequest;
import com.clinicsanddoctors.data.remote.requests.ClinicsRequest;
import com.clinicsanddoctors.data.remote.respons.AdvertisingResponse;
import com.clinicsanddoctors.data.remote.respons.CategoryResponse;
import com.clinicsanddoctors.data.remote.respons.PlanResponse;
import com.clinicsanddoctors.data.remote.respons.PostResponse;
import com.clinicsanddoctors.data.remote.respons.RegisterResponse;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;
import rx.exceptions.CompositeException;
import rx.exceptions.OnErrorFailedException;

/**
 * Created by Daro on 07/07/2017.
 */

public class ClinicServices {
    public static String API_URL = "http://clinic.xanthops.com/api/v1/";
    private static ApiClientInterface sApiClient;
    private static Retrofit sRestAdapter;

    public static ApiClientInterface getServiceClient() {
        if (sApiClient == null) {
            initApiClient();
        }
        return sApiClient;
    }

    private static void initApiClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(5, TimeUnit.MINUTES);
        httpClient.connectTimeout(5, TimeUnit.MINUTES);
        httpClient.writeTimeout(5, TimeUnit.MINUTES);
        httpClient.addInterceptor(interceptor);

        sRestAdapter = new Retrofit.Builder()
                .baseUrl(API_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        sApiClient = sRestAdapter.create(ApiClientInterface.class);
    }


    public interface RadiusSearch {
        int RADIUS = 100;
        int INTERVAL = 1;
    }

    public interface ApiError {
        String getError();

        String getCode();
    }

    public static ApiError attendError(HttpException error) {
        try {
            if (error.code() != 404)
                return new Gson().fromJson(error.response().errorBody().string(), ErrorApi.class);
            else
                return new ErrorApi("Something was wrong", "418");
        } catch (IOException e) {
            return new ErrorApi("Something was wrong", "418");
        } catch (OnErrorFailedException e) {
            return new ErrorApi("Something was wrong", "418");
        } catch (CompositeException e) {
            return new ErrorApi("Something was wrong", "418");
        } catch (JsonParseException e) {
            return new ErrorApi("Something was wrong", "418");
        }
    }

    public interface ApiClientInterface {

        @POST("get_clincs")
        Observable<List<ClinicAndDoctorResponse>> getClinics(@Body ClinicsRequest getStoreRequest);

        @POST("get_doctors")
        Observable<List<ClinicAndDoctorResponse>> getDoctors(@Body ClinicsRequest getStoreRequest);

        @GET("get_specialties")
        Observable<List<CategoryResponse>> getCategories();

        @GET("get_terms_of_use")
        Observable<SettingDescription> getTermsOfUse();

        @GET("get_privacy_policy")
        Observable<SettingDescription> getPrivacyPolicy();

        @POST("send_rating")
        Observable<JSONObject> sendRate(@Body RateRequest rateRequest);

        @POST("send_report")
        Observable<JSONObject> sendReport(@Body ReportRequest reportRequest);

        @GET("get_advertising_banner")
        Observable<List<AdvertisingResponse>> getAdvertisingBanner();

        @POST("get_advertising_popover")
        Observable<List<AdvertisingResponse>> getAdvertisingPopOver(@Body AdvertisingRequest advertisingRequest);

        @POST("register")
        Observable<RegisterResponse> register(@Body RegisterRequest registerFacebookRequest);

        @POST("register_with_fb")
        Observable<RegisterResponse> registerFacebook(@Body RegisterFacebookRequest registerFacebookRequest);

        @POST("login")
        Observable<RegisterResponse> login(@Body LoginRequest loginRequest);

        @POST("forgot_password")
        Observable<JSONObject> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

        @POST("logout")
        Observable<JSONObject> logout(@Body LogoutRequest logoutRequest);

        @POST("edit_profile")
        Observable<EditProfileRequest> editProfile(@Body EditProfileRequest editProfileRequest);

        @POST("get_post")
        Observable<List<PostResponse>> getPosts(@Body MyRequestServiceRequest serviceRequest);

        @POST("create_edit_post")
        Observable<JSONObject> createEditPost(@Body PostRequest postRequest);

        @GET("get_plan")
        Observable<List<PlanResponse>> getPlan();

        @POST("set_plan")
        Observable<JSONObject> setPlan(@Body PlanRequest planRequest);

        @POST("delete_post")
        Observable<JSONObject> deletePost(@Body DeleteRequest deleteRequest);

        @POST("save_post")
        Observable<JSONObject> savePost(@Body PostSaveRemoveRequest postSaveRemoveRequest);

        @POST("remove_post")
        Observable<JSONObject> removePost(@Body PostSaveRemoveRequest postSaveRemoveRequest);

        @POST("get_saved_post")
        Observable<List<PostResponse>> getSavePost(@Body ServiceProviderIdRequest serviceProviderIdRequest);

    }

}
