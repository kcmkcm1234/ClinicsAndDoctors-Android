package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 10/08/2017.
 */

public class AdvertisingRequest {
    private String latitude;
    private String longitude;

    public AdvertisingRequest setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public AdvertisingRequest setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }
}
