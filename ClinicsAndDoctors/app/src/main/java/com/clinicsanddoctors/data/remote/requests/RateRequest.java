package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 09/08/2017.
 */

public class RateRequest {

    private String user_id;
    private String clinic_id;
    private String doctor_id;
    private int value;
    private String comment;
    private String reason;

    public RateRequest setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public RateRequest setClinic_id(String clinic_id) {
        this.clinic_id = clinic_id;
        return this;
    }

    public RateRequest setDoctor_id(String doctor_id) {
        this.doctor_id = doctor_id;
        return this;
    }

    public RateRequest setValue(int value) {
        this.value = value;
        return this;
    }

    public RateRequest setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public RateRequest setReason(String reason) {
        this.reason = reason;
        return this;
    }
}
