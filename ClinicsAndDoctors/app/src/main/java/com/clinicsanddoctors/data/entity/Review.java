package com.clinicsanddoctors.data.entity;

import com.clinicsanddoctors.data.remote.respons.UserPostResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daro on 20/10/2017.
 */

@Parcel
public class Review {
    @Expose
    private String id;
    @Expose
    private String comment;
    @SerializedName("created_date_time")
    @Expose
    private String createdDateTime;
    @SerializedName("updated_date_time")
    @Expose
    private String updatedDateTime;
    @SerializedName("user")
    @Expose
    private UserPostResponse userClient;
    @Expose
    private int rating;

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public UserPostResponse getUserClient() {
        return userClient;
    }

    public String getCreatedDateTime() {
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            date = simpleDateFormat.parse(createdDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        String dateString = simpleDateFormat.format(date);

        return dateString;
    }

    public String getUpdatedDateTime() {
        return updatedDateTime;
    }

    public int getRating() {
        return rating;
    }
}
