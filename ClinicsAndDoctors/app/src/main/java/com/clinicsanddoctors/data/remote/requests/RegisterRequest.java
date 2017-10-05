package com.clinicsanddoctors.data.remote.requests;

import com.google.gson.annotations.Expose;

/**
 * Created by Daro on 01/08/2017.
 */

public class RegisterRequest {
    @Expose
    String surname;
    @Expose
    String name;
    @Expose
    String email;
    @Expose
    String password;
    @Expose
    String phone_number;
    @Expose
    String profile_picture;

    public RegisterRequest setLast_name(String last_name) {
        this.surname = last_name;
        return this;
    }

    public RegisterRequest setFirst_name(String first_name) {
        this.name = first_name;
        return this;
    }

    public RegisterRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterRequest setMobile_phone(String mobile_phone) {
        this.phone_number = mobile_phone;
        return this;
    }

    public RegisterRequest setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
        return this;
    }

}
