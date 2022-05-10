@file:Suppress("unused", "MayBeConstant")

object Dependencies {

    /* Gradle */
    private val gradleVersion = "7.0.4"
    val gradleDependency = "com.android.tools.build:gradle:$gradleVersion"

    /* Kotlin */
    private val kotlinVersion = "1.6.0"
    private val kotlinCoroutineVersion = "1.6.0"
    val kotlinGradlePluginDependency = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion"
    val kotlinStdLibDependency = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    val kotlinStdJdk8LibDependency = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
    val kotlinCoroutineCoreDependency = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutineVersion"
    val kotlinCoroutinePlayServiceDependency = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$kotlinCoroutineVersion"
    val kotlinCoroutineAndroidDependency = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinCoroutineVersion"
    val kotlinCoroutineTestDependency = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinCoroutineVersion"

    /* Arrow-Kt */
    private val arrowVersion = "1.0.1"
    val arrowCoreDependency = "io.arrow-kt:arrow-core:$arrowVersion"

    /* Android X */
    val androidDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"
    val androidXCore = "androidx.core:core-ktx:1.7.0"
    val androidXAppCompat = "androidx.appcompat:appcompat:1.4.1"
    val androidXLegacySupportV4 = "androidx.legacy:legacy-support-v4:1.0.0"
    val androidXActivity = "androidx.activity:activity-ktx:1.4.0"
    val androidXFragment = "androidx.fragment:fragment-ktx:1.5.0-alpha02"
    val androidXFragmentTest = "androidx.fragment:fragment-testing:1.5.0-alpha02"

    /* Android X - UI */
    val androidXConstraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    val androidXMaterialDesign = "com.google.android.material:material:1.3.0-beta01"
    val androidXRecyclerViewDependency = "androidx.recyclerview:recyclerview:1.1.0"
    val androidXRecyclerViewSelectionDependency = "androidx.recyclerview:recyclerview-selection:1.1.0-rc03"
    val androidXViewPager2Dependency = "androidx.viewpager2:viewpager2:1.0.0"

    /* Android X - Navigation */
    private val navigationVersion = "2.3.5"
    val androidXNavigationSafeArgsGradlePluginDependency = "androidx.navigation:navigation-safe-args-gradle-plugin:$navigationVersion"
    val androidXNavigationSafeArgsPlugin = "androidx.navigation.safeargs.kotlin"
    val androidXNavigationFragment = "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
    val androidXNavigationUi = "androidx.navigation:navigation-ui-ktx:$navigationVersion"
    val androidXNavigationTest = "androidx.navigation:navigation-testing:$navigationVersion"

    /* Android X - Room */
    private val roomVersion = "2.4.0-rc01"
    val roomDependency = "androidx.room:room-runtime:$roomVersion"
    val roomCompilerDependency = "androidx.room:room-compiler:$roomVersion"
    val roomKtxDependency = "androidx.room:room-ktx:$roomVersion"
    val roomTestDependency = "androidx.room:room-testing:$roomVersion"

    /* Android X - Data Sore */
    val dataStorePreferenceDependency = "androidx.datastore:datastore-preferences:1.0.0-alpha06"

    /* Android X - Work */
    private val workVersion = "2.7.1"
    val workDependency = "androidx.work:work-runtime-ktx:$workVersion"
    val workGcmDependency = "androidx.work:work-gcm:$workVersion"
    val workMultiProcessDependency = "androidx.work:work-multiprocess:$workVersion"
    val workAndroidTestDependency = "androidx.work:work-testing:$workVersion"

    /* Android X - STARTUP */
    val startupDependency = "androidx.startup:startup-runtime:1.1.0"

    /* Glide */
    private val glideVersion = "4.11.0"
    val glideDependency = "com.github.bumptech.glide:glide:$glideVersion"
    val glideCompilerDependency = "com.github.bumptech.glide:compiler:$glideVersion"

    /* DI */
    private val javaxAnnotationVersion = "1.0"
    private val javaxInjectVersion = "1"
    val javaxAnnotationDependency = "javax.annotation:jsr250-api:$javaxAnnotationVersion"
    val javaxInjectDependency = "javax.inject:javax.inject:$javaxInjectVersion"

    /* Hilt */
    private val hiltVersion = "2.38.1"
    val hiltGradlePluginDependency = "com.google.dagger:hilt-android-gradle-plugin:$hiltVersion"
    val hiltDependency = "com.google.dagger:hilt-android:$hiltVersion"
    val hiltCompilerDependency = "com.google.dagger:hilt-android-compiler:$hiltVersion"
    val hiltWorkManagerDependency = "androidx.hilt:hilt-work:1.0.0"
    val hiltCompilerWorkManagerDependency = "androidx.hilt:hilt-compiler:1.0.0"

