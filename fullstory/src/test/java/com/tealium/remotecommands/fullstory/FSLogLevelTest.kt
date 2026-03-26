package com.tealium.remotecommands.fullstory

import com.fullstory.FS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FSLogLevelTest {

    @Test
    fun testShortNames() {
        assertEquals(FS.LogLevel.LOG,   "log".toFSLogLevel())
        assertEquals(FS.LogLevel.DEBUG, "debug".toFSLogLevel())
        assertEquals(FS.LogLevel.INFO,  "info".toFSLogLevel())
        assertEquals(FS.LogLevel.WARN,  "warning".toFSLogLevel())
        assertEquals(FS.LogLevel.ERROR, "error".toFSLogLevel())
    }

    @Test
    fun testFslogPrefixNames() {
        assertEquals(FS.LogLevel.LOG,   "fslog_log".toFSLogLevel())
        assertEquals(FS.LogLevel.DEBUG, "fslog_debug".toFSLogLevel())
        assertEquals(FS.LogLevel.INFO,  "fslog_info".toFSLogLevel())
        assertEquals(FS.LogLevel.WARN,  "fslog_warning".toFSLogLevel())
        assertEquals(FS.LogLevel.ERROR, "fslog_error".toFSLogLevel())
    }

    @Test
    fun testEnumNameFormat() {
        // FS.LogLevel enum names serialized via toString() — e.g. when passed through TealiumHelper data map
        assertEquals(FS.LogLevel.LOG,   "LOG".toFSLogLevel())
        assertEquals(FS.LogLevel.DEBUG, "DEBUG".toFSLogLevel())
        assertEquals(FS.LogLevel.INFO,  "INFO".toFSLogLevel())
        assertEquals(FS.LogLevel.WARN,  "WARN".toFSLogLevel())
        assertEquals(FS.LogLevel.ERROR, "ERROR".toFSLogLevel())
    }

    @Test
    fun testCaseInsensitive() {
        assertEquals(FS.LogLevel.ERROR, "ERROR".toFSLogLevel())
        assertEquals(FS.LogLevel.DEBUG, "DEBUG".toFSLogLevel())
        assertEquals(FS.LogLevel.WARN,  "WARNING".toFSLogLevel())
        assertEquals(FS.LogLevel.INFO,  "INFO".toFSLogLevel())
    }

    @Test
    fun testUnknownReturnsNull() {
        assertNull("verbose".toFSLogLevel())
        assertNull("".toFSLogLevel())
        assertNull("trace".toFSLogLevel())
        assertNull("FS.LogLevel.ERROR".toFSLogLevel())
    }
}
