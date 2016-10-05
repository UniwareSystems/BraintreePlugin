#import <Cordova/CDV.h>
#import <BraintreeCore/BraintreeCore.h>
#import <BraintreePayPal/BraintreePayPal.h>
#import <BraintreeDataCollector/BraintreeDataCollector.h>
// This is js-native wrapper for Braintree SDK for iOS

@interface BraintreePlugin : CDVPlugin<BTAppSwitchDelegate, BTViewControllerPresentingDelegate>

@property (nonatomic, strong) BTAPIClient *braintreeClient;
@property (nonatomic, strong) BTDataCollector *dataCollector;

-(void)initWithSettings:(CDVInvokedUrlCommand *)command;
-(void)processImmediate:(CDVInvokedUrlCommand *)command;
-(void)processVaulted:(CDVInvokedUrlCommand *)command;

@end
