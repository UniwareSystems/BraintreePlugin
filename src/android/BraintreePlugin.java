package com.uniware;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.DataCollector;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalAccountNonce;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;

import android.app.Activity;
import android.content.Intent;

public class BraintreePlugin extends CordovaPlugin implements PaymentMethodNonceCreatedListener
{
	private CallbackContext callbackContext;
	private Activity activity = null;
	private BraintreeFragment mBraintreeFragment = null;
	protected static final int REQUEST_CODE = 100;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException
	{
		boolean retValue = true;
		
		this.callbackContext = callbackContext;
		this.activity = this.cordova.getActivity();
		
		if (action.equals("initWithSettings"))
		{
            this.init(args);
        }
		else if (action.equals("processImmediate"))
		{
        	this.processImmediate(args);
		}
		else if (action.equals("processVaulted"))
		{
			this.processVaulted(args);
		}
		else
		{
			retValue = false;
        }
		
        return retValue;
	}

	private void dismiss(JSONArray args)
	{
		
	}

	private void startService(String clientToken)
	{
		try
		{
			if (this.mBraintreeFragment != null)
			{
				this.mBraintreeFragment.removeListener(this);
			}

			this.mBraintreeFragment = BraintreeFragment.newInstance(this.activity, clientToken);
			this.mBraintreeFragment.addListener(this);
		}
		catch (InvalidArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init(JSONArray args)
	{
		try
		{
			startService(args.getString(0));
			callbackContext.success();
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processImmediate(JSONArray args)
	{
		try
		{
			PayPalRequest ppReq = new PayPalRequest(args.getString(0));
			ppReq.currencyCode(args.getString(1));

			PayPal.requestOneTimePayment(this.mBraintreeFragment, ppReq);
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void processVaulted(JSONArray args)
	{
		try
		{
			PayPal.authorizeAccount(this.mBraintreeFragment);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce)
	{
		try
		{
			PayPalAccountNonce pp = (PayPalAccountNonce) paymentMethodNonce;

			JSONObject json = new JSONObject();

			json.put("nonce", paymentMethodNonce.getNonce().toString());
			json.put("payerId", pp.getPayerId().toString());
			json.put("payerId", pp.getFirstName().toString());
			json.put("payerId", pp.getLastName().toString());
			json.put("deviceData", DataCollector.collectDeviceData(this.mBraintreeFragment));

			PluginResult result = new PluginResult(PluginResult.Status.OK, json);

			this.callbackContext.sendPluginResult(result);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void onCancel(int requestCode)
	{

	}

	public void onError(Exception error)
	{

	}
}
