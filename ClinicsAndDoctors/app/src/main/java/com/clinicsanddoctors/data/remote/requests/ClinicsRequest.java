package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 01/08/2017.
 */

public class ClinicsRequest {
    String latitude;
    String longitude;
    String radius;
    String category_id;

    public ClinicsRequest setCategoryId(String category_id) {
        this.category_id = category_id;
        return this;
    }

    public ClinicsRequest setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public ClinicsRequest setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public ClinicsRequest setRadius(String radius) {
        this.radius = radius;
        return this;
    }
}
