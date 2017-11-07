package com.clinicsanddoctors.ui.main.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.entity.Clinic;
import com.clinicsanddoctors.data.entity.ClinicAndDoctor;
import com.clinicsanddoctors.data.local.AppPreference;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.requests.ClinicsRequest;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Daro on 27/07/2017.
 */

public class MapPresenter implements MapContract.Presenter {

    private MapContract.View mView;
    private Context mContext;
    private CompositeSubscription mCompositeSubscriptions;

    //delete this line
    //private List<Clinic> clinicList;

    public MapPresenter(MapContract.View view, Context context) {
        mView = view;
        mContext = context;
        mCompositeSubscriptions = new CompositeSubscription();
    }


    private Clinic getNewClinic(ClinicAndDoctorResponse storeResponse, Category category) {
        return new Clinic(storeResponse, category);
    }

    private void onSuccess(List<Clinic> clinics) {
        mView.hideProgressDialog();
        mView.showClinics(clinics);
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

    @Override
    public void addSubscription(Subscription subscription) {
        mCompositeSubscriptions.add(subscription);
    }

    @Override
    public Observable<Bitmap> getCustomMarker(View marker, ClinicAndDoctor clinicAndDoctor) {

        int idDrawablePin = R.drawable.ic_pin_clinic;
        Drawable drawablePin = mContext.getResources().getDrawable(idDrawablePin);
        Observable<Bitmap> pinBackgroundCar = getBitmapColorFromDrawable(marker,
                drawablePin, Color.parseColor("#C32254"), clinicAndDoctor);
        return pinBackgroundCar;
    }

    @Override
    public void getClinics(Category category, Location location, int radius, boolean isFromTab) {
        if (isFromTab)
            mView.showProgressDialog();

        if (location.getLongitude() == 0 || location.getLatitude() == 0) {
            mView.hideProgressDialog();
            return;
        }

        ClinicsRequest clinicsRequest = new ClinicsRequest();
        clinicsRequest
                .setLatitude("" + location.getLatitude())
                .setLongitude("" + location.getLongitude())
                .setRadius("" + radius);

        if (AppPreference.getUser(mContext) != null)
            clinicsRequest.setUser_id("" + AppPreference.getUser(mContext).getId());
        else
            clinicsRequest.setUser_id("0");

        if (!category.getId().equalsIgnoreCase("0"))
            clinicsRequest.setCategoryId(category.getId());

        ClinicServices.getServiceClient().getClinics(clinicsRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(equineResponse -> equineResponse)
                .map(obj -> getNewClinic(obj, category))
                .toList()
                .subscribe(this::onSuccess, this::onError);
    }

    private Observable<Bitmap> getBitmapColorFromDrawable(View marker, Drawable drawable, int color, ClinicAndDoctor clinicAndDoctor) {
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {

                    if (clinicAndDoctor.getPicture() != null && !clinicAndDoctor.getPicture().isEmpty()) {
                        Observable<Bitmap> providerFlow = chargePhotoProfileMarker(clinicAndDoctor.getPicture());
                        providerFlow.subscribe(bitmap -> {
                            ((CircleImageView) marker.findViewById(R.id.mPin)).setImageBitmap(bitmap);
                            marker.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                            marker.measure(RelativeLayout.MeasureSpec.makeMeasureSpec(0, RelativeLayout.MeasureSpec.UNSPECIFIED), RelativeLayout.MeasureSpec.makeMeasureSpec(0, RelativeLayout.MeasureSpec.UNSPECIFIED));
                            marker.layout(0, 0, marker.getMeasuredWidth(), marker.getMeasuredHeight());
                            marker.buildDrawingCache();
                            Bitmap returnedBitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(),
                                    Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(returnedBitmap);
                            Drawable drawable = marker.getBackground();
                            if (drawable != null)
                                drawable.draw(canvas);
                            marker.draw(canvas);

                            subscriber.onNext(returnedBitmap);
                            subscriber.onCompleted();
                        });
                    } else {
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        subscriber.onNext(bitmap);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private Observable<Bitmap> chargePhotoProfileMarker(String url) {
        return Observable.from(Glide.with(mContext).load(url)
                .asBitmap()
                .thumbnail(0.1f)
                .into(-1, -1))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(throwable -> onReturnError());
    }

    private Bitmap onReturnError() {
        Drawable d = mContext.getResources().getDrawable(R.drawable.ic_pin_clinic);
        return ((BitmapDrawable) d).getBitmap();
    }


    private LatLng getRandomLocation(LatLng point) {

        int radius = 1000;
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for (int i = 0; i < 10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;
            Random random = new Random();
            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));
        }
        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);
    }
}
