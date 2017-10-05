package com.clinicsanddoctors.data.remote.requests;

import com.google.gson.annotations.Expose;

/**
 * Created by Daro on 01/08/2017.
 */

public class LoginRequest {
    @Expose
    String email;
    @Expose
    String password;

    public LoginRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public LoginRequest setPassword(String password) {
        this.password = password;
        return this;
    }

}
