package com.clinicsanddoctors.data.remote.requests;

import com.google.gson.annotations.Expose;

/**
 * Created by Daro on 01/08/2017.
 */

public class LoginRequest {
    @Expose
    String phone_number;
    @Expose
    String password;

    public LoginRequest setMobile(String phone_number) {
        this.phone_number = phone_number;
        return this;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

}
