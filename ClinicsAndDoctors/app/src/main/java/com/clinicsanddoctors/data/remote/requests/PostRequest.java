package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 10/08/2017.
 */

public class PostRequest {

    private String post_id;
    private String user_id;
    private String title;
    private String description;
    private String picture;
    private String from;
    private String to;
    private String category_id;
    private String status;
    private double latitude;
    private double longitude;

    public PostRequest setPost_id(String post_id) {
        this.post_id = post_id;
        return this;
    }

    public PostRequest setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public PostRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public PostRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public PostRequest setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public PostRequest setFrom(String from) {
        this.from = from;
        return this;
    }

    public PostRequest setTo(String to) {
        this.to = to;
        return this;
    }

    public PostRequest setCategory_id(String category_id) {
        this.category_id = category_id;
        return this;
    }

    public PostRequest setStatus(String status) {
        this.status = status;
        return this;
    }

    public PostRequest setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public PostRequest setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }
}
