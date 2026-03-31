# BrainZone ProGuard rules
-keep class com.brainzone.mindgames.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
