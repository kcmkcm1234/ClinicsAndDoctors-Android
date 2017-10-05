package com.clinicsanddoctors.data.remote.requests;

import com.google.gson.annotations.Expose;

public class RegisterFacebookRequest {
    @Expose
    String fb_id;
    @Expose
    String fb_social_token;

    public RegisterFacebookRequest(String fb_id, String fb_social_token) {
        this.fb_id = fb_id;
        this.fb_social_token = fb_social_token;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getFb_social_token() {
        return fb_social_token;
    }

    public void setFb_social_token(String fb_social_token) {
        this.fb_social_token = fb_social_token;
    }
}
