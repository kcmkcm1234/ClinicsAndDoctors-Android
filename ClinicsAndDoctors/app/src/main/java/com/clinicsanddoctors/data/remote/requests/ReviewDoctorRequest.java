package com.clinicsanddoctors.data.remote.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by evaristo on 11/13/17.
 */

public class ReviewDoctorRequest {

    @Expose
    @SerializedName("doctor_id")
    int doctorId;

    public ReviewDoctorRequest(int doctorId) {
        this.doctorId = doctorId;
    }
}
