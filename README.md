# openCloseTime
Simple reactive style Android library to capture place open/close time

<img src="https://raw.githubusercontent.com/ruwanka/openCloseTime/master/screenshot1.png" width="250"> <img src="https://raw.githubusercontent.com/ruwanka/openCloseTime/master/screenshot2.png" width="250">

# Usage

Add jitpack.io to your project root build.gradle

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Add the dependency

```
dependencies {
	  implementation 'com.github.ruwanka:openCloseTime:v0.1-alpha'
  }
```
Use it in your layout

```
  <com.ruwanka.openclosetime.OpenCloseTimeView
    android:id="@+id/openCloseTimeView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:oct_buttonBackground="@drawable/text_input_background"
    app:oct_bottomMargin="8dp"
    app:oct_itemGap="8dp"
    app:oct_itemHeight="50dp">
 ```
Observe changes in your activity

```
OpenCloseTimeView timeView = findViewById(R.id.openCloseTimeView);
...
timeView.observe()
        .subscribe(openCloseTime -> Log.d(TAG, openCloseTime.toString()));
```
Checkout sample project for working example.

Kudos to the developers of [MaterialDateTimePicker](https://github.com/wdullaer/MaterialDateTimePicker)
