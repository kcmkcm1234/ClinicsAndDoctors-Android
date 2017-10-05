package com.clinicsanddoctors.data.entity;


import com.clinicsanddoctors.data.remote.respons.AdvertisingResponse;

/**
 * Created by Daro on 10/08/2017.
 */

public class AdvertisingPopOver extends AdvertisingBanner {
    private double latitude;
    private double longitude;
    private String image;

    public AdvertisingPopOver(AdvertisingResponse advertisingResponse) {
        super(advertisingResponse);
        latitude = advertisingResponse.getLatitude();
        longitude = advertisingResponse.getLongitude();
        image = advertisingResponse.getImage();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImage() {
        return image;
    }
}
