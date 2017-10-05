package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 09/08/2017.
 */

public class ReportRequest {
    private String service_provider_id;
    private String description;

    public ReportRequest(String service_provider_id, String description) {
        this.service_provider_id = service_provider_id;
        this.description = description;
    }
}
