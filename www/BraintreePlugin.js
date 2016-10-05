
/*
* PhoneGap plugin for BrainTree payments
* BraintreePlugin.js
*
*
* by Stephen Hewison
*/


// constructor
//
function BraintreePlugin () {
	console.log('Construct');
}

// init with environment variables
//
BraintreePlugin.prototype.init = function(apiKey, successCallback, failureCallback)
{
    console.log('BrainTreePayment.init start');
    cordova.exec(successCallback, failureCallback, "BraintreePlugin", "initWithSettings", [apiKey])
    console.log('BrainTreePayment.init done');
};

BraintreePlugin.prototype.processImmediate = function(amount, currency, env, successCallback, failureCallback)
{
  console.log('BrainTreePayment.getCardInfoz start');
  cordova.exec(successCallback, failureCallback, "BraintreePlugin", "processImmediate", [amount, currency, env])
  console.log('BrainTreePayment.getCardInfoz done');
};

BraintreePlugin.prototype.processVaulted = function(env, successCallback, failureCallback)
{
  console.log('BrainTreePayment.getCardInfoz start');
  cordova.exec(successCallback, failureCallback, "BraintreePlugin", "processVaulted", [env])
  console.log('BrainTreePayment.getCardInfoz done');
};

// Dismisses Braintree dialog window if developer no more needs it
//
BraintreePlugin.prototype.dismiss = function(successCallback, failureCallback)
{
	cordova.exec(successCallback, failureCallback, "BraintreePlugin", "dismiss", [])
  console.log('Dismiss');
};

// Constructor makes all methods in this file accesible
// to JavaScript in PhoneGap application
//
cordova.addConstructor(function()
{
  if (!window.plugins)
    window.plugins = {};
  
  if (!window.plugins.braintree)
    window.plugins.braintree = new BraintreePlugin();
  
});
