package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 07/08/2017.
 */

public class ForgotPasswordRequest {

    private String email;

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    public ForgotPasswordRequest setEmail(String email) {
        this.email = email;
        return this;
    }
}
