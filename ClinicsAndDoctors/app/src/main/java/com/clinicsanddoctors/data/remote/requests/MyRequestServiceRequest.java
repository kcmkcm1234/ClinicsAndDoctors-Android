package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 30/08/2017.
 */

public class MyRequestServiceRequest {
    private String user_id;
    private String status;
    private String post_id;
    private String category_id;
    private String latitude;
    private String longitude;
    private String radius;
    private String service_provider_id;

    public MyRequestServiceRequest(){

    }

    public void setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
    }

    public MyRequestServiceRequest(String user_id) {
        this.user_id = user_id;
    }

    public MyRequestServiceRequest setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public MyRequestServiceRequest setStatus(String status) {
        this.status = status;
        return this;
    }

    public MyRequestServiceRequest setPost_id(String post_id) {
        this.post_id = post_id;
        return this;
    }

    public MyRequestServiceRequest setCategory_id(String category_id) {
        this.category_id = category_id;
        return this;
    }

    public MyRequestServiceRequest setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public MyRequestServiceRequest setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public MyRequestServiceRequest setRadius(String radius) {
        this.radius = radius;
        return this;
    }
}
