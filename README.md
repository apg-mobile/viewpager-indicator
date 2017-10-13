# viewpager-indicator
A practice project for creating custom view in Android

````
allprojects {
	repositories {
	...
	maven { url 'https://jitpack.io' }
	}
}
  
dependencies {
  compile 'com.github.apg-mobile:viewpager-indicator:1.1.0'
}
````

### Example Usage
just call the follwing method and all done !!

````
setViewPager(yourViewPAger);
````

provide some custom attriubte for customizing indicator !!
```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.apg.viewpagerindicator.app.MainActivity">

    <com.apg.viewpagerindicator.library.ViewPagerIndicator
        android:id="@+id/viewPagerIndicator"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginEnd="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        app:indicator_allFill="true"
        app:indicator_color="@color/colorPrimary"
        app:indicator_gravity="center"
        app:indicator_items="3"
        app:indicator_selectedColor="@color/colorAccent"
        app:indicator_size="30dp"
        app:indicator_sizeOffSet="5dp"
        app:indicator_spacing="5dp"
        app:indicator_startPosition="0"
        app:indicator_stokeWidth="5dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="@+id/guideline2" />


    <android.support.constraint.Guideline
        android:id="@+id/guideline2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal"
        app:layout_constraintGuide_percent="0.5" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_marginEnd="8dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="16dp"
        android:gravity="center"
        android:maxLines="1"
        android:paddingTop="6dp"
        android:text="Sample ViewPager Indicator"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/viewPagerIndicator" />

</android.support.constraint.ConstraintLayout>

```
