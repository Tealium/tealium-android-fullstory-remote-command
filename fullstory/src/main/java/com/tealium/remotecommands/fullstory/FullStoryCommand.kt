package com.tealium.remotecommands.fullstory

import com.fullstory.FS

interface FullStoryCommand {
    fun identifyUser(id: String, data: Map<String, Any>? = null)
    fun setUserData(data: Map<String, Any>)
    fun anonymize()

    fun logEvent(eventName: String, eventData: Map<String, Any>? = null)

    fun shutdown()
    fun restart()

    fun consent(userConsents: Boolean)
    fun resetIdleTimer()
    fun log(logLevel: FS.LogLevel, message: String)
}