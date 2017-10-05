package com.clinicsanddoctors.data.entity;


import com.clinicsanddoctors.data.remote.respons.AdvertisingResponse;

public class AdvertisingBanner {
    protected String id;
    protected String name;
    protected String banner;
    protected String startDate;
    protected String endDate;
    protected String status;
    protected String link;
    protected String type;

    public AdvertisingBanner(AdvertisingResponse advertisingResponse) {
        id = advertisingResponse.getAdvertising_banner_id();
        name = advertisingResponse.getName();
        banner = advertisingResponse.getBanner();
        startDate = advertisingResponse.getStart_date();
        endDate = advertisingResponse.getEnd_date();
        status = advertisingResponse.getStatus();
        link = advertisingResponse.getLink();
        type = advertisingResponse.getType();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBanner() {
        return banner;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getLink() {
        return link;
    }

    public String getType() {
        return type;
    }
}
