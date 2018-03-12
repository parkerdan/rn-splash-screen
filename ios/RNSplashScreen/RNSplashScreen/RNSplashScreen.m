//
//  RNSplashScreen.m
//  RNSplashScreen
//
//  Created by Daniel Parker on 3/12/18.
//


#import "RNSplashScreen.h"

static RCTRootView *rootView = nil;

@interface RNSplashScreen()

@end

@implementation RNSplashScreen

RCT_EXPORT_MODULE(SplashScreen)

+ (void)open:(RCTRootView *)v {
   rootView = v;
   UIImageView *view = [[UIImageView alloc]initWithFrame:[UIScreen mainScreen].bounds];

   NSDictionary *dict = @{@"320x480" : @"LaunchImage-700",
                          @"320x568" : @"LaunchImage-700-568h",
                          @"375x667" : @"LaunchImage-800-667h",
                          @"414x736" : @"LaunchImage-800-Portrait-736h"};
   NSString *key = [NSString stringWithFormat:@"%dx%d",
       (int)[UIScreen mainScreen].bounds.size.width,
       (int)[UIScreen mainScreen].bounds.size.height];
   view.image = [UIImage imageNamed:dict[key]];


   [[NSNotificationCenter defaultCenter] removeObserver:rootView name:RCTContentDidAppearNotification object:rootView];

   [rootView setLoadingView:view];
}

RCT_EXPORT_METHOD(close:(NSString *)animationType
                  duration:(NSInteger)duration
                  delay:(NSInteger)delay) {
   if (!rootView) {
       return;
   }

   if([animationType isEqualToString:@"none"]) {
       rootView.loadingViewFadeDelay = 0;
       rootView.loadingViewFadeDuration = 0;
   }
   else {
       rootView.loadingViewFadeDelay = delay / 1000.0;
       rootView.loadingViewFadeDuration = duration / 1000.0;
   }
   dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(rootView.loadingViewFadeDelay * NSEC_PER_SEC)),
                  dispatch_get_main_queue(),
                  ^{
                      [UIView animateWithDuration:rootView.loadingViewFadeDuration
                                       animations:^{
                                           if([animationType isEqualToString:@"scale"]) {
                                               rootView.loadingView.transform = CGAffineTransformMakeScale(1.5, 1.5);
                                               rootView.loadingView.alpha = 0;
                                           }
                                           else {
                                               rootView.loadingView.alpha = 0;
                                           }
                                       } completion:^(__unused BOOL finished) {
                                           [rootView.loadingView removeFromSuperview];
                                       }];
                  });

}

@end
