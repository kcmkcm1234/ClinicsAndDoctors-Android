package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 01/08/2017.
 */

public class ServiceProviderRequest {
    String latitude;
    String longitude;
    String radius;
    String category_id;

    public ServiceProviderRequest setCategoryId(String category_id) {
        this.category_id = category_id;
        return this;
    }

    public ServiceProviderRequest setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public ServiceProviderRequest setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public ServiceProviderRequest setRadius(String radius) {
        this.radius = radius;
        return this;
    }
}
