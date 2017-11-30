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
    int userId;

    public SearchRequest(String keyword, int userId) {
        this.keyword = keyword;
        this.userId = userId;
    }
}
