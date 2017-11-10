# TanRabad Proguard rule

-dontnote android.support.**

-keep,includedescriptorclasses class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

-dontwarn com.squareup.**
-dontwarn okio.**
-dontwarn java.nio.file.Files
-dontwarn java.nio.file.Path
-dontwarn java.nio.file.OpenOption
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

-dontwarn com.bluelinelabs.logansquare.**
-keep,includedescriptorclasses class com.bluelinelabs.logansquare.** { *; }
-keep @com.bluelinelabs.logansquare.annotation.JsonObject class *
-keep class **$$JsonObjectMapper { *; }

-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class th.or.nectec.thai.** { *; }
-dontwarn net.jcip.annotations.Immutable
