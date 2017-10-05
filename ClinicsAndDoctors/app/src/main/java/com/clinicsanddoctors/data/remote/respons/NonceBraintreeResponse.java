package com.clinicsanddoctors.data.remote.respons;

/**
 * Created by Daro on 22/03/2017.
 */
public class NonceBraintreeResponse {

    String detail;
    String transaction_id;
    String transaction_type;
    String transaction_status;

    public String getDetail() {
        return detail;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public String getTransaction_status() {
        return transaction_status;
    }
}
