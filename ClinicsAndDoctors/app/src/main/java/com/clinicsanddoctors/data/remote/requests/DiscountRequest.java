package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 14/09/2017.
 */

public class DiscountRequest {

    private String post_id;
    private String service_provider_id;

    public DiscountRequest(String service_provider_id, String post_id) {
        this.service_provider_id = service_provider_id;
        this.post_id = post_id;
    }
}
