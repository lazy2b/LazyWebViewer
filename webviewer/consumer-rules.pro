-keepattributes Exceptions
# okhttp3
-dontnote okio.**
-dontnote okhttp3.**
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}

# fastjson
-dontnote com.alibaba.fastjson.**
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}

-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-keepattributes AnnotationDefault
-dontwarn java.lang.annotation.Annotation

-keep class com.lazylibs.webviewer.update.VersionUpdateModel { *; }

####################################################################################################
############ adjust 配置 ############
## 非谷歌play只需要以下配置
-keep public class com.adjust.sdk.** { *; }
## 谷歌play则需要以下配置
#-keep class com.adjust.sdk.** { *; }
#-keep class com.google.android.gms.common.ConnectionResult {
#    int SUCCESS;
#}
#-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
#    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
#}
#-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
#    java.lang.String getId();
#    boolean isLimitAdTrackingEnabled();
#}
#-keep public class com.android.installreferrer.** { *; }
####################################################################################################



## 版本更新
#-dontwarn java.lang.invoke.**
#-keep public interface com.lazylibs.updater.** {*;}
#-keep public enum com.lazylibs.updater.** {*;}
#-keep public class com.lazylibs.updater.** {
#    public *** **(...);
#}
##