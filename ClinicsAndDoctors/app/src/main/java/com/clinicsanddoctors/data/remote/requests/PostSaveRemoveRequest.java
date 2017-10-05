package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 14/09/2017.
 */

public class PostSaveRemoveRequest {

    private String post_id;
    private String service_provider_id;

    public PostSaveRemoveRequest(String post_id, String service_provider_id) {
        this.post_id = post_id;
        this.service_provider_id = service_provider_id;
    }
}
