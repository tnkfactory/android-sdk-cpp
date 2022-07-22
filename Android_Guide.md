1. [SDK 설정하기](#1-sdk-설정하기)
   * [라이브러리 등록](#라이브러리-등록) 
   * [Proguard 사용](#proguard-사용)
   * [Manifest 설정하기](#manifest-설정하기)
     * [Activity tag 추가하기](#activity-tag-추가하기)
     * [Publisher ID 등록하기](#publisher-id-등록하기)
     * [Application ID 설정하기](#application-id-설정하기)
   * [COPPA 설정](#coppa-설정)

2. [Main화면 출력하기](#2-메인화면-출력하기)

3. [SDK referance](#3-sdk-referance)

## 1. SDK 설정하기

### 라이브러리 등록

TNK SDK는 Maven Central에 배포되어 있습니다.

settings.gradle에 아래와 같이 mavenCentral()가 포함되어있는지 확인합니다.
```gradle
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "project_name"
include ':app'
```

만약 settings.gradle에 저 부분이 존재하지 않다면 최상위 Level(Project)의 build.gradle에 maven repository를 추가해주세요.
```gradle
repositories {
    mavenCentral()
}
```

아래의 코드를 App Module의 build.gradle 파일에 추가해주세요.
```gradle
dependencies {
    implementation 'com.tnkfactory:cpp:1.1.0'
}
```

### Proguard 사용

Proguard 를 사용하시는 경우 Proguard 설정 파일에 아래의 내용을 반드시 넣어주세요.

```proguard
 -keep class com.tnkfactory.** { *;}
```

### Manifest 설정하기

#### Activity tag 추가하기

광고 목록을 띄우기 위한 Activity 2개를 <activity/>로 아래와 같이 설정합니다. 매체앱인 경우에만 설정하시면 됩니다. 광고만 진행하실 경우에는 설정하실 필요가 없습니다.


```xml
<activity android:name="com.tnkfactory.ad.AdWallActivity" />
<activity android:name="com.tnkfactory.ad.AdMediaActivity" android:screenOrientation="portrait"/>

<!-- 또는 아래와 같이 설정-->
<activity android:name="com.tnkfactory.ad.AdMediaActivity" android:screenOrientation="sensorLandscape"/>
```

### Publisher ID 등록하기

Test Flight 에서는 별도로 계정등록을 하지않아도 간단히 테스트를 진행할 수 있었습니다. 하지만 실제 광고를 받기 위해서는 우선 Tnk Publish Site 에서 Inventory를 등록하여 발급받은 ID 를 AndroidManifest.xml 파일에 추가하셔야합니다.
아래의 샘플을 참고하시어 실제 ID 를 등록하세요.

#### Application ID 설정하기

Tnk 사이트에서 앱 등록하면 상단에 App ID 가 나타납니다. 이를 AndroidMenifest.xml 파일의 application tag 안에 아래와 같이 설정합니다.
(*your-application-id-from-tnk-site* 부분을 실제 App ID 값으로 변경하세요.)


```xml
<application>

    <meta-data android:name="tnkad_app_id" android:value="your-application-id-from-tnk-site" />

</application>
```



AndroidManifest.xml의 내용 예시 
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.tnkfactory.tnk_cpp_sample">
    
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/AppTheme">

        ...
        ...
        <activity android:name="com.tnkfactory.ad.AdWallActivity" android:exported="true"/>
        <activity android:name="com.tnkfactory.ad.AdMediaActivity" android:screenOrientation="fullSensor" android:exported="true"/>
        ...
        ...
        <!-- Pub ID -->
	      <meta-data android:name="tnk_pub_id" android:value="2080502030a1981221891a01090d0b06" />
        <!-- App ID -->
        <meta-data android:name="tnkad_app_id" android:value="50c050c0-e091-84ca-ac48-190e0a07080e" />
        ...
        ...
    </application>
</manifest>	
```



### COPPA 설정

COPPA는 [미국 어린이 온라인 개인정보 보호법](https://www.ftc.gov/tips-advice/business-center/privacy-and-security/children's-privacy) 및 관련 법규입니다. 구글 에서는 앱이 13세 미만의 아동을 대상으로 서비스한다면 관련 법률을 준수하도록 하고 있습니다. 연령에 맞는 광고가 보일 수 있도록 아래의 옵션을 설정하시기 바랍니다.

```java
AdConfiguration.setCOPPA(this, true); // ON - 13세 미안 아동을 대상으로 한 서비스 일경우 사용
AdConfiguration.setCOPPA(this, false); // OFF - 기본값
```

## 2. 메인화면 출력하기

캐시팝팝플러스의 메인화면을 출력하실 경우 다음과 같이 호출하시면 됩니다.


```java

CppConfig config = new CppConfig.Builder()
            .tnkLayout(TemplateLayoutUtils.getBlueTabStyle_01())
            .listener(adViewListener)
            .build();

CashpoppopPlus cpp = new CashpoppopPlus(MainActivity.this, config);
cpp.showAdView(this);

```

COPPA설정, 유저 고유 구분값 설정, 분석도구 사용 등의 설정을 모두 적용한 예제는 다음과 같습니다.

샘플 코드를 참고 하시는 것을 추천합니다.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // 유저 고유 식별값 설정
    TnkSession.setUserName(MainActivity.this, "user_uuid");

    // Analytics Report 사용을 위한 앱 시작 로깅
    TnkSession.applicationStarted(this);

    // COPPA 설정 (true - ON / false - OFF)
    TnkSession.setCOPPA(MainActivity.this, false);

    CppConfig config = new CppConfig.Builder()
            .tnkLayout(TemplateLayoutUtils.getBlueTabStyle_01())
            .listener(adViewListener)
            .build();

    CashpoppopPlus cpp = new CashpoppopPlus(MainActivity.this, config);
    cpp.showAdView(this);

}
    CppAdViewListener adViewListener = new CppAdViewListener() {
        @Override
        public void onClickMenu(CppMainViewId id) {
            super.onClickMenu(id);
            switch (id) {
                case PPI:
                    Log.d(TAG, "onClick 보상형");
                    break;
                case CPS:
                    Log.d(TAG, "onClick 구매형");
                    break;
            }
        }

        @Override
        public void onDismiss() {
            super.onDismiss();
        }
    };
```


## 3. SDK referance


### CashpoppopPlus

캐시팝팝플러스의 메인화면을 출력하는 클래스 입니다. 

```java
public class CashpoppopPlus extends BottomSheetDialogFragment  
```

<pre>
Fragment  
↳ DialogFragment  
 ↳ AppCompatDialogFragment  
  ↳ BottomSheetDialogFragment  
   ↳ CashpoppopPlus  
</pre>

##### Summary

###### Constructors

- public CashpoppopPlus(Activity activity, CppConfig cppConfig)

###### Method
 
- void showAdView(Activity activity)



---
##### public CashpoppopPlus(Activity activity, CppConfig cppConfig)

###### Description

생성자는 화면을 출력하기 위한 Activity와 설정값을 적용하기 위해 CppConfig를 요구합니다.
ㅇ

###### Parameters

| 파라메터 명칭 | 내용                                                         |
| ------------- | ------------------------------------------------------------ |
| Activity      | 현재 Activity 객체                                           |
| cppConfig         | config 클래스   |

###### 적용예시

```java
@Override

public void onCreate(Bundle savedInstanceState) {

    ...

    final Button button = (Button)findViewById(R.id.main_ad);

    button.setOnClickListener(new OnClickListener() {

        @Override

        public void onClick(View v) {

            TnkSession.popupAdList(MainActivity.this,"Your title here");

        }

    });
}
```






