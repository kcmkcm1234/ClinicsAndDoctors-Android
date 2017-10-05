package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 09/08/2017.
 */

public class RateRequest {
    private String service_provider_id;
    private String value;

    public RateRequest(String service_provider_id, String value) {
        this.service_provider_id = service_provider_id;
        this.value = value;
    }
}
