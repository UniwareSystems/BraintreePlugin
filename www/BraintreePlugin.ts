declare var cordova: any;
class BraintreePlugin {

    public PayPal: PayPal = new PayPal();
    public Cards: Cards = new Cards();

    public init(apiKey: string, onSuccess: Function, onFailed: Function) {
        cordova.exec(onSuccess, onFailed, "BraintreePlugin", "initWithSettings", [apiKey]);
    }
    public dismiss(onSuccess: Function, onFailed: Function) {
        cordova.exec(onSuccess, onFailed, "BraintreePlugin", "dismiss", []);
    }
}

class Cards {
    public tokeniseCard(cardNumber: string, expiry: string, cvv: string, onSuccess: Function, onFailed: Function) {
        cordova.exec(onSuccess, onFailed, "BraintreePlugin", "cardTokenise", [cardNumber, expiry, cvv]);
    }
}

class PayPal {
    public processImmediate(amount, currency, env: string, onSuccess: Function, onFailed: Function) {
        cordova.exec(onSuccess, onFailed, "BraintreePlugin", "paypalProcessImmediate", [amount, currency, env]);
    }
    public processVaulted(env: string, onSuccess: Function, onFailed: Function) {
        cordova.exec(onSuccess, onFailed, "BraintreePlugin", "paypalProcessVaulted", [env]);
    }
}

cordova.addConstructor(function()
{
  if (!window.plugins)
    window.plugins = {};
  
  if (!window.plugins.braintreeApp)
    window.plugins.braintreeApp = new BraintreePlugin();
});