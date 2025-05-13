import com.vanniktech.maven.publish.SonatypeHost

/**
 * Precompiled script plugin from:
 * https://github.com/cortinico/kotlin-android-template/blob/master/buildSrc/src/main/kotlin/publish.gradle.kts
 *
 * The following plugin tasks care of setting up:
 * - Publishing to Maven Central
 * - GPG Signing with in memory PGP Keys
 *
 * To use it just apply:
 *
 * plugins {
 *     publish
 * }
 *
 * To your build.gradle.kts.
 *
 * If you copy over this file in your project, make sure to copy it inside: buildSrc/src/main/kotlin/publish.gradle.kts.
 * Make sure to copy over also buildSrc/build.gradle.kts otherwise this plugin will fail to
 * compile due to missing dependencies.
 */
plugins {
    id("com.vanniktech.maven.publish") // Maven publishing plugin
    id("signing") // Enables GPG signing for artifacts
}

mavenPublishing {
    // Configure artifact coordinates
    coordinates(
        groupId = UserpilotLibraryCoordinates.PUBLISHING_GROUP_ID,
        artifactId = UserpilotLibraryCoordinates.PUBLISHING_ARTIFACT_ID,
        version = UserpilotLibraryCoordinates.LIBRARY_VERSION
    )

    // Define POM metadata for the artifact
    pom {
        name.set("Userpilot") // Display name of the artifact
        description.set("Userpilot Android SDK") // Brief description of the SDK
        inceptionYear.set("2024") // Year of creation
        url.set("https://github.com/Userpilot/android-sdk") // Project URL

        // License information
        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        // Developer information
        developers {
            developer {
                id.set("Userpilot")
                name.set("Userpilot")
                email.set("dev@userpilot.co")
            }
        }

        // Source Control Management (SCM) details
        scm {
            url.set("https://github.com/Userpilot/android-sdk")
        }
    }

    // Publish artifacts to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    // Enable GPG signing for all publications
    signAllPublications()
}

/*
publishing {
    publications {
        create<MavenPublication>("LocalRelease") {
            groupId = UserpilotLibraryCoordinates.PUBLISHING_GROUP_ID
            artifactId = UserpilotLibraryCoordinates.PUBLISHING_ARTIFACT_ID
            version = UserpilotLibraryCoordinates.LIBRARY_VERSION

            // Publishing the AAR (Android Archive)
            afterEvaluate {
                from(components["release"])
            }
        }
    }

    repositories {
        mavenLocal()
    }
}
*/