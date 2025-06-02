package com.userpilot.segment.sample.managers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.segment.analytics.kotlin.android.Analytics
import com.segment.analytics.kotlin.core.Analytics
import com.userpilot.interfaces.UserpilotNavigationHandler
import com.userpilot.segment.UserpilotDestination
import com.userpilot.segment.sample.BuildConfig

/**
 * Created by Motasem Hamed
 * on 14 Apr, 2025
 */

object SegmentManager {

    //region Properties
    /**
     * We have made the Userpilot instance optional to allow flexibility in switching the token at runtime.
     * However, this approach is not recommended because the token is intended to be configured only once.
     * For better practice, declare the Userpilot instance using lateinit to ensure a single, definitive configuration.
     */
    private lateinit var analytics: Analytics
    private val isAnalyticsInitialized: Boolean
        get() = this::analytics.isInitialized

    @SuppressLint("StaticFieldLeak")
    private lateinit var userpilotDestination: UserpilotDestination
    private val isUserpilotInitialized: Boolean
        get() = this::userpilotDestination.isInitialized
    //endregion

    //region Setup
    fun initialize(context: Context) {
        analytics = Analytics(
            BuildConfig.SEGMENT_WRITE_KEY,
            context
        ) {
            this.trackApplicationLifecycleEvents = true
        }

        // Creating a reference to this destination will allow us to access the underlying
        // Userpilot SDK in other areas of the code. This enables access to any additional SDK
        // functionality desired, like the trigger experience manually
        userpilotDestination = UserpilotDestination(context) {
            // optionally apply customizations using the UserpilotConfig here
            loggingEnabled = true
            navigationHandler = object : UserpilotNavigationHandler {
                override suspend fun navigateTo(uri: Uri): Boolean {
                    if (uri.scheme?.contains("http") == true || uri.scheme?.contains("https") == true) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    } else {
                        Intent(Intent.ACTION_VIEW).apply {
                            data = uri
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }.also {
                            context.startActivity(it)
                        }
                    }
                    return true
                }
            }
        }
        analytics.add(userpilotDestination)
        settings()
    }
    //endregion

    //region Userpilot SDK APIs

    // Userpilot Settings
    private fun settings(): Map<String, Any?> {
        if (!isUserpilotInitialized) return mapOf()
        return userpilotDestination.userpilot?.settings() ?: mapOf()
    }

    // Identify user
    fun identify(
        userID: String,
        properties: Map<String, Any>? = null,
    ) {
        if (!isAnalyticsInitialized) return
        analytics.identify(userID, properties)
    }

    fun group(
        groupID: String,
        properties: Map<String, Any>? = null,
    ) {
        if (!isAnalyticsInitialized) return
        analytics.group(groupID, properties)
    }

    // Identify user
    fun anonymous() {
        if (!isAnalyticsInitialized) return
        analytics.identify()
    }

    // Logout user
    fun logout() {
        if (!isAnalyticsInitialized) return
        analytics.reset()
    }

    // Track screens
    fun screen(screenTitle: String) {
        if (!isAnalyticsInitialized) return
        analytics.screen(screenTitle)
    }

    // Track user events
    fun track(eventName: String, properties: Map<String, Any>? = null) {
        if (!isAnalyticsInitialized) return
        analytics.track(eventName, properties)
    }
}
