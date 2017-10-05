package com.clinicsanddoctors.data.remote.respons;

/**
 * Created by Daro on 10/08/2017.
 */

public class AdvertisingResponse {

    private String advertising_banner_id;
    private String name;
    private String banner;
    private String image;
    private String start_date;
    private String end_date;
    private String status;
    private String link;
    private double latitude;
    private double longitude;
    private String type;

    public String getAdvertising_banner_id() {
        return advertising_banner_id;
    }

    public String getName() {
        return name;
    }

    public String getBanner() {
        return banner;
    }

    public String getImage() {
        return image;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getStatus() {
        return status;
    }

    public String getLink() {
        return link;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getType() {
        return type;
    }
}
