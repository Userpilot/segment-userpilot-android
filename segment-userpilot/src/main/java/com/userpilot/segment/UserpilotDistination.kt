package com.userpilot.segment

import android.content.Context
import com.segment.analytics.kotlin.android.plugins.AndroidLifecycle
import com.segment.analytics.kotlin.core.BaseEvent
import com.segment.analytics.kotlin.core.GroupEvent
import com.segment.analytics.kotlin.core.IdentifyEvent
import com.segment.analytics.kotlin.core.ScreenEvent
import com.segment.analytics.kotlin.core.Settings
import com.segment.analytics.kotlin.core.TrackEvent
import com.segment.analytics.kotlin.core.platform.DestinationPlugin
import com.segment.analytics.kotlin.core.platform.Plugin
import com.segment.analytics.kotlin.core.platform.plugins.logger.LogKind.ERROR
import com.segment.analytics.kotlin.core.platform.plugins.logger.log
import com.segment.analytics.kotlin.core.utilities.toContent
import com.userpilot.Userpilot
import com.userpilot.UserpilotConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.jetbrains.annotations.VisibleForTesting

/**
 * A [DestinationPlugin] implementation for integrating the Userpilot SDK with Segment's Kotlin analytics library.
 *
 * This class handles forwarding `identify`, `track`, and `screen` events to the Userpilot SDK.
 * It supports lazy initialization and respects integration settings specified in Segment's configuration.
 *
 * @property context The Android [Context] used for initializing the Userpilot SDK.
 * @property config Optional lambda to customize the [UserpilotConfig] when initializing Userpilot.
 *
 */
class UserpilotDestination(
    private val context: Context,
    private val config: (UserpilotConfig.() -> Unit)? = null,
) : DestinationPlugin(), AndroidLifecycle {

    override val key: String = "Userpilot Mobile"

    /**
     * Returns the plugin version including build type.
     */
    val version: String
        get() = "${BuildConfig.BUILD_TYPE}-${BuildConfig.PLUGIN_VERSION}"

    /**
     * Instance of the Userpilot SDK.
     */
    var userpilot: Userpilot? = null
        internal set

    /**
     * Called when Segment updates the destination settings.
     * Initializes Userpilot if settings are available and this is the first load.
     */
    @Suppress("TooGenericExceptionCaught")
    override fun update(settings: Settings, type: Plugin.UpdateType) {
        super.update(settings, type)

        if (settings.hasIntegrationSettings(this)) {
            try {
                if (type == Plugin.UpdateType.Initial) {
                    initUserpilot(settings)
                    analytics.log("$key destination loaded")
                }
            } catch (exception: Exception) {
                analytics.log(
                    message = "$key destination failed to load. ${exception.message}",
                    kind = ERROR
                )
            }
        } else {
            analytics.log(message = "$key destination is disabled via settings")
        }
    }

    /**
     * Initializes the Userpilot SDK using the token provided in Segment settings.
     */
    @VisibleForTesting
    internal fun initUserpilot(settings: Settings) {
        getUserpilotSettings(settings)?.let {
            if (userpilot == null) {
                userpilot = Userpilot(context, it.token) {
                    config?.invoke(this)
                }
            }
        }
    }

    @VisibleForTesting
    internal fun getUserpilotSettings(settings: Settings): UserpilotSettings? {
        return settings.destinationSettings(key)
    }

    /**
     * Handles `identify` calls from Segment and forwards them to Userpilot.
     */
    override fun identify(payload: IdentifyEvent): BaseEvent {
        userpilot?.identify(
            userID = payload.userId,
            properties = payload.traits.mapToUserpilotProperties()
        )
        return payload
    }

    /**
     * Handles `group` calls from Segment and forwards them to Userpilot.
     */
    override fun group(payload: GroupEvent): BaseEvent {
        (userpilot?.settings()?.get("User") as? User)?.userID?.let {
            val properties = payload.traits.mapToUserpilotProperties() ?: emptyMap()
            val company = mapOf("id" to payload.groupId) + properties
            userpilot?.identify(it, company = company)
        }
        return payload
    }

    /**
     * Handles `track` calls from Segment and forwards valid events to Userpilot.
     */
    override fun track(payload: TrackEvent): BaseEvent {
        if (payload.isValid) {
            userpilot?.track(
                name = payload.event,
                properties = payload.properties.mapToUserpilotProperties()
            )
        }
        return payload
    }

    /**
     * Handles `screen` calls from Segment and forwards valid screen views to Userpilot.
     */
    override fun screen(payload: ScreenEvent): BaseEvent {
        if (payload.isValid) {
            userpilot?.screen(title = payload.name)
        }
        return payload
    }

    override fun reset() {
        userpilot?.destroy()
    }

    /**
     * Maps a [JsonObject] to a format compatible with Userpilot.
     * Recursively includes only allowed primitive types and nested maps.
     */
    private fun JsonObject.mapToUserpilotProperties(): Map<String, Any>? {
        val map = hashMapOf<String, Any>()
        forEach { (key, value) ->
            val content = value.toContent()
            if (content != null && content.isAllowedPropertyType) {
                map[key] = content
            }
        }
        return if (map.isEmpty()) null else map
    }

    /**
     * Recursively checks whether a value is allowed by Userpilot.
     */
    private val Any.isAllowedPropertyType: Boolean
        get() = when (this) {
            is String, is Boolean, is Int, is Long, is Double -> true
            is Map<*, *> -> this.keys.all { it is String } &&
                this.values.all { it?.isAllowedPropertyType == true }
            else -> false
        }

    /**
     * Checks if a [TrackEvent] is valid.
     */
    private val TrackEvent.isValid: Boolean
        get() = this.event.isNotEmpty()

    /**
     * Checks if a [ScreenEvent] is valid.
     */
    private val ScreenEvent.isValid: Boolean
        get() = this.name.isNotEmpty()
}

/**
 * Serializable class for holding Userpilot token configuration parsed from Segment settings.
 */
@Serializable
data class UserpilotSettings(
    var token: String
)

internal data class User(
    var userID: String = ""
)