    /* Dagger */
    private val daggerVersion = "2.28"
    val daggerDependency = "com.google.dagger:dagger:$daggerVersion"
    val daggerCompilerDependency = "com.google.dagger:dagger-compiler:$daggerVersion"

    /* Network */
    private val retrofitVersion = "2.9.0"
    private val okHttpVersion = "4.4.0"
    val retrofitDependency = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    val retrofitConverterGsonDependency = "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    val okHttpDependency = "com.squareup.okhttp3:okhttp:$okHttpVersion"
    val mockWebServerDependency = "com.squareup.okhttp3:mockwebserver:4.9.1"
    val loggingInterceptorDependency = "com.squareup.okhttp3:logging-interceptor:$okHttpVersion"

    /* Json Parser */
    val gsonDependency = "com.google.code.gson:gson:2.8.6"

    /* Google */
    private val googleServicesPluginVersion = "4.3.5"
    private val googlePlayServicesAuthVersion = "20.0.1"
    private val googlePlayServicesLocationVersion = "18.0.0"
    private val googlePlayServicesMapVersion = "18.0.2"
    val googleServicesPluginDependency =
        "com.google.gms:google-services:$googleServicesPluginVersion"
    val googlePlayServicesAuthDependency =
        "com.google.android.gms:play-services-auth:$googlePlayServicesAuthVersion"
    val googlePlayServicesLocationDependency =
        "com.google.android.gms:play-services-location:$googlePlayServicesLocationVersion"
    val googlePlayServicesMapDependency =
        "com.google.android.gms:play-services-maps:$googlePlayServicesMapVersion"
    val googlePlayCoreDependency = "com.google.android.play:core:1.10.3"
    val googlePlayCoreKtxDependency = "com.google.android.play:core-ktx:1.8.1"

    /* Map Box */
    val mapBoxSdkVersion = "9.5.0"
    val mapBoxSdkDependency = "com.mapbox.mapboxsdk:mapbox-android-sdk:${mapBoxSdkVersion}"

    /* Test */
    private val junitVersion = "4.13"
    private val hamcrestVersion = "2.2"
    private val mockitoVersion = "3.1.0"
    private val mockKVersion = "1.12.2"
    private val robolectricVersion = "4.5.1"
    private val androidXTestVersion = "1.4.0"
    private val androidXTestEspressoVersion = "3.3.0"
    private val androidXTestUiAutomatorVersion = "2.2.0"
    val junitDependency = "junit:junit:$junitVersion"
    val hamcrestDependency = "org.hamcrest:hamcrest:$hamcrestVersion"
    val mockitoDependency = "org.mockito:mockito-core:$mockitoVersion"
    val mockitoInlineDependency = "org.mockito:mockito-inline:$mockitoVersion"
    val mockKDependency = "io.mockk:mockk:$mockKVersion"
    val robolectricDependency = "org.robolectric:robolectric:$robolectricVersion"
    val androidXTestCoreDependency = "androidx.test:core:$androidXTestVersion"
    val androidXTestArchCoreDependency = "androidx.arch.core:core-testing:2.0.0"
    val androidXTestRunnerDependency = "androidx.test:runner:$androidXTestVersion"
    val androidXTestRulesDependency = "androidx.test:rules:$androidXTestVersion"
    val androidXTestExtJunitDependency = "androidx.test.ext:junit:1.1.3"
    val androidXTestExtTruthDependency = "androidx.test.ext:truth:1.0.0"
    val googleTruthDependency = "com.google.truth:truth:1.0.0"
    val androidXTestEspressoCoreDependency =
        "androidx.test.espresso:espresso-core:$androidXTestEspressoVersion"
    val androidXTestEspressoContribDependency =
        "androidx.test.espresso:espresso-contrib:$androidXTestEspressoVersion"
    val androidXTestEspressoIntentsDependency =
        "androidx.test.espresso:espresso-intents:$androidXTestEspressoVersion"
    val androidXTestEspressoAccessibilityDependency =
        "androidx.test.espresso:espresso-accessibility:$androidXTestEspressoVersion"
    val androidXTestEspressoWebDependency =
        "androidx.test.espresso:espresso-web:$androidXTestEspressoVersion"
    val androidXTestEspressoIdlingConcurrentDependency =
        "androidx.test.espresso.idling:idling-concurrent:$androidXTestEspressoVersion"
    val androidXTestEspressoIdlingResourceDependency =
        "androidx.test.espresso:espresso-idling-resource:$androidXTestEspressoVersion"
    val androidXTestUiAutomatorDependency =
        "androidx.test.uiautomator:uiautomator:$androidXTestUiAutomatorVersion"

}