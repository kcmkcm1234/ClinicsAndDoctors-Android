package com.clinicsanddoctors.ui.main.list;

import android.content.Context;

import com.clinicsanddoctors.R;
import com.clinicsanddoctors.data.entity.AdvertisingBanner;
import com.clinicsanddoctors.data.entity.Category;
import com.clinicsanddoctors.data.remote.ClinicServices;
import com.clinicsanddoctors.data.remote.respons.AdvertisingResponse;
import com.clinicsanddoctors.data.remote.respons.CategoryResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Daro on 09/08/2017.
 */

public class ListPresenter implements ListContract.Presenter {

    private ListContract.View mView;
    private Context mContext;
    private Timer mTimerBanner;
    private int position = 0;

    public ListPresenter(ListContract.View view, Context context) {
        mView = view;
        mContext = context;
    }

    @Override
    public void getCategory() {
        mView.showProgressDialog();
        ClinicServices.getServiceClient().getCategories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(categoryResponse -> categoryResponse)
                .map(this::getCategory)
                .toList()
                .subscribe(this::onSuccess, this::onError);
    }

    @Override
    public void getAdsBanner() {
        ClinicServices.getServiceClient().getAdvertisingBanner()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapIterable(adsResponse -> adsResponse)
                .map(this::getAdvertising)
                .toList()
                .subscribe(this::onSuccessBanner, this::onErrorBanner);
    }

    private void onErrorBanner(Throwable throwable) {
    }

    private void onSuccessBanner(List<AdvertisingBanner> advertisingBanners) {
        if (advertisingBanners.isEmpty()) {
            return;
        }
        mTimerBanner = new Timer();
        mTimerBanner.schedule(new TimerTask() {
            @Override
            public void run() {
                if (position >= advertisingBanners.size())
                    position = 0;
                mView.showAdvertising(advertisingBanners.get(position));
                position++;
            }
        }, 1, 10000);

    }

    private List<AdvertisingBanner> filterAds(List<AdvertisingBanner> advertisingBanners) {

        if (advertisingBanners != null && !advertisingBanners.isEmpty()) {
            List<AdvertisingBanner> list = new ArrayList<>();
            for (AdvertisingBanner advertisingBanner : advertisingBanners)
                list.add(advertisingBanner);
            return list;
        }
        return null;
    }

    private AdvertisingBanner getAdvertising(AdvertisingResponse advertisingResponse) {
        return new AdvertisingBanner(advertisingResponse);
    }

    private void onSuccess(List<Category> categories) {
        mView.hideProgressDialog();
        mView.showCategory(categories);
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

    private Category getCategory(CategoryResponse categoryResponse) {
        return new Category(categoryResponse);
    }

    public void cancelTimer() {
        if (mTimerBanner != null) {
            mTimerBanner.cancel();
            mTimerBanner = null;
        }
    }
}
