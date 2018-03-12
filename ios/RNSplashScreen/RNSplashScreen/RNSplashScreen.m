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

RCT_EXPORT_MODULE(RNSplashScreen)

+ (NSString *)splashImageNameForOrientation {
  CGRect screenRect = [[UIScreen mainScreen] bounds];
  CGFloat screenWidth = screenRect.size.width;
  CGFloat screenHeight = screenRect.size.height;
  CGSize viewSize = CGSizeMake(screenWidth, screenHeight);

  UIInterfaceOrientation orientation =
    [UIApplication sharedApplication].statusBarOrientation;

  NSString* viewOrientation = @"Portrait";
  if (UIDeviceOrientationIsLandscape(orientation)) {
    viewSize = CGSizeMake(viewSize.height, viewSize.width);
    viewOrientation = @"Landscape";
  }

  NSArray* imagesDict = [
    [[NSBundle mainBundle] infoDictionary] valueForKey:@"UILaunchImages"
  ];

  for (NSDictionary* dict in imagesDict) {
    CGSize imageSize = CGSizeFromString(dict[@"UILaunchImageSize"]);
    if (
      CGSizeEqualToSize(imageSize, viewSize) &&
      [viewOrientation isEqualToString:dict[@"UILaunchImageOrientation"]]
    )
      return dict[@"UILaunchImageName"];
  }
  return nil;
}

+ (void)open:(RCTRootView *)v {
  rootView = v;
  UIImageView *view = [
   [UIImageView alloc]initWithFrame:[UIScreen mainScreen].bounds
  ];


  NSString* launchImage = [RNSplashScreen splashImageNameForOrientation];

  view.image = [UIImage imageNamed:launchImage];


  [
    [NSNotificationCenter defaultCenter]
    removeObserver:rootView
    name:RCTContentDidAppearNotification
    object:rootView
  ];

  [rootView setLoadingView:view];
}

RCT_EXPORT_METHOD(
  close:(NSString *)animationType
  duration:(NSInteger)duration
  delay:(NSInteger)delay
) {
  if (!rootView) {
     return;
  }

  if([animationType isEqualToString:@"none"]) {
    rootView.loadingViewFadeDelay = 0;
    rootView.loadingViewFadeDuration = 0;
  } else {
    rootView.loadingViewFadeDelay = delay / 1000.0;
    rootView.loadingViewFadeDuration = duration / 1000.0;
  }

   dispatch_after(
      dispatch_time(
       DISPATCH_TIME_NOW,
       (int64_t)(rootView.loadingViewFadeDelay * NSEC_PER_SEC)
      ),
      dispatch_get_main_queue(),
      ^{
        [
          UIView animateWithDuration:rootView.loadingViewFadeDuration
          animations: ^{
            if([animationType isEqualToString:@"scale"]) {
              rootView.loadingView.transform = CGAffineTransformMakeScale(
                1.5,
                1.5
              );
              rootView.loadingView.alpha = 0;
            } else {
              rootView.loadingView.alpha = 0;
            }
          }
          completion: ^(__unused BOOL finished) {
            [rootView.loadingView removeFromSuperview];
          }
        ];
      }
  );

}

@end
