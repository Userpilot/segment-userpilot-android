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
        name.set(UserpilotLibraryCoordinates.NAME)
        description.set(UserpilotLibraryCoordinates.DESCRIPTION)
        inceptionYear.set(UserpilotLibraryCoordinates.INCEPTION_YEAR)
        url.set(UserpilotLibraryCoordinates.ORG_URL)

        // License information
        licenses {
            license {
                name.set(UserpilotLibraryCoordinates.LICENSE)
                url.set(UserpilotLibraryCoordinates.LICENSE_URL)
            }
        }

        // Developer information
        developers {
            developer {
                id.set(UserpilotLibraryCoordinates.ORG_ID)
                name.set(UserpilotLibraryCoordinates.ORG_NAME)
                organization.set(UserpilotLibraryCoordinates.ORG_NAME)
                organizationUrl.set(UserpilotLibraryCoordinates.ORG_URL)
                email.set(UserpilotLibraryCoordinates.ORG_EMAIL)
            }
        }

        // Source Control Management (SCM) details
        scm {
            connection.set("scm:git:git://github.com/" + UserpilotLibraryCoordinates.GITHUB_PATH + ".git")
            developerConnection.set("scm:git:ssh://github.com:" + UserpilotLibraryCoordinates.GITHUB_PATH + ".git")
            url.set("https://github.com/" + UserpilotLibraryCoordinates.GITHUB_PATH)
        }
    }

    // Publish artifacts to Maven Central
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, false)

    // Enable GPG signing for all publications
    signAllPublications()
}
