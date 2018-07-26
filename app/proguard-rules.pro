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
-keep class com.lljjcoder.**{
	*;
}

-keep class com.yanzhenjie.**{
	*;
}

-dontwarn demo.**
-keep class demo.**{*;}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.format.exception.**{*;}


# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#指定代码的压缩级别
-optimizationpasses  5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

#优化不优化输入的类文件
-dontoptimize

#不做预校验
-dontpreverify

#混淆时是否记录日志
-verbose

#保护注解
-keepattributes  *Annotation*

#  保持哪些类不被混淆
-keep  public  class  *  extends  android.app.Fragment
-keep  public  class  *  extends  android.app.Activity
-keep  public  class  *  extends  android.app.Application
-keep  public  class  *  extends  android.app.Service
-keep  public  class  *  extends  android.content.BroadcastReceiver
-keep  public  class  *  extends  android.preference.Preference

#如果引用了v4或者v7包
-dontwarn  android.support.**

#忽略警告
-ignorewarning


####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####
-keep  public  class  *  extends  android.view.View  {
public  <init>(android.content.Context);
public  <init>(android.content.Context,  android.util.AttributeSet);
public  <init>(android.content.Context,  android.util.AttributeSet,  int);
public  void  set*(...);
}

#保持  native  方法不被混淆
#-keepclasseswithmembernames  class  *  {
#native  <methods>;
#}

#保持自定义控件类不被混淆
#-keepclasseswithmembers  class  *  {
#	public  <init>(android.content.Context,  android.util.AttributeSet);
#}

-keepclassmembers  class  *  extends  android.app.Activity  {
<fields>;
        <methods>;
}

#保持  Parcelable  不被混淆
#-keep  class  *  implements  android.os.Parcelable  {
#	public  static  final  android.os.Parcelable$Creator  *;
#}

#保持  Serializable  不被混淆
#-keepnames  class  *  implements  java.io.Serializable

#-keepclassmembers  enum  *  {
#    public  static  **[]  values();
#    public  static  **  valueOf(java.lang.String);
#}

#不被混淆  格式：-keep  class  类所在包的全路径名称
-keep  class  com.advertisement.sdk.**  {
        protected  <fields>;
protected  void  recoFile;
protected  <methods>;
public  <fields>;
public  void  recoFile;
public  <methods>;
}

-keep  class  com.jpp.mall.activity.**  {
        protected  <fields>;
protected  void  recoFile;
protected  <methods>;
public  <fields>;
public  void  recoFile;
public  <methods>;
}

-dontwarn
-dontpreverify
-flattenpackagehierarchy
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-ignorewarnings
-overloadaggressively
-keepattributes *Annotation*
-useuniqueclassmembernames

-keep class **.R$* {*;}
-keep class **.R{*;}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
# JNI
-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public <init>(org.json.JSONObject);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepnames class * implements java.io.Serializable

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclassmembers class * {
  public <init>(android.content.Context);
}

##################################################

-keepattributes *Annotation*
-keepattributes Signature

-dontshrink
-dontoptimize

-keepattributes Exceptions,InnerClasses,Signature
-keepattributes SourceFile,LineNumberTable


-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keep class org.apache.commons.httpclient.** {*;}
-keep class m.frameword.** {*;}


-keep public class com.jpp.mall.R$*{
    public static final int *;
}

-keep public class * extends android.database.sqlite.SQLiteOpenHelper



-keep class de.greenrobot.dao.** {*;}
#保持greenDao的方法不被混淆
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
	public static java.lang.String TABLENAME;
}
-keep class **$Properties

# 高德定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索sdk
#-keep class com.terark.**{*;}
# Keep missing symbols
-keepattributes MODE
# Keep Jackson stuff
-keep class org.codehaus.** { *; }
-keep class com.fasterxml.jackson.annotation.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }
# Keep Apache Lucene
-keep class org.apache.lucene.** { *; }
-keep class org.apache.lucene.search.** { *; }
-keep class org.lukhnos.portmobile.file.** { *; }



-ignorewarnings

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}



-keep public class [com.jpp.mall].R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#指定代码的压缩级别
-optimizationpasses 5

