plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.kitchenmasters"
    compileSdk = 34




    defaultConfig {
        applicationId = "com.example.kitchenmasters"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = (true)
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    buildFeatures {
        viewBinding = true
    }
    allprojects {
        tasks.withType<JavaCompile> {
            options.compilerArgs.addAll(listOf("-Xlint:deprecation"))
        }
    }
}



dependencies {
    // Volley for network requests
    implementation ("com.android.volley:volley:1.2.1")

    // Retrofit for handling HTTP requests
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")

    // Image loading library
    implementation ("com.squareup.picasso:picasso:2.71828")

    // Mockito for mocking objects in tests
    implementation ("org.mockito:mockito-core:3.12.4")

    // Core KTX for Kotlin extensions
    implementation ("androidx.core:core-ktx:1.12.0")
    //shared preferences

    // Google Play services for text recognition
    implementation ("com.google.android.gms:play-services-vision:20.1.3")
    // AndroidX libraries
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.activity:activity:1.9.0")
    implementation ("androidx.navigation:navigation-fragment:2.7.7")
    implementation ("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.ar:core:1.42.0")

    testImplementation ("junit:junit:4.13.2")
    // AndroidX Test - Core library
    androidTestImplementation ("androidx.test:core:1.3.0")
    // AndroidX Test - JUnit Runner and Rules
    androidTestImplementation ("androidx.test:runner:1.3.0")
    androidTestImplementation ("androidx.test:rules:1.3.0")
    // Mockito for mocking
    androidTestImplementation ("org.mockito:mockito-core:3.x")
    // Mockito Android support
    androidTestImplementation ("org.mockito:mockito-android:3.x")
    // Espresso for UI tests
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
}