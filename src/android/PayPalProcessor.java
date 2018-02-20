package com.uniware;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.models.PayPalRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class PayPalProcessor {

    private final BraintreeFragment _braintreeFragment;

    public PayPalProcessor(BraintreeFragment bf) {
        _braintreeFragment = bf;
    }

    public void processImmediate(JSONArray args) {
        try {
            PayPalRequest ppReq = new PayPalRequest(args.getString(0));
            ppReq.currencyCode(args.getString(1));

            PayPal.requestOneTimePayment(_braintreeFragment, ppReq);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processVaulted(JSONArray args) {
        try {
            PayPal.authorizeAccount(_braintreeFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
