package com.tealium.remotecommands.fullstory

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
        fullStoryRemoteCommand = FullStoryRemoteCommand()

        fullStoryRemoteCommand.fullStoryInstance = mockFullStoryInstance
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

}