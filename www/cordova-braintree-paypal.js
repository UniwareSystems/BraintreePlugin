// constructor
//
function BraintreePayPal () {
	
}

// init with environment variables
//
BraintreePayPal.prototype.init = function(apiKey, successCallback, failureCallback)
{
  cordova.exec(successCallback, failureCallback, "BraintreePlugin", "initWithSettings", [apiKey])
};

BraintreePayPal.prototype.processImmediate = function(amount, currency, env, successCallback, failureCallback)
{
  cordova.exec(successCallback, failureCallback, "BraintreePlugin", "processImmediate", [amount, currency, env])
};

BraintreePayPal.prototype.processVaulted = function(env, successCallback, failureCallback)
{
  cordova.exec(successCallback, failureCallback, "BraintreePlugin", "processVaulted", [env])
};

// Dismisses Braintree dialog window if developer no more needs it
//
BraintreePayPal.prototype.dismiss = function(successCallback, failureCallback)
{
	cordova.exec(successCallback, failureCallback, "BraintreePlugin", "dismiss", [])
  console.log('Dismiss');
};

module.exports = new BraintreePayPal();