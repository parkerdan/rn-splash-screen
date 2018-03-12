package com.parkerdan.rnsplashscreen;

import android.os.Handler;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

class RNSplashScreenModule extends ReactContextBaseJavaModule {

   public RNSplashScreenModule(ReactApplicationContext reactContext) {
      super(reactContext);
   }

   @Override
   public String getName() {
      return "RNSplashScreen";
   }

   @ReactMethod
    public void close(
      final String animationType,
      final int duration,
      final int delay
    ) {
      final Handler handler = new Handler();
      handler.postDelayed(new Runnable() {
        public void run() {
            RNSplashScreen.remove(getCurrentActivity(), animationType, duration);
        }
      }, delay);
    }
}