#包明不混合大小写
-dontusemixedcaseclassnames

#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

#优化不优化输入的类文件
-dontoptimize

#不做预校验
-dontpreverify

#混淆时是否记录日志
-verbose

#保护注解
-keepattributes *Annotation*

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference

# 腾讯x5smtt
-keep class com.tencent.smtt.**{*;}

#如果引用了v4或者v7包
-dontwarn android.support.**

#忽略警告
-ignorewarning


####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####
-keep public class * extends android.view.View {
	public <init>(android.content.Context);
	public <init>(android.content.Context, android.util.AttributeSet);
	public <init>(android.content.Context, android.util.AttributeSet, int);
	public void set*(...);
}

#保持 native 方法不被混淆
#-keepclasseswithmembernames class * {
#native <methods>;
#}

#保持自定义控件类不被混淆
#-keepclasseswithmembers class * {
#	public <init>(android.content.Context, android.util.AttributeSet);
#}

-keepclassmembers class * extends android.app.Activity {
	<fields>;
    <methods>;
}

-keep class tv.danmaku.ijk.media.**{*;}

-keep class org.greenrobot.greendao.**{*;}
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

#使用注解需要添加
-keepattributes *Annotation*
#反射忽略
-keepattributes Signature
-keepattributes EnclosingMethod
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {#指定不混淆所有的JNI方法
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {#所有View的子类及其子类的get、set方法都不进行混淆
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {#不混淆Activity中参数类型为View的所有方法
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {#不混淆Enum类型的指定方法
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#不混淆Parcelable和它的子类，还有Creator成员变量
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#不混淆R类里及其所有内部static类中的所有static变量字段
-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**#不提示兼容库的错误警告



-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.**
-keep public class com.android.vending.licensing.ILicensingService

-dontwarn javax.annotation.processing.**
-keep class javax.annotation.processing.** {*;}
-dontwarn javax.lang.model.element.**
-keep class javax.lang.model.element.** {*;}
-keepattributes InnerClasses

#友盟
-keep class com.umeng.** { *; }
-keep class com.umeng.analytics.** { *; }
-keep class com.umeng.common.** { *; }
-keep class com.umeng.newxp.** { *; }
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

#ormlite
-keep class com.j256.ormlite.** { *; }
-keep class com.j256.ormlite.android.** { *; }
-keep class com.j256.ormlite.field.** { *; }
-keep class com.j256.ormlite.stmt.** { *; }

#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
public void onEventMainThread(**); #所有监听的方法都要列在这里
}

# okio
-dontwarn okio.**
-keep class okio.** { *; }

# 百度地图
-dontwarn com.baidu.**
-keep class com.baidu.** { *; }

#Gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#这句非常重要，主要是滤掉 com.bgb.scan.model包下的所有.class文件不进行混淆编译
-keep class com.bgb.scan.model.** {*;}
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.opena


#不混淆所有的cn.beyondsoft.lawyer.model包下的类和这些类的所有成员变量
-keep class cn.beyondsoft.lawyer.model.**{*;}

-keep class com.hp.hpl.sparta.**{*;}
-keep class com.hp.hpl.sparta.xpath.**{*;}

#pinyin4j
-dontwarn demo.**
-keep class demo.**{*;}
-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.fo


# Ping++ 混淆过滤
-dontwarn com.pingplusplus.**
-keep class com.pingplusplus.** {*;}

# 支付宝混淆过滤
-dontwarn com.alipay.**
-keep class com.alipay.** {*;}

# 微信或QQ钱包混淆过滤
-dontwarn  com.tencent.**
-keep class com.tencent.** {*;}

# 银联支付混淆过滤
-dontwarn  com.unionpay.**
-keep class com.unionpay.** {*;}

# 招行一网通混淆过滤
-keepclasseswithmembers class cmb.pb.util.CMBKeyboardFunc {
    public <init>(android.app.Activity);
    public boolean HandleUrlCall(android.webkit.WebView,java.lang.String);
    public void callKeyBoardActivity();
}

# 内部WebView混淆过滤
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.** {* ;}
-keep class com.tencent.mid.** {* ;}
-keep class com.qq.taf.jce.** {*;}
