package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 01/08/2017.
 */

public class DoctorsRequest {
    String latitude;
    String longitude;
    String radius;
    String specialty_id;
    String user_id;
    String clinic_id;

    public DoctorsRequest setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public DoctorsRequest setCategoryId(String specialty_id) {
        this.specialty_id = specialty_id;
        return this;
    }

    public DoctorsRequest setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public DoctorsRequest setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public DoctorsRequest setRadius(String radius) {
        this.radius = radius;
        return this;
    }

    public void setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
    }
}
