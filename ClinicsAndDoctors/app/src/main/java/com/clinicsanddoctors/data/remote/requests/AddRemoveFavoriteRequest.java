package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 27/10/2017.
 */

public class AddRemoveFavoriteRequest {

    private String clinic_id;
    private String doctor_id;
    private String user_id;

    public AddRemoveFavoriteRequest setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
        return this;
    }

    public AddRemoveFavoriteRequest setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
        return this;
    }

    public AddRemoveFavoriteRequest setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }
}
