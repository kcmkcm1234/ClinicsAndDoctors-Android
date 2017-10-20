package com.clinicsanddoctors.data.entity;

/**
 * Created by Daro on 20/10/2017.
 */

public class Review {
    private String id;
    private String description;
    private UserClient userClient;
    private String date;

    public String getId() {
        return id;
    }

    public Review setId(String id) {
        this.id = id;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Review setDescription(String description) {
        this.description = description;
        return this;
    }

    public UserClient getUserClient() {
        return userClient;
    }

    public Review setUserClient(UserClient userClient) {
        this.userClient = userClient;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Review setDate(String date) {
        this.date = date;
        return this;
    }
}
