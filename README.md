# Restaurant Management System

After cloning the source, you should install all dependencies by running
```
npm install
```

## IOS
To run on IOS simulator you have to use [Xcode](https://developer.apple.com/xcode/).

#### Installation
  1. Go to ios directory
  2. Run `pod install`
  3. Login apple account and Download certificates.


##### Note: If you get an errors on SMXCrashlytics
  1. In Xcode go to `SMXCrashlytics` project
  2. Choose `Build Settings`
  3. Add `$(SRCROOT)/../../../ios/Pods/Crashlytics/iOS` to Framwork Search Paths

## Android
To run on Android Simulator you have to use [Android Studio](https://developer.android.com/studio/index.html)

#### Installation
  1. Go to android directory
  2. Open Rms project
  3. Try to build project and install all needed SDK.
  
##### You can build Debug app into your android phone.
```
./gradlew installStandardDebug
```

### Scene error
If you found scene error in RMS Application.
  1. Uninstall RN router flux `npm uninstall react-native-router-flux`
  2. Reinstall specific version `npm install --save react-native-router-flux@3.38.0`
  3. Rebuild Application
