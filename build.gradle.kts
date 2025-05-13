import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id(libs.plugins.android.application.get().pluginId) apply false
    id(libs.plugins.kotlin.android.get().pluginId) apply false
    id(libs.plugins.android.library.get().pluginId) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.versions)
    base
}

allprojects {
    group = UserpilotLibraryCoordinates.PUBLISHING_GROUP_ID
}

val detektPlugin = libs.plugins.detekt.get().pluginId
val detektFormatting = libs.detekt.formatting

subprojects {
    apply {
        plugin(detektPlugin)
    }

    detekt {
        config.from(rootProject.files("config/detekt/detekt.yml"))
    }

    dependencies {
        detektPlugins(detektFormatting)
    }
}

tasks {
    withType<DependencyUpdatesTask>().configureEach {
        rejectVersionIf {
            candidate.version.isStableVersion().not()
        }
    }
}
