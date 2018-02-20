package com.uniware;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.DataCollector;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PaymentMethodNonce;

import android.app.Activity;

public class BraintreePlugin extends CordovaPlugin implements PaymentMethodNonceCreatedListener {
	private CallbackContext _callbackContext;
	private Activity _activity = null;
	private BraintreeFragment _braintreeFragment = null;

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		_callbackContext = callbackContext;
		_activity = this.cordova.getActivity();

		if (action.equals("initWithSettings")) {
			this.init(args);
		} else if (action.equals("paypalProcessImmediate")) {
			new PayPalProcessor(_braintreeFragment).processImmediate(args);
		} else if (action.equals("paypalProcessVaulted")) {
			new PayPalProcessor(_braintreeFragment).processVaulted(args);
		} else {
			return false;
		}

		return true;
	}

	private void init(JSONArray args) {
		try {
			startService(args.getString(0));
			_callbackContext.success();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void startService(String clientToken) {
		try {
			if (_braintreeFragment != null) {
				_braintreeFragment.removeListener(this);
			}

			_braintreeFragment = BraintreeFragment.newInstance(_activity, clientToken);
			_braintreeFragment.addListener(this);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
		try {
			PayPalAccountNonce pp = (PayPalAccountNonce) paymentMethodNonce;

			JSONObject json = new JSONObject();

			json.put("nonce", paymentMethodNonce.getNonce().toString());
			json.put("payerId", pp.getPayerId().toString());
			json.put("payerId", pp.getFirstName().toString());
			json.put("payerId", pp.getLastName().toString());
			json.put("deviceData", DataCollector.collectDeviceData(_braintreeFragment));

			PluginResult result = new PluginResult(PluginResult.Status.OK, json);

			_callbackContext.sendPluginResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
