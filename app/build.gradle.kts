plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services") version "4.4.2"
    id("kotlin-kapt")
    id("kotlin-parcelize")
}
android {
    namespace = "com.duc.karaoke_app"
    compileSdk = 35

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
    }
    defaultConfig {
        applicationId = "com.duc.karaoke_app"
        minSdk = 31
        targetSdk = 35
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.gms.play.services.auth)
    implementation(libs.firebase.storage)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.glide)
    implementation(libs.core)
    //thư viện liveStream
    implementation(libs.rtplibrary)
    implementation(libs.rtmp)
    implementation(libs.exoplayer)
    implementation(libs.circleimageview)

    implementation("com.google.api-client:google-api-client-android:1.31.2")
    implementation("com.google.apis:google-api-services-drive:v3-rev197-1.25.0")
    implementation("com.google.http-client:google-http-client-gson:1.41.3")
    implementation("com.google.http-client:google-http-client-jackson2:1.42.3")

    implementation (libs.androidx.viewpager2)

    implementation ("com.airbnb.android:lottie:6.1.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation (libs.shimmer)
    implementation (libs.mpandroidchart)
    implementation (libs.billing)



}
