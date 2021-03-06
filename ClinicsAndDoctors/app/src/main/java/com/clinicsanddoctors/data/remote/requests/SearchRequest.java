package com.clinicsanddoctors.data.remote.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by evaristo on 11/30/17.
 */

public class SearchRequest {

    @Expose String keyword;
    @Expose
    @SerializedName("user_id")
    Integer userId;
    @Expose
    @SerializedName("clinic_id")
    Integer clinic_id;

    public SearchRequest(String keyword, int userId) {
        this.keyword = keyword;
        this.userId = userId;
    }

    public SearchRequest(String keyword) {
        this.keyword = keyword;
    }

    public SearchRequest setClinicId(Integer clinic_id) {
        this.clinic_id = clinic_id;
        return this;
    }
}
