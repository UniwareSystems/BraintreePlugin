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
import com.braintreepayments.api.models.CardNonce;
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

		if (action.equals("dropInUI")) {
			startDropIn();
		} else if (action.equals("initWithSettings")) {
			this.init(args);
		} else if (action.equals("paypalProcessImmediate")) {
			new PayPalProcessor(_braintreeFragment).processImmediate(args);
		} else if (action.equals("paypalProcessVaulted")) {
			new PayPalProcessor(_braintreeFragment).processVaulted(args);
		} else if (action.equals("cardTokenise")) {
			new CardProcessor(_braintreeFragment).tokeniseCard(args.getString(0), args.getString(1), args.getString(2));
		} else {
			return false;
		}
		
		return true;
	}

	private void startDropIn() {
		cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(this.activity, BraintreePaymentActivity.class);
				intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, clientToken);
				this.cordova.startActivityForResult(this, intent, DROP_IN_REQUEST);
            }
        });
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
			JSONObject json = new JSONObject();

			json.put("nonce", paymentMethodNonce.getNonce().toString());
			json.put("deviceData", DataCollector.collectDeviceData(_braintreeFragment));

			if (paymentMethodNonce instanceof PayPalAccountNonce) {
				PayPalAccountNonce pp = (PayPalAccountNonce) paymentMethodNonce;
				json.put("payerId", pp.getPayerId().toString());
				json.put("forename", pp.getFirstName().toString());
				json.put("surname", pp.getLastName().toString());
			} else if (paymentMethodNonce instanceof CardNonce) {
				CardNonce c = (CardNonce) paymentMethodNonce;
				json.put("lastFour", c.getLastFour().toString());
				json.put("cardType", c.getCardType().toString());
			}

			_callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, json));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
