# RN-SPLASH

`yarn add rn-splash`

* A splash screen that runs on the native thread and is dismissed on the JavaScript thread

## Two separate set-ups, same implementation in React-Native land

## IOS
* Uses the Launch Image catalog (a single image) to show while the app is loading.
*  Launch Images are specific to phone sizes to you will have to create separate images for each screen size

## Android
*  Since there are 13,000+ Android devices, creating an image for each screen would be counter productive.
*  I have decided to go with a 2-image approach
*  One image is used as the `background` and the other image is centered within the background and is called the `logo`

## Installation (iOS)

* Drag RNSplashScreen.xcodeproj to your project on Xcode.

* Click on your main project file (the one that represents the .xcodeproj) select Build Phases and drag libRNSplashScreen.a from the Products folder inside the RNSplashScreen.xcodeproj.

* Look for Header Search Paths and make sure it contains `$(SRCROOT)/../../../react-native/React` as recursive.

* In your project, Look for Header Search Paths and make sure it contains `$(SRCROOT)/../node_modules/rn-splash/ios/RNSplashScreen/RNSplashScreen` as recursive

* delete your project's LaunchScreen.xib

* [Add your app's launch image set](https://developer.apple.com/ios/human-interface-guidelines/icons-and-images/launch-screen/)

##### In AppDelegate.m add two lines of code, remove one

```c
// right below other imports
#import "RNSplashScreen.h"

// remove this
// rootView.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];

// replace with this
[RNSplashScreen open:rootView];
```

## Installation (Android)

* In `android/settings.gradle`

```
...
include ':rn-splash'
project(':rn-splash').projectDir = new File(rootProject.projectDir, '../node_modules/rn-splash/android')
```

* In `android/app/build.gradle`

```
...
dependencies {
    ...
    // From node_modules
    compile project(':rn-splash')
}
```

* Create a drawable folder to put your splash images in.  The path should be `android/app/src/main/res/drawable`

* Make the background image title is `background.{extension}` full path `res/drawable/background.{extension}`

* Make the logo image title is `logo.{extension}` full path `res/drawable/logo.{extension}`

* [You can make folders for different screen resolutions]()

```java
drawable-ldpi/
  background.png
  logo.png

drawable-mdpi/
  background.png
  logo.png

drawable-hdpi/
  background.png
  logo.png

drawable-xhdpi/
  background.png
  logo.png

drawable-xxhdpi/
  background.png
  logo.png

drawable-xxxhdpi/
  background.png
  logo.png
```

### We also need to tell Java Land how big to make the center logo within the background.
*  Create a file called `integers/xml` within your projects `values`
*  full path `android/app/src/main/res/values/integers.xml`
*  put two values, `logo_height` and `logo_width` within

```xml
<resources>
    <integer name="logo_height">100</integer>
    <integer name="logo_width">100</integer>
</resources>

```

#### MainActivity.java

```java
// import these FOUR things
import com.parkerdan.rnsplashscreen.RNSplashScreen;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.ReactInstanceManager;
import android.os.Bundle;

// Add this method to the MainActivity class
  @Override
    protected void onCreate(Bundle savedInstanceState) {
      RNSplashScreen.show(this);
      super.onCreate(savedInstanceState);
   }


```

#### MainApplication.java

```java
// import the package
import com.parkerdan.rnsplashscreen.RNSplashScreen;


// Add to packages
...
    new MainReactPackage(),
    new RNSplashScreenPackage()
...

```

## Usage

```js
...
import SplashScreen from 'rn-splash'
...
componentDidMount () {
  SplashScreen.close("scale", 850, 500)
}
...

```

## Method

* close(animationType, duration, delay)
  close splash screen with custom animation

  * animationType: one of ("scale","fade","none")
  * duration: determine the duration of animation
  * delay: determine the delay of animation
