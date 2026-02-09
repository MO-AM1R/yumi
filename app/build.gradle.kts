plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.services)
    alias(libs.plugins.safe.args)
}

android {
    namespace = "com.example.yumi"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.yumi"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidyoutubeplayer.core)
    implementation(libs.credentials.play.services)
    implementation(libs.retrofit.adapter.rxjava3)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.animated.bottom.bar)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.logging.interceptor)
    annotationProcessor(libs.room.compiler)
    implementation(libs.firebase.firestore)
    implementation(libs.blurview.vversion)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.constraintlayout)
    implementation(libs.refresh.layout)
    implementation(libs.converter.gson)
    implementation(libs.firebase.auth)
    implementation(libs.room.runtime)
    implementation(libs.credentials)
    implementation(libs.google.gms)
    implementation(libs.rxandroid)
    implementation(libs.appcompat)
    implementation(libs.googleid)
    implementation(libs.retrofit)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.fragment)
    implementation(libs.palette)
    implementation(libs.rxjava)
    implementation(libs.okhttp)
    implementation(libs.glide)
    implementation(libs.glide)
    implementation(libs.core)

    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    testImplementation(libs.junit)
}