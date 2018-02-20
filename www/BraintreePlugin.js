var BraintreePlugin = /** @class */ (function () {
  function BraintreePlugin() {
      this.PayPal = new PayPal();
      this.Cards = new Cards();
  }
  BraintreePlugin.prototype.init = function (apiKey, onSuccess, onFailed) {
      cordova.exec(onSuccess, onFailed, "BraintreePlugin", "initWithSettings", [apiKey]);
  };
  BraintreePlugin.prototype.dismiss = function (onSuccess, onFailed) {
      cordova.exec(onSuccess, onFailed, "BraintreePlugin", "dismiss", []);
  };
  return BraintreePlugin;
}());
var Cards = /** @class */ (function () {
  function Cards() {
  }
  Cards.prototype.tokeniseCard = function (cardNumber, expiry, cvv, onSuccess, onFailed) {
      cordova.exec(onSuccess, onFailed, "BraintreePlugin", "cardTokenise", [cardNumber, expiry, cvv]);
  };
  return Cards;
}());
var PayPal = /** @class */ (function () {
  function PayPal() {
  }
  PayPal.prototype.processImmediate = function (amount, currency, env, onSuccess, onFailed) {
      cordova.exec(onSuccess, onFailed, "BraintreePlugin", "paypalProcessImmediate", [amount, currency, env]);
  };
  PayPal.prototype.processVaulted = function (env, onSuccess, onFailed) {
      cordova.exec(onSuccess, onFailed, "BraintreePlugin", "paypalProcessVaulted", [env]);
  };
  return PayPal;
}());
cordova.addConstructor(function () {
  if (!window.plugins)
      window.plugins = {};
  if (!window.plugins.braintreeApp)
      window.plugins.braintreeApp = new BraintreePlugin();
});
