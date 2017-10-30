package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 07/08/2017.
 */

public class ForgotPasswordRequest {

    private String phone_number;

    public ForgotPasswordRequest(String phone_number) {
        this.phone_number = phone_number;
    }
}
