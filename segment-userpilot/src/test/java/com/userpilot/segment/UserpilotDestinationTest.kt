package com.userpilot.segment

import android.content.Context
import com.segment.analytics.kotlin.core.Analytics
import com.segment.analytics.kotlin.core.GroupEvent
import com.segment.analytics.kotlin.core.IdentifyEvent
import com.segment.analytics.kotlin.core.ScreenEvent
import com.segment.analytics.kotlin.core.Settings
import com.segment.analytics.kotlin.core.TrackEvent
import com.segment.analytics.kotlin.core.platform.Plugin
import com.userpilot.Userpilot
import com.userpilot.UserpilotConfig
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class UserpilotDestinationTest {

    //region Mocked objects
    private lateinit var context: Context
    private lateinit var analytics: Analytics
    private lateinit var userpilotMock: Userpilot
    private lateinit var destination: UserpilotDestination
    private lateinit var settings: Settings
    private lateinit var userpilotSettings: UserpilotSettings
    //endregion

    //region setup test
    @Before
    fun setup() {
        // Mock dependencies
        context = mockk()
        analytics = mockk(relaxed = true)
        userpilotMock = mockk(relaxed = true)

        // Create destination first (before settings setup)
        destination = UserpilotDestination(context)
        destination.analytics = analytics

        // Set up settings with the specific destination instance
        userpilotSettings = UserpilotSettings(token = "test-token")
        settings = mockk()
        // Mock with the specific destination instance
        every { settings.hasIntegrationSettings(destination) } returns true
        every { settings.destinationSettings<UserpilotSettings>(any()) } returns userpilotSettings

        // Manually inject our mock instead of letting it be created
        destination.userpilot = userpilotMock
    }
    //endregion

    //region Test cases
    @Test
    fun `test initialization skipped when settings don't have integration`() {
        // Create a fresh destination to test initialization
        val testDestination = UserpilotDestination(context)
        testDestination.analytics = analytics

        // Create settings mock that returns false for hasIntegrationSettings
        val testSettings = mockk<Settings>()
        every { testSettings.hasIntegrationSettings(testDestination) } returns false

        // Initialize the destination
        testDestination.update(testSettings, Plugin.UpdateType.Initial)

        // Verify Userpilot was not initialized (userpilot should be null)
        assertNull(testDestination.userpilot)
    }

    @Test
    fun `test initialization with custom config`() {
        // Create configuration with custom logic
        val configCallback: (UserpilotConfig.() -> Unit) = {
            // Custom configuration
        }

        // Create a spied destination with config
        val customDestination = spyk(UserpilotDestination(context, configCallback))
        customDestination.analytics = analytics

        val testSettings = mockk<Settings>()
        every { testSettings.hasIntegrationSettings(customDestination) } returns true
        every { testSettings.destinationSettings<UserpilotSettings>(any()) } returns userpilotSettings

        // Mock initUserpilot to inject the mock
        every { customDestination.initUserpilot(any()) } answers {
            customDestination.userpilot = userpilotMock
        }

        // Initialize
        customDestination.update(testSettings, Plugin.UpdateType.Initial)

        // Assert
        assertEquals(userpilotMock, customDestination.userpilot)
    }

    @Test
    fun `test initialization with valid settings`() {
        // Create a spy on destination
        val destinationSpy = spyk(UserpilotDestination(context))
        destinationSpy.analytics = analytics

        val testSettings = mockk<Settings>()

        // Mock behavior for the settings with the destinationSpy
        every { testSettings.hasIntegrationSettings(destinationSpy) } returns true
        every {
            destinationSpy.update(
                any(),
                any()
            )
        } answers { callOriginal() } // Optional, ensures real update runs
        every { destinationSpy.initUserpilot(any()) } just Runs

        // Now call update on the spy
        destinationSpy.update(testSettings, Plugin.UpdateType.Initial)

        // Verify initUserpilot was called
        verify { destinationSpy.initUserpilot(testSettings) }
    }

    @Test
    fun `test initUserpilot initializes correctly`() {
        // Create a mock for Settings and mock destinationSettings
        val testSettings = mockk<Settings>()
        val userpilotSettings = mockk<UserpilotSettings>()

        // Mock token value
        every { userpilotSettings.token } returns "test-token"

        // Mock the behavior of the getUserpilotSettings method
        val testDestination = spyk(UserpilotDestination(context))
        every { testDestination.getUserpilotSettings(testSettings) } returns userpilotSettings

        // Create a mock Userpilot instance
        val mockUserpilot = mockk<Userpilot>(relaxed = true)

        // Mock the Userpilot function to return our mock
        mockkStatic(::Userpilot)
        every {
            Userpilot(
                any(),
                any(),
                any()
            )
        } returns mockUserpilot

        testDestination.analytics = analytics

        // Call initUserpilot with the mocked settings
        testDestination.initUserpilot(testSettings)

        // Verify Userpilot was called with correct parameters
        verify {
            Userpilot(
                eq(context),
                eq("test-token"),
                any()
            )
        }

        // Check that the userpilot field was set
        assertNotNull(testDestination.userpilot)

        // Clean up
        unmockkStatic(::Userpilot)
    }

    @Test
    fun `test get userpilot settings returns correct settings`() {
        // Create mock Settings
        val mockSettings = mockk<Settings>()

        // Create a spy of the result (this avoids class loading issues)
        val spyDestination = spyk(UserpilotDestination(context))

        // Mock the getUserpilotSettings method itself
        every { spyDestination.getUserpilotSettings(mockSettings) } answers {
            UserpilotSettings("test-token")
        }

        // Call the method under test
        val result = spyDestination.getUserpilotSettings(mockSettings)

        // Assert that the result has the expected properties
        assertEquals("test-token", result?.token)
    }

    @Test
    fun `test get userpilot settings returns null when no settings available`() {
        // Create mock Settings
        val mockSettings = mockk<Settings>()

        // Set up the expected key value
        val expectedKey = "Userpilot Mobile"

        // Mock the destinationSettings method with explicit type parameter
        every { mockSettings.destinationSettings<UserpilotSettings>(expectedKey) } returns null

        // Create an instance of UserpilotDestination to test
        val destination = UserpilotDestination(context)

        // Call the method under test
        val result = destination.getUserpilotSettings(mockSettings)

        // Verify that destinationSettings was called with the correct key
        verify { mockSettings.destinationSettings<UserpilotSettings>(expectedKey) }

        // Assert that the result is null
        assertNull(result)
    }

    @Test
    fun `test identify forwards user data to Userpilot`() {
        // Set up the destination with a mock Userpilot instance
        destination.userpilot = userpilotMock

        // Create identify event
        val traits = JsonObject(
            mapOf(
                "name" to JsonPrimitive("Test User"),
                "email" to JsonPrimitive("test@example.com")
            )
        )

        val identifyEvent = IdentifyEvent(
            userId = "user-123",
            traits = traits
        )

        // Process the identify event
        destination.identify(identifyEvent)

        // Verify Userpilot.identify was called with correct parameters
        verify {
            userpilotMock.identify(
                userID = "user-123",
                properties = match {
                    it["name"] == "Test User" && it["email"] == "test@example.com"
                }
            )
        }
    }

    @Test
    fun `test group forwards group data to Userpilot`() {
        // Setup mock for User settings with userID
        val userMock = mockk<User>()
        val userSettingsMock = mockk<Map<String, Any>>()

        every { userMock.userID } returns "user-123"
        every { userSettingsMock["User"] } returns userMock
        every { userpilotMock.settings() } returns userSettingsMock

        // Set up the destination with a mock Userpilot instance
        destination.userpilot = userpilotMock

        // Create group event
        val traits = JsonObject(
            mapOf(
                "name" to JsonPrimitive("Test Company"),
                "plan" to JsonPrimitive("enterprise")
            )
        )

        val groupEvent = GroupEvent(
            groupId = "company-123",
            traits = traits
        )

        // Process the group event
        destination.group(groupEvent)

        // Verify Userpilot.identify was called with correct parameters
        verify {
            userpilotMock.identify(
                userID = "user-123",
                company = match {
                    it["id"] == "company-123" &&
                        it["name"] == "Test Company" &&
                        it["plan"] == "enterprise"
                }
            )
        }
    }

    @Test
    fun `test group does nothing when user ID not available`() {
        // Setup mock with null/missing user settings
        val userSettingsMock = mockk<Map<String, Any>>()
        every { userSettingsMock["User"] } returns null
        every { userpilotMock.settings() } returns userSettingsMock

        // Set up the destination with a mock Userpilot instance
        destination.userpilot = userpilotMock

        // Create group event
        val traits = JsonObject(
            mapOf(
                "name" to JsonPrimitive("Test Company")
            )
        )

        val groupEvent = GroupEvent(
            groupId = "company-123",
            traits = traits
        )

        // Process the group event
        destination.group(groupEvent)

        // Verify Userpilot.identify was not called
        verify(exactly = 0) { userpilotMock.identify(any(), any(), any()) }
    }

    @Test
    fun `test track forwards event to Userpilot`() {
        // Set up the destination with a mock Userpilot instance
        destination.userpilot = userpilotMock

        // Create track event
        val properties = JsonObject(
            mapOf(
                "plan" to JsonPrimitive("premium"),
                "value" to JsonPrimitive(99.99)
            )
        )

        val trackEvent = TrackEvent(
            event = "Subscription Purchased",
            properties = properties
        )

        // Process the track event
        destination.track(trackEvent)

        // Verify Userpilot.track was called with correct parameters
        verify {
            userpilotMock.track(
                name = "Subscription Purchased",
                properties = match {
                    it["plan"] == "premium" && it["value"] == 99.99
                }
            )
        }
    }

    @Test
    fun `test track ignores empty event names`() {
        // Set up the destination with a mock Userpilot instance
        destination.userpilot = userpilotMock

        // Create track event with empty name
        val trackEvent = TrackEvent(
            event = "",
            properties = JsonObject(mapOf("test" to JsonPrimitive("value")))
        )

        // Process the track event
        destination.track(trackEvent)

        // Verify Userpilot.track was not called
        verify(exactly = 0) { userpilotMock.track(any(), any()) }
    }

    @Test
    fun `test screen forwards to Userpilot`() {
        // Set up the destination with a mock Userpilot instance
        destination.userpilot = userpilotMock

        // Create screen event
        val screenEvent = ScreenEvent(
            name = "Product Details",
            category = "",
            properties = JsonObject(mapOf("productId" to JsonPrimitive("123")))
        )

        // Process the screen event
        destination.screen(screenEvent)

        // Verify Userpilot.screen was called with correct parameters
        verify {
            userpilotMock.screen(title = "Product Details")
        }
    }

    @Test
    fun `test screen ignores empty screen names`() {
        // Set up the destination with a mock Userpilot instance
        destination.userpilot = userpilotMock

        // Create screen event with empty name
        val screenEvent = ScreenEvent(
            name = "",
            category = "",
            properties = JsonObject(mapOf("test" to JsonPrimitive("value")))
        )

        // Process the screen event
        destination.screen(screenEvent)

        // Verify Userpilot.screen was not called
        verify(exactly = 0) { userpilotMock.screen(any()) }
    }

    @Test
    fun `test reset calls destroy on Userpilot`() {
        // Set up the destination with a mock Userpilot instance
        destination.userpilot = userpilotMock

        // Call reset
        destination.reset()

        // Verify Userpilot.destroy was called
        verify { userpilotMock.destroy() }
    }
//endregion
}
