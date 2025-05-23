plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.rerng_app_report_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.rerng_app_report_project"
        minSdk = 24
        targetSdk = 35

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        flavorDimensions += "rerng_app"
        productFlavors{
            create("dev"){
                dimension = "rerng_app"
                applicationId = "com.example.rerng_app.dev"
                resValue("string", "app_name", "rerng_app Test")
                buildConfigField("String", "apiBaseUrl",
                    "\"http://10.0.2.2:8000/\"")
            }
            create("prd"){
                dimension = "rerng_app"
                applicationId = "com.example.rerng_app"
                resValue("string", "app_name", "rerng_app")
                buildConfigField("String", "apiBaseUrl",
                    "\"http://10.0.2.2:8000/\"")
            }
        }

    }
    signingConfigs {
        create("release") {
            storeFile = file("C:\\Users\\M\\Rerng_app_report_project\\app\\rerng_app.jks")
            storePassword = "ravit123"
            keyAlias = "upload"
            keyPassword = "ravit123"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs["release"]
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.security.crypto.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Networking & API handling
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Lifecycle and ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    // Fragment and Activity KTX
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.5.7")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    // UI Components
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Security & Encryption
    implementation("androidx.security:security-crypto:1.1.0-alpha05")

    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.x.x")
}


