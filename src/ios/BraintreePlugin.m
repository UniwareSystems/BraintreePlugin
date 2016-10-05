#import "BraintreePlugin.h"

@interface BraintreePlugin () <BTAppSwitchDelegate, BTViewControllerPresentingDelegate>
@property (nonatomic, strong) CDVInvokedUrlCommand *command;
@end

@implementation BraintreePlugin

-(void)paymentDriver:(id)paymentDriver requestsPresentationOfViewController:(UIViewController *)viewController
{
     [self.viewController presentViewController:viewController animated:YES completion:nil];
}

-(void)paymentDriver:(id)driver requestsDismissalOfViewController:(UIViewController *)viewController
{
    [self.viewController dismissViewControllerAnimated:YES completion:nil];
}

-(void)openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication
{

}

-(void)initDataCollector:(NSString *)environment
{
    if ([environment isEqual: @"sandbox"])
    {
        self.dataCollector = [[BTDataCollector alloc] initWithEnvironment:BTDataCollectorEnvironmentSandbox];
    }
    else if ([environment isEqual: @"production"])
    {
        self.dataCollector = [[BTDataCollector alloc] initWithEnvironment:BTDataCollectorEnvironmentProduction];
    }
}

-(void)initWithSettings:(CDVInvokedUrlCommand *)command
{
    NSString *clientToken = [command.arguments objectAtIndex:0];

    self.braintreeClient = [[BTAPIClient alloc] initWithAuthorization:clientToken];

    [BTAppSwitch setReturnURLScheme:@"com.upaychilli.UpayChilli.payments"];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

-(void)processImmediate:(CDVInvokedUrlCommand *)command
{
    self.command = command;

    NSString *chargeAmount = [command.arguments objectAtIndex:0];
    NSString *chargeCurrency = [command.arguments objectAtIndex:1];
    NSString *chargeEnvironment = [command.arguments objectAtIndex:2];

    [self initDataCollector:chargeEnvironment];

    BTPayPalDriver *payPalDriver = [[BTPayPalDriver alloc] initWithAPIClient:self.braintreeClient];

    payPalDriver.viewControllerPresentingDelegate = self;
    payPalDriver.appSwitchDelegate = self;

    [payPalDriver authorizeAccountWithCompletion:^(BTPayPalAccountNonce *tokenizedPayPalAccount, NSError *error)
    {
        if (tokenizedPayPalAccount == nil)
        {
            [self userDidCancelPayment];
        }

        CDVPluginResult *pluginResult;

        if (tokenizedPayPalAccount != nil)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
                @"nonce": tokenizedPayPalAccount.nonce,
                @"payerId": tokenizedPayPalAccount.payerId,
                @"forename": tokenizedPayPalAccount.firstName,
                @"surname": tokenizedPayPalAccount.lastName,
                @"deviceData": [BTDataCollector payPalClientMetadataId]
            }];
        }

        if (error != nil)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }

        if (error || tokenizedPayPalAccount)
        {
            [self.commandDelegate sendPluginResult:pluginResult callbackId:self.command.callbackId];
        }
    }];

    BTPayPalRequest *request = [[BTPayPalRequest alloc] initWithAmount:chargeAmount];
    request.currencyCode = chargeCurrency;

    [payPalDriver requestOneTimePayment:request completion:^(BTPayPalAccountNonce * _Nullable tokenizedPayPalAccount, NSError * _Nullable error) {

    }];
}

-(void)processVaulted:(CDVInvokedUrlCommand *)command
{
    self.command = command;

    NSString *chargeEnvironment = [command.arguments objectAtIndex:0];

    [self initDataCollector:chargeEnvironment];

    BTPayPalDriver *payPalDriver = [[BTPayPalDriver alloc] initWithAPIClient:self.braintreeClient];
    payPalDriver.viewControllerPresentingDelegate = self;
    payPalDriver.appSwitchDelegate = self;


    [payPalDriver authorizeAccountWithCompletion:^(BTPayPalAccountNonce *tokenizedPayPalAccount, NSError *error)
    {
        if (tokenizedPayPalAccount == nil)
        {
            [self userDidCancelPayment];
        }

        CDVPluginResult *pluginResult;

        if (tokenizedPayPalAccount != nil)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:@{
                @"nonce": tokenizedPayPalAccount.nonce,
                @"payerId": tokenizedPayPalAccount.payerId,
                @"forename": tokenizedPayPalAccount.firstName,
                @"surname": tokenizedPayPalAccount.lastName,
                @"deviceData": [BTDataCollector payPalClientMetadataId]
            }];
        }

        if (error != nil)
        {
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
        }

        if (error || tokenizedPayPalAccount)
        {
            [self.commandDelegate sendPluginResult:pluginResult callbackId:self.command.callbackId];
        }
    }];

    BTPayPalRequest *request = [[BTPayPalRequest alloc] init];

    [payPalDriver requestBillingAgreement:request completion:^(BTPayPalAccountNonce * _Nullable tokenizedPayPalAccount, NSError * _Nullable error) {

    }];
}

-(void)userDidCancelPayment {
    [self.viewController dismissViewControllerAnimated:YES completion:nil];
}

@end
