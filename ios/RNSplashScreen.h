//
//  RNSplashScreen.h
//  RNSplashScreen
//
//  Created by Daniel Parker on 3/12/18.
//

#import <React/RCTBridgeModule.h>
#import <React/RCTRootView.h>

@interface RNSplashScreen : NSObject <RCTBridgeModule>

+ (void)open:(RCTRootView *)v;

@end
