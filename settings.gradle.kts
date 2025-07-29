@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = ("segment-userpilot-android")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(
    "samples:segment-destination-example",
    "segment-userpilot",
)
