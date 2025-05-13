plugins {
    id(libs.plugins.android.application.get().pluginId)
    id(libs.plugins.kotlin.android.get().pluginId)
}

android {
    compileSdk = libs.versions.compile.sdk.version.get().toInt()

    defaultConfig {
        minSdk = libs.versions.min.sdk.version.get().toInt()
        targetSdk = libs.versions.compile.sdk.version.get().toInt()
        namespace = UserpilotSampleCoordinates.APP_ID

        applicationId = UserpilotSampleCoordinates.APP_ID
        versionCode = UserpilotSampleCoordinates.APP_VERSION_CODE
        versionName = UserpilotSampleCoordinates.APP_VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SEGMENT_WRITE_KEY", "\"qHhRnDri9F4sX87VBmnsbIz4GGZ09IgJ\"")
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    lint {
        warningsAsErrors = true
        abortOnError = true
        disable.add("GradleDependency")
        disable.add("AppBundleLocaleChanges")
        disable.add("MonochromeLauncherIcon")
    }
}

dependencies {
    implementation(libs.segment)
    implementation(projects.segmentUserpilot)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraint.layout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
}
