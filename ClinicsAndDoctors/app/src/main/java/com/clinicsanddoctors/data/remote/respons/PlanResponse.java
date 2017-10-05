package com.clinicsanddoctors.data.remote.respons;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Daro on 14/09/2017.
 */

public class PlanResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("available_jobs")
    @Expose
    private String available_jobs;

    @SerializedName("amount")
    @Expose
    private String amount;

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getAvailable_jobs() {
        return available_jobs;
    }

    public String getAmount() {
        return amount;
    }
}
