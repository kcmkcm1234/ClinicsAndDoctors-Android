package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 08/08/2017.
 */

public class LogoutRequest {

    private String access_token;
    private String user_id;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
