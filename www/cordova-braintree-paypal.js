
/*
* PhoneGap plugin for BrainTree payments
* BraintreePlugin.js
*
*
* by Stephen Hewison
*/


// constructor
//
function Plugin () {
	console.log('Construct');
}

// init with environment variables
//
Plugin.prototype.init = function(apiKey, successCallback, failureCallback)
{
  cordova.exec(successCallback, failureCallback, "BraintreePlugin", "initWithSettings", [apiKey])
};

Plugin.prototype.processImmediate = function(amount, currency, env, successCallback, failureCallback)
{
  cordova.exec(successCallback, failureCallback, "BraintreePlugin", "processImmediate", [amount, currency, env])
};

Plugin.prototype.processVaulted = function(env, successCallback, failureCallback)
{
  cordova.exec(successCallback, failureCallback, "BraintreePlugin", "processVaulted", [env])
};

// Dismisses Braintree dialog window if developer no more needs it
//
Plugin.prototype.dismiss = function(successCallback, failureCallback)
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
  
  if (!window.plugins.braintreeApp)
    window.plugins.braintreeApp = new Plugin();
  
});
