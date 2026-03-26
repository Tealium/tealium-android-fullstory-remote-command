package com.tealium.remotecommands.fullstory

import com.fullstory.FS
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FullStoryRemoteCommandTest {

    @MockK
    lateinit var mockFullStoryInstance: FullStoryCommand

    lateinit var fullStoryRemoteCommand: FullStoryRemoteCommand

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        fullStoryRemoteCommand = FullStoryRemoteCommand(fullStoryInstance =  mockFullStoryInstance)
    }

    @Test
    fun testSplitCommands() {
        val json = JSONObject()
        json.put("command_name", "logevent, identify, restart")
        val commands = fullStoryRemoteCommand.splitCommands(json)

        assertEquals(3, commands.count())
        assertEquals("logevent", commands[0])
        assertEquals("identify", commands[1])
        assertEquals("restart", commands[2])
    }

    @Test
    fun testIdentify() {
        val payload = JSONObject()
        payload.put("uid", "test123")
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.IDENTIFY), payload)

        every {
            mockFullStoryInstance.identifyUser(any(), any())
        } just Runs

        verify {
            mockFullStoryInstance.identifyUser("test123")
        }

        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testLogEvent() {
        val eventParams = JSONObject()
        eventParams.put("key1", "val1")
        eventParams.put("key2", "val2")
        eventParams.put("key3", "val3")

        val payload = JSONObject()
        payload.put("event_name", "test123")
        payload.put("event", eventParams)
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.LOG_EVENT), payload)

        every {
            mockFullStoryInstance.logEvent(any(), any())
        } just Runs

        verify {
            mockFullStoryInstance.logEvent(
                "test123",
                mapOf("key1" to "val1", "key2" to "val2", "key3" to "val3")
            )
        }

        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testUserData() {
        val userData = JSONObject()
        userData.put("key1", "val1")
        userData.put("key2", "val2")
        userData.put("key3", "val3")

        val payload = JSONObject()
        payload.put(Keys.USER_VARIABLES, userData)
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.SET_USER_VARIABLES), payload)

        every {
            mockFullStoryInstance.setUserData(any())
        } just Runs

        verify {
            mockFullStoryInstance.setUserData(
                mapOf("key1" to "val1", "key2" to "val2", "key3" to "val3")
            )
        }

        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testShutdown() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.SHUTDOWN), JSONObject())

        verify { mockFullStoryInstance.shutdown() }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testRestart() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.RESTART), JSONObject())

        verify { mockFullStoryInstance.restart() }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testAnonymize() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.ANONYMIZE), JSONObject())

        verify { mockFullStoryInstance.anonymize() }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testResetIdleTimer() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.RESET_IDLE_TIMER), JSONObject())

        verify { mockFullStoryInstance.resetIdleTimer() }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testConsentGranted() {
        val payload = JSONObject()
        payload.put(Keys.CONSENT_GRANTED, true)
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.CONSENT), payload)

        verify { mockFullStoryInstance.consent(true) }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testConsentRevoked() {
        val payload = JSONObject()
        payload.put(Keys.CONSENT_GRANTED, false)
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.CONSENT), payload)

        verify { mockFullStoryInstance.consent(false) }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testLog() {
        val payload = JSONObject()
        payload.put(Keys.LOG_LEVEL, "error")
        payload.put(Keys.LOG_MESSAGE, "Login failed")
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.LOG), payload)

        verify { mockFullStoryInstance.log(FS.LogLevel.ERROR, "Login failed") }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testLogSkippedWhenMissingParams() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.LOG), JSONObject())

        verify(exactly = 0) { mockFullStoryInstance.log(any<FS.LogLevel>(), any()) }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testIdentifySkippedWhenUidBlank() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.IDENTIFY), JSONObject())

        verify(exactly = 0) { mockFullStoryInstance.identifyUser(any(), any()) }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testSetUserVariablesSkippedWhenMissing() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.SET_USER_VARIABLES), JSONObject())

        verify(exactly = 0) { mockFullStoryInstance.setUserData(any()) }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testConsentSkippedWhenKeyMissing() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.CONSENT), JSONObject())

        verify(exactly = 0) { mockFullStoryInstance.consent(any()) }
        confirmVerified(mockFullStoryInstance)
    }

    @Test
    fun testLogEventSkippedWhenNameBlank() {
        fullStoryRemoteCommand.parseCommands(arrayOf(Commands.LOG_EVENT), JSONObject())

        verify(exactly = 0) { mockFullStoryInstance.logEvent(any(), any()) }
        confirmVerified(mockFullStoryInstance)
    }

}