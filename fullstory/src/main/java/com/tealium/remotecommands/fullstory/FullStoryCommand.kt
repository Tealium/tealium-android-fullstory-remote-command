package com.tealium.remotecommands.fullstory

interface FullStoryCommand {
    fun identifyUser(id: String, data: Map<String, Any>? = null)
    fun setUserData(data: Map<String, Any>)
    fun anonymize()
    fun shutdown()
    fun restart()

    fun logEvent(eventName: String, eventData: Map<String, Any>? = null)
}