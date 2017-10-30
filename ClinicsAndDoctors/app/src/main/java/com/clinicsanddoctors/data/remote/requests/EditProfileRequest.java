package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 16/08/2017.
 */

public class EditProfileRequest {
    private String password;
    private String picture;
    private String user_id;
    private String full_name;
    private String phone_number;
    private String email;

    public EditProfileRequest setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public EditProfileRequest setFull_name(String full_name) {
        this.full_name = full_name;
        return this;
    }

    public EditProfileRequest setProfile_picture(String profile_picture) {
        this.picture = profile_picture;
        return this;
    }

    public EditProfileRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public EditProfileRequest setMobile(String mobile) {
        this.phone_number = mobile;
        return this;
    }

    public EditProfileRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPicture() {
        return picture;
    }
}
