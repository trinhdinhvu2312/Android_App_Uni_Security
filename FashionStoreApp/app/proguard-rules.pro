# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-printmapping mapping.txt
-verbose
-dontoptimize
-dontpreverify
-dontshrink
-dontskipnonpubliclibraryclassmembers
-dontusemixedcaseclassnames
-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
#-keep class com.example.fashionstoreapp.Activity.MainActivity
-keep class com.example.fashionstoreapp.Model.** { *; }
#-keep class com.example.fashionstoreapp.Adapter.** { *; }
#-keep class com.example.fashionstoreapp.Fragment.** { *; }
#-keep class com.example.fashionstoreapp.Interface.** { *; }
#-keep class com.example.fashionstoreapp.Retrofit.** { *; }
#-keep class com.example.fashionstoreapp.Somethings.** { *; }
#-keep class com.example.fashionstoreapp.Zalo.** { *; }
