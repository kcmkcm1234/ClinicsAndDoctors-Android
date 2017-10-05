package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 14/09/2017.
 */

public class PlanRequest {
    private String service_provider_id;
    private String plan_id;

    public PlanRequest(String service_provider_id, String plan_id) {
        this.service_provider_id = service_provider_id;
        this.plan_id = plan_id;
    }
}
