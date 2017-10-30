
package com.clinicsanddoctors.data.remote.respons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("id")
    @Expose
    private Integer userId;
    @SerializedName("fb_id")
    @Expose
    private String fb_id;
    @SerializedName("full_name")
    @Expose
    private String full_name;
    @SerializedName("phone_number")
    @Expose
    private String mobileNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("picture")
    @Expose
    private String profilePicture;

    @SerializedName("user_role")
    @Expose
    private String user_role;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("category_id")
    @Expose
    private String category_id;
    @SerializedName("jobs_count")
    @Expose
    private int jobs_count;
    @SerializedName("plan_id")
    @Expose
    private int plan_id;


    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("code")
    @Expose
    private String code;

    public String getZipcode() {
        return zipcode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFb_id() {
        return fb_id;
    }

    public String getFullname() {
        return full_name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getDetail() {
        return detail;
    }

    public String getCode() {
        return code;
    }

    public String getUser_role() {
        return user_role;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCategory_id() {
        return category_id;
    }

    public int getJobs_count() {
        return jobs_count;
    }

    public int getPlan_id() {
        return plan_id;
    }
}
