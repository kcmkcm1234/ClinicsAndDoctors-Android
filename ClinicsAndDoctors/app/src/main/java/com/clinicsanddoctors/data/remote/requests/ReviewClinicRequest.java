package com.clinicsanddoctors.data.remote.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by evaristo on 11/30/17.
 */

public class ReviewClinicRequest {

    @Expose
    @SerializedName("clinic_id")
    int clinicId;

    public ReviewClinicRequest(int clinicId) {
        this.clinicId = clinicId;
    }
}
