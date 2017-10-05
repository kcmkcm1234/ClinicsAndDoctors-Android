package com.clinicsanddoctors.data.remote.requests;

public class UserAndNonceRequest {
    private int service_provider_id;
    private String nonce;
    private String amount;

    public UserAndNonceRequest(int user_id, String nonce, String amount) {
        this.service_provider_id = user_id;
        this.nonce = nonce;
        this.amount = amount;
    }


}
