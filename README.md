# EmotControl

This library provides a convenient Java interface to interact with our hardware. The library also provides properties via string resources:

```
<string-array name="arm_actions_array">
    <item>Stable</item>
    <item>Up</item>
    <item>Down</item>
</string-array>

<string-array name="led_colors_array">
    <item>Black</item>
    <item>White</item>
    <item>Green</item>
    <item>Red</item>
    <item>Yellow</item>
    <item>Blue</item>
    <item>DarkBlue</item>
</string-array>

<string-array name="emotions_array">
    <item>Reset</item>
    <item>Angry</item>
    <item>Happy</item>
    <item>Sad</item>
    <item>Fear</item>
    <item>Disgust</item>
    <item>Surprise</item>
</string-array>
```

So you can access and use them at your convenience. For example:

```
ArrayAdapter.createFromResource(this, R.array.arm_actions_array, ...);
```

### Setting Up

* Obtain AAR
* File -> Add module -> Import AAR
* In the project's `settings.gradle`: make sure the library module is included (should have been included automatically). It should read `include ':your-app-name', ':libcontrol-release'`.
* In the project's `build.gradle`: add `maven { url "https://jitpack.io" }` to repository list. So it looks like: (this is because we depend on a USB library hosted there)

```
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

* In the app's `build.gradle`: add two dependencies - a USB library and our library. So it looks like:

```
dependencies {
    compile 'com.github.felHR85:UsbSerial:4.5'
    compile project(':libcontrol-release')
    ...
}
```

* **Important!** Register `org.emot.libcontrol.UsbService` in your `AndroidManifest.xml`. So it looks like:

```xml
<application
    ...
    <activity android:name=".MainActivity">
        ...
    </activity>
    <service
        android:name="org.emot.libcontrol.UsbService"
        android:enabled="true" />
</application>
```
* Import stuffs from `org.emot.libcontrol` and play!

### Using the Library

The quickest way to learn how to use the library is to look through the demo app, which is included in this repository.

You must call `EmotControl.initialize(), onPause(), onResume()` at the corresponding time of the app's lifecycle.

Methods available:

```
static void setArm(Arms which, ArmActions action);
static void setLed(Leds which, int r, int g, int b);
static void setLed(Leds which, LedColors presetColor);
static void setEmotion(Emotions which);
```

# Protocol

### Downlink: from Android to Arduino

Data: control signals for two arms and two leds. Format: 4 bytes per message.

```text
    R   G   B
01  __  __  __    left  LED RGB       LED messages
02  __  __  __    right LED RGB

00 'l' 'u'  __    left  arm up        Arm messages
00 'l' 's'  __    left  arm steady
00 'l' 'd'  __    left  arm down
00 'r' 'u'  __    right arm up
00 'r' 's'  __    right arm steady
00 'r' 'd'  __    right arm down

```

### Uplink: from Arduino to Android

Data: pressure sensor value. 32-bit floating point. Format: `"%6.2f %6.2f"`
